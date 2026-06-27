package com.ymt.ui.controller;

import java.io.ByteArrayInputStream;
import java.net.URL;

import java.util.logging.Logger;

import com.ymt.core.data.Book;
import com.ymt.enums.SceneType;
import com.ymt.interfaces.Navigator;
import com.ymt.logger.AppLogger;
import com.ymt.records.SceneResult;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookCellController {

    private static final Logger LOG = AppLogger.get(BookCellController.class);

    @FXML private ImageView coverImage;
    @FXML private Label titleLabel;
    
    private final Navigator navigator;
    private Book book;
    
    public BookCellController(Navigator navigator) {
        this.navigator = navigator;
        this.book = null;
    }

    public void setBook(Book book) {
        this.book = book;
        loadBook();
    }

    private void loadBook() {
        titleLabel.setText(book.getTitle());
        coverImage.setImage(loadCover(book.getImage()));
    }

    private Image loadCover(byte[] imageByte) {
        if (imageByte != null) {
            return new Image(new ByteArrayInputStream(imageByte));
        }
        return placeholder();
    }

    private Image placeholder() {
        URL url = getClass().getResource("/img/test.jpg");

        if (url != null) {
            new Image(url.toExternalForm());
        }
        return null;  
    }

    @FXML
    private void openPage() {
        SceneResult<ChapterController> result = navigator.load(SceneType.CHAPTER);
        result.controller().setBook(book);
        navigator.show(result);
    }
}
