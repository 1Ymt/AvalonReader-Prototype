package com.ymt.ui.controller;

import java.io.ByteArrayInputStream;

import com.ymt.core.data.Book;
import com.ymt.interfaces.Navigator;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

/**
 * Controller for chapter.fxml.
 *
 * All handlers are stubs that print TODO messages; logic is wired up later.
 */
public class ChapterController {

    @FXML private ImageView coverImage;
    @FXML private TextArea  descriptionArea;
    @FXML private TextField searchField;
    @FXML private FlowPane  chapterContainer;

    private Navigator navigator;
    private Book book;
    
    public ChapterController(Navigator navigator) {
        this.navigator = navigator;
    }
    
    @FXML
    private void initialize() {
        
    }

    @FXML
    private void onReturn() {
        
    }

    @FXML
    private void onSearch() {
        // LinkedHashSet<String> chapters = new LinkedHashSet<>();

        // for (String string : contentProvider.getChapterLabels()) {
        //     if (containsIgnoreCase(string, searchField.getText()))
        //         chapters.add(string);
        // }
        // loadBookChapters(chapters);
    }

    public void setBook(Book book) {
        this.book = book;
        loadBook();
    }

    private void loadBook() {
        coverImage.setImage(loadCover(book.getImage()));
        //descriptionArea.setText(book.getDescription());
    }

    private Image loadCover(byte[] imageByte) {
        if (imageByte != null) {
            return new Image(new ByteArrayInputStream(imageByte));
        }
        return placeholder();
    }

    private Image placeholder() {
        var url = getClass().getResource("/img/test.jpg");
        // Returns null if you haven't added a placeholder yet; ImageView
        // simply shows nothing in that case, which is safe.
        return url != null ? new Image(url.toExternalForm()) : null;
    }

    private static boolean containsIgnoreCase(String str, String searchStr)     {
        if(str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }
}
