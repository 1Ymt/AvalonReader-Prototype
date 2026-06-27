package com.ymt.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import java.util.logging.Logger;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;
import com.ymt.logger.AppLogger;

/**
 * Parsing/content layer on top of {@link Reader}.
 *
 * Wraps the EPUB parser and hands back chapter HTML that is ready to render: every
 * image the chapter references is resolved against the EPUB archive and inlined as a
 * {@code data:} URI. This covers cases the underlying parser doesn't handle for a
 * base-URL-less WebView, namely:
 * <ul>
 *   <li>SVG-wrapped illustrations — {@code <svg>…<image xlink:href="../Images/x.jpeg"/></svg>}
 *       (the parser only rewrites {@code <img src>}), and</li>
 *   <li>malformed inline data URIs like {@code src="../Images/data:image/png;base64,…"}
 *       where a folder path was prepended in front of an already-inline image.</li>
 * </ul>
 *
 * The UI just asks for {@link #getChapterLabels()} and {@link #getChapterHtml(String)};
 * it needs to know nothing about zips, base64 or the parser's quirks.
 */
public class EpubContentProvider {

    private static final Logger LOG = AppLogger.get(EpubContentProvider.class);

    /** Image-bearing attributes we rewrite: {@code <img src>} and SVG {@code <image xlink:href>}. */
    private static final Pattern IMG_REF =
            Pattern.compile("(?i)(src|xlink:href)\\s*=\\s*\"([^\"]*)\"");
    private static final Pattern IMG_EXT =
            Pattern.compile("(?i).*\\.(png|jpe?g|gif|svg|webp|bmp)$");

    private final String epubPath;

    /** Chapter labels in reading order. */
    private final List<String> chapterLabels = new ArrayList<>();
    /** Chapter label -> raw section HTML (before image inlining). */
    private final Map<String, String> rawHtml = new LinkedHashMap<>();
    /** Chapter label -> HTML with images inlined (computed lazily, then cached). */
    private final Map<String, String> inlinedHtml = new HashMap<>();

    /** Basename (lower-cased) of every image inside the EPUB -> its zip entry name. */
    private final Map<String, String> imageEntries = new HashMap<>();
    /** Zip entry name -> its "data:" URI, so each image is read & encoded only once. */
    private final Map<String, String> dataUriCache = new HashMap<>();

    /**
     * Opens the EPUB, reads every chapter and catalogues its image resources.
     *
     * @throws ReadingException if the archive can't be parsed.
     */
    public EpubContentProvider(String epubPath) throws ReadingException {
        this.epubPath = epubPath;

        Reader reader = new Reader();
        reader.setIsIncludingTextContent(true); // enables the plain-text fallback below
        reader.setFullContent(epubPath);

        indexImages();

        int pageCount = reader.getToc().getNavMap().getNavPoints().size();
        for (int i = 0; i < pageCount; i++) {
            try {
                BookSection section = reader.readSection(i);
                String label = section.getLabel();
                if (label == null)
                    continue;
                String content = section.getSectionContent();
                String html = (content != null && !content.isBlank())
                        ? content
                        : "<p>" + escapeHtml(section.getSectionTextContent()) + "</p>";
                if (!rawHtml.containsKey(label))
                    chapterLabels.add(label);
                rawHtml.put(label, html);
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }

    /** Chapter labels in reading order, suitable for a navigation dropdown. */
    public List<String> getChapterLabels() {
        return chapterLabels;
    }

    /** The chapter's HTML with all EPUB images inlined as data URIs; "" if unknown. */
    public String getChapterHtml(String label) {
        if (label == null)
            return "";
        return inlinedHtml.computeIfAbsent(label, l -> inlineImages(rawHtml.get(l)));
    }

    // -------------------------------------------------------------- images

    /** Records every image entry in the EPUB zip, keyed by lower-cased basename. */
    private void indexImages() {
        try (ZipFile zip = new ZipFile(epubPath)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                String base = baseName(name).toLowerCase();
                if (IMG_EXT.matcher(base).matches()) {
                    imageEntries.putIfAbsent(base, name); // first match wins
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rewrites every {@code src}/{@code xlink:href} that points to an image bundled in
     * the EPUB into a self-contained {@code data:} URI, so it renders without a base
     * URL. Leaves real external links and unknown references untouched.
     */
    private String inlineImages(String html) {
        if (html == null || html.isEmpty() || imageEntries.isEmpty())
            return html != null ? html : "";

        Matcher m = IMG_REF.matcher(html);
        StringBuilder out = new StringBuilder();
        while (m.find()) {
            String resolved = resolveImageRef(m.group(2));
            String replacement = resolved != null ? m.group(1) + "=\"" + resolved + "\"" : m.group(0);
            m.appendReplacement(out, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(out);
        return out.toString();
    }

    /** Resolves a reference to a {@code data:} URI, or null to leave it as-is. */
    private String resolveImageRef(String value) {
        if (value == null || value.isEmpty())
            return null;
        // Repair malformed refs like "../Images/data:image/png;base64,..." (some
        // generators prepend a folder path in front of an already-inline image).
        int dataIdx = value.indexOf("data:");
        if (dataIdx > 0)  return value.substring(dataIdx);
        if (dataIdx == 0) return null;                  // already a clean data URI
        String entry = imageEntries.get(baseName(value).toLowerCase());
        return entry != null ? toDataUri(entry) : null; // unknown -> leave (e.g. <a> link)
    }

    /** Reads an image entry from the EPUB and base64-encodes it (cached per entry). */
    private String toDataUri(String entry) {
        String cached = dataUriCache.get(entry);
        if (cached != null)
            return cached;
        try (ZipFile zip = new ZipFile(epubPath)) {
            ZipEntry ze = zip.getEntry(entry);
            if (ze == null)
                return null;
            try (InputStream in = zip.getInputStream(ze)) {
                String uri = "data:" + mimeType(entry) + ";base64,"
                        + Base64.getEncoder().encodeToString(in.readAllBytes());
                dataUriCache.put(entry, uri);
                return uri;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // -------------------------------------------------------------- helpers

    /** Last path segment, stripped of any query/fragment. */
    private static String baseName(String path) {
        int slash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        String name = slash >= 0 ? path.substring(slash + 1) : path;
        int cut = name.indexOf('?');
        if (cut >= 0) name = name.substring(0, cut);
        cut = name.indexOf('#');
        if (cut >= 0) name = name.substring(0, cut);
        return name;
    }

    private static String mimeType(String entry) {
        String e = entry.toLowerCase();
        if (e.endsWith(".png"))  return "image/png";
        if (e.endsWith(".jpg") || e.endsWith(".jpeg")) return "image/jpeg";
        if (e.endsWith(".gif"))  return "image/gif";
        if (e.endsWith(".svg"))  return "image/svg+xml";
        if (e.endsWith(".webp")) return "image/webp";
        if (e.endsWith(".bmp"))  return "image/bmp";
        return "application/octet-stream";
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
