module com.ymt.avalonreader {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires EpubParser;
    requires transitive javafx.graphics;
    requires java.logging;

    // Let JavaFX instantiate the Application/launcher and reflectively wire @FXML.
    exports com.ymt;
    opens com.ymt.core to javafx.fxml, javafx.graphics;
    opens com.ymt.ui.controller to javafx.fxml;
}
