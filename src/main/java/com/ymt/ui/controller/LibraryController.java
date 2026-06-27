package com.ymt.ui.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.core.data.Book;
import com.ymt.core.data.BookRegistry;
import com.ymt.enums.SceneType;
import com.ymt.interfaces.CellLoader;
import com.ymt.interfaces.Navigator;
import com.ymt.logger.AppLogger;
import com.ymt.records.CellResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;

public class LibraryController {

    private static final Logger LOG = AppLogger.get(LibraryController.class);

    @FXML private FlowPane bookContainer;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;
    @FXML private ComboBox<String> filterCombo;
    @FXML private Label bookCountLabel;
    @FXML private ToggleButton lightThemeToggle;
    @FXML private ToggleButton darkThemeToggle;
    
    private final Navigator navigator;
    private final BookRegistry bookRegistry;
    private final CellLoader cellLoader;

    public LibraryController(Navigator navigator, BookRegistry bookRegistry, CellLoader cellLoader) {
        this.navigator = navigator;
        this.bookRegistry = bookRegistry;
        this.cellLoader = cellLoader;
    }

    @FXML
    private void initialize() {
        loadingBookFromBookRegistry();
    }

    private void loadingBookFromBookRegistry() {
        LOG.log(Level.INFO, "Loading books into container");
        Object[] books = bookRegistry.all();
        for (Object book : books) {
            loadBook((Book) book);
        }
    }

    private void loadBook(Book book) {
        CellResult<BookCellController> result = cellLoader.loadCell(SceneType.CellType.BOOK_CELL);
        result.controller().setBook(book);
        bookContainer.getChildren().add(result.node());
    }

    @FXML
    private void onSearch() {
        System.out.println("[TODO] search");
    }

    @FXML
    private void onAddBook() {
        System.out.println("[TODO] addBook");
    }

    @FXML
    private void onSortChanged() {
        System.out.println("[TODO] sort by: " + sortCombo.getValue());
    }

    @FXML
    private void onFilterChanged() {
        System.out.println("[TODO] filter shelf: " + filterCombo.getValue());
    }

    @FXML
    private void onToggleView(ActionEvent e) {
        System.out.println("[TODO] view: " + ((ToggleButton) e.getSource()).getText());
    }

    @FXML
    private void onSelectTheme() {
        System.out.println("[TODO] onSelectTheme");
    }

    @FXML
    private void onSelectShelf(ActionEvent e) {
        System.out.println("[TODO] shelf: " + ((Button) e.getSource()).getText());
    }

    @FXML
    private void onAddShelf() {
        System.out.println("[TODO] new shelf");
    }

    @FXML
    private void onRefresh() {
        System.out.println("[TODO] refresh");
    }
}   
