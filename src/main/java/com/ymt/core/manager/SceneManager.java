package com.ymt.core.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.enums.SceneType;
import com.ymt.enums.SceneType.CellType;
import com.ymt.core.AppContext;
import com.ymt.core.factory.ControllerFactory;
import com.ymt.interfaces.CellLoader;
import com.ymt.interfaces.Navigator;
import com.ymt.logger.AppLogger;
import com.ymt.records.CellResult;
import com.ymt.records.SceneResult;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SceneManager implements Navigator, CellLoader {
    private static final Logger LOG = AppLogger.get(SceneManager.class);

    private Stage stage;
    private ControllerFactory factory;
    private AppContext context;

    private final Map<SceneType, Parent> scenes = new HashMap<>();

    public SceneManager(Stage stage, ControllerFactory factory, AppContext context) {
        this.stage = stage;
        this.factory = factory;
        this.context = context;
        setRoot();
    }

    @Override
    public <T> CellResult<T> loadCell(CellType type) {
        LOG.log(Level.INFO, "Loading Cell: " + type.toString());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.location));
            loader.setControllerFactory(factory.getFactory());
            Node node = loader.load();
            T controller = loader.getController();
            
            return new CellResult<>(node, controller);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Fail to load FXMLLoader", e);
            return null;
        }
    }

    @Override
    public void show(SceneType type) {
        if (!scenes.containsKey(type)) {
            try {
                Parent root = loadFXML(type);
                scenes.put(type, root);
            } catch (IOException e) {
                LOG.log(Level.SEVERE, "Load " + type, e);
            }
        }

        stage.getScene().setRoot(scenes.get(type));
        LOG.log(Level.INFO, "Showing Scene: " + type.toString());
    }

    //TODO: Maybe store chapter scenes into scenes Array 
    @Override
    public void show(SceneResult<?> result) {
        stage.getScene().setRoot(result.root());
        LOG.log(Level.INFO, "Showing Scene: " + result.type().toString());
    }

    @Override
    public <T> SceneResult<T> load(SceneType type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.location));
            loader.setControllerFactory(factory.getFactory());
            Parent root = loader.load();
            
            T controller = loader.getController();
                 
            return new SceneResult<>(root, controller, type);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Fail to load FXMLLoader", e);
            return null;
        }
        
    }

    public void removeAllAndActivate(SceneType sceneType) {
        scenes.clear();
        show(sceneType);
    }


    private void setRoot(){
        Scene scene = new Scene(new Pane());
        stage.setScene(scene);
        stage.show();
    }

    private Parent loadFXML(SceneType fxmlType) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlType.location));
        fxmlLoader.setControllerFactory(factory.getFactory());

        return fxmlLoader.load();
    }
    
}
