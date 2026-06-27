package com.ymt.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.core.factory.ControllerFactory;
import com.ymt.core.manager.SceneManager;
import com.ymt.core.module.DataModule;
import com.ymt.enums.SceneType;
import com.ymt.logger.AppLogger;
import com.ymt.logger.LogConfig;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX entry point.
 *
 * <p>Will load the main view ({@code /fxml/main-view.fxml}) and show the primary
 * stage. Wiring to be filled in.
 */
public class AvalonReader extends Application {;
    private static final Logger LOG = AppLogger.get(AvalonReader.class);

    private AppLifecycle lifecycle;
    private SceneManager sceneManager;
    

    @Override
    public void start(Stage stage) {
        LogConfig.init();

        DataModule dataModule = new DataModule();

        this.lifecycle = new AppLifecycle();
        this.lifecycle.addModule(dataModule);
        this.lifecycle.start();

        AppContext context = new AppContext(dataModule);

        ControllerFactory factory = new ControllerFactory(context);
        this.sceneManager = new SceneManager(stage, factory, context);
        factory.setNavigator(sceneManager);
        factory.setCellLoader(sceneManager);

        context.loadManager().load();
        
        LOG.log(Level.INFO, "Setup finished");

        sceneManager.show(SceneType.LIBRARY);
        stage.setOnCloseRequest(e -> lifecycle.close());
        stage.show();

        LOG.log(Level.INFO, "UI setup finished");
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
}
