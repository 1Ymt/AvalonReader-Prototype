package com.ymt.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ymt.core.data.Book;
import com.ymt.core.data.BookRegistry;

import static org.junit.jupiter.api.Assertions.*;

class BookRegistryTest {

    private BookRegistry registry;
    private Book sampleBook;

    // Runs before EVERY test method — gives each test a clean slate
    @BeforeEach
    void setUp() {
        registry = new BookRegistry();
        sampleBook = new Book("id-1", "Dune", null, "Frank Herbert", "/books/dune.epub", "This is a Test Book");
    }

    // --- loadBook() ---

    @Test
    void loadBook_addsBookSuccessfully() {
        registry.loadBook("id-1", sampleBook);

        Book result = registry.getBook("id-1");
        assertNotNull(result);
        assertEquals("Dune", result.getTitle());
    }

    @Test
    void loadBook_throwsOnNullId() {
        assertThrows(IllegalArgumentException.class, () ->
            registry.loadBook(null, sampleBook)
        );
    }

    @Test
    void loadBook_throwsOnNullBook() {
        assertThrows(IllegalArgumentException.class, () ->
            registry.loadBook("id-1", null)
        );
    }

    @Test
    void loadBook_throwsOnDuplicateId() {
        registry.loadBook("id-1", sampleBook);

        Book anotherBook = new Book("id-2", "Foundation", null, "Isaac Asimov", "/books/foundation.epub", "This is a Test Book");

        // same ID "id-1" again — must be rejected
        assertThrows(IllegalArgumentException.class, () ->
            registry.loadBook("id-1", anotherBook)
        );
    }

    @Test
    void loadBook_throwsOnDuplicateBookObject() {
        registry.loadBook("id-1", sampleBook);

        // same Book object, different ID — still a duplicate value
        assertThrows(IllegalArgumentException.class, () ->
            registry.loadBook("id-99", sampleBook)
        );
    }

    // --- getBook() ---

    @Test
    void getBook_returnsNullForUnknownId() {
        assertNull(registry.getBook("does-not-exist"));
    }

    @Test
    void getBook_returnsCorrectBook() {
        Book other = new Book("id-2", "Foundation", null, "Isaac Asimov", "/books/foundation.epub,", "This is a Test Book");
        registry.loadBook("id-1", sampleBook);
        registry.loadBook("id-2", other);

        assertEquals("Foundation", registry.getBook("id-2").getTitle());
        assertEquals("Dune",       registry.getBook("id-1").getTitle());
    }

    // --- Book getters ---

    @Test
    void book_gettersReturnCorrectValues() {
        assertEquals("id-1",          sampleBook.getID());
        assertEquals("Dune",          sampleBook.getTitle());
        assertEquals("Frank Herbert", sampleBook.getAuthor());
        assertEquals("/books/dune.epub", sampleBook.getFilePath());
        assertNull(sampleBook.getImage());
    }
}
