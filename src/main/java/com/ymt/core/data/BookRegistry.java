package com.ymt.core.data;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.logger.AppLogger;

public class BookRegistry {
    private static final Logger LOG = AppLogger.get(BookRegistry.class);
    
    private final LinkedHashMap<String, Book> bookRepository;

    public BookRegistry() {
        this.bookRepository = new LinkedHashMap<>();
    }

    public void loadBook(String id, Book book) {
        if (id == null || book == null) {
            LOG.log(Level.WARNING, "ID or Book is null");
            return;
        }

        if (bookRepository.containsKey(id) || bookRepository.containsValue(book)) {
            LOG.log(Level.WARNING, "Duplicate ID or Value in bookRepository");
            return;
        }

        bookRepository.put(id, book);
        LOG.log(Level.FINE, "Adding "+ id + " to bookRepository");
    }

    public Object[] all() {
        return bookRepository.values().toArray();
    }

    public Book getBook(String id) {
        if (bookRepository.containsKey(id)) {
            return bookRepository.get(id);
        }
        return null;
    }
}
