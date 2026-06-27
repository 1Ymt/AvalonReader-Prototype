package com.ymt.core.manager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.mertakdut.Package.Metadata;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.ReadingException;
import com.ymt.core.data.Book;
import com.ymt.core.data.BookRegistry;
import com.ymt.logger.AppLogger;

public class LoadManager {
    private static final Logger LOG = AppLogger.get(LoadManager.class);

    private final String LIBRARY_DIRECTORY = "library";
    private final BookRegistry bookRegistry;
    private final Reader reader;

    public LoadManager(BookRegistry bookRegistry) {
        this.bookRegistry = bookRegistry;
        this.reader = new Reader();
    }

    public void reload() {
        
    }

    public void load() {
        List<String> bookList = getAllFiles();
        loadBookListIntoRegistry(bookList);
        LOG.log(Level.INFO, "Loading books from library into Book Repository");
    }

    private void loadBookListIntoRegistry(List<String> bookList) {
        for (String bookPath : bookList) {
            try {
                reader.setFullContent(bookPath);
                Metadata data = reader.getInfoPackage().getMetadata();

                String id = bookPath;  //TODO: generate custom id for each book
                String title = data.getTitle();
                byte[] image = reader.getCoverImage();
                String author = data.getCreator();
                String filePath = bookPath;
                String description = data.getDescription();

                Book book = new Book(id, title, image, author, filePath, description);
                bookRegistry.loadBook(id, book);
            } catch (ReadingException e) {
                LOG.log(Level.SEVERE, "Path: " + bookPath + " does not point to an EPUB file", e);
            }
        }
    }
    
    private List<String> getAllFiles() {
        List<String> fileList = new ArrayList<>();
    
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(LIBRARY_DIRECTORY))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.toString());
                }
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Libary path: " + LIBRARY_DIRECTORY + " does not exist", e);
        }

        return fileList;
    }
}
