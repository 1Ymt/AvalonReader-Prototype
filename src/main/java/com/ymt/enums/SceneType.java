package com.ymt.enums;

public enum SceneType {
    LIBRARY("library"),
    CHAPTER("chapter");

    public final String location;

    private SceneType(String location) {
        this.location = "/fxml/" + location + ".fxml";
    }

    public enum CellType {
        BOOK_CELL("book-cell");

        public final String location;

        private CellType(String location) {
            this.location = "/fxml/" + location + ".fxml";
        }
    }
}


