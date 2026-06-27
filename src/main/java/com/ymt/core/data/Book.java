package com.ymt.core.data;

import com.ymt.util.Guard;

/**
 * Minimal book model.
 *
 * @param title     display title shown under the cover
 * @param image classpath location of the cover image,
 *                  e.g. "/images/covers/clean-code.png" (may be null)
 */

public class Book {
    private final String id;
    private final String title;
    private final byte[] image;
    private final String author;
    private String filePath;
    private final String description;

    public Book(String id, String title, byte[] image, String author, String filePath, String description) {
        this.id = Guard.orEmpty(id);
        this.title = Guard.orEmpty(title);
        this.image = image;
        this.author = Guard.orEmpty(author);
        this.filePath = Guard.orEmpty(filePath);
        this.description = Guard.orEmpty(description);
    }

    public String getID() {
        return this.id;
    }
    
    public String getTitle() {
        return this.title;
    }

    public byte[] getImage() {
        return this.image;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getDescription() {
        return this.getDescription();
    }

}

