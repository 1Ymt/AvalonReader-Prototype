package com.ymt.core.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.core.AppContext;
import com.ymt.interfaces.CellLoader;
import com.ymt.interfaces.Navigator;
import com.ymt.logger.AppLogger;
import com.ymt.util.Guard;
import com.ymt.ui.controller.BookCellController;
import com.ymt.ui.controller.ChapterController;
import com.ymt.ui.controller.LibraryController;

import javafx.util.Callback;

public class ControllerFactory {
    private static final Logger LOG = AppLogger.get(ControllerFactory.class);

    private final AppContext context;
    private Navigator navigator;
    private CellLoader cellLoader;

    public ControllerFactory(AppContext context) {
        this.context = context;
    }

    public void setNavigator(Navigator nav) {
        this.navigator = nav;
    }

    public void setCellLoader(CellLoader cellLoader) {
        this.cellLoader = cellLoader;
    }

    public Callback<Class<?>, Object> getFactory() {
        Callback<Class<?>, Object> factory = new Callback<Class<?>,Object>() {

            @Override
            public Object call(Class<?> param) {
                Guard.nonEmpty(navigator);
                Guard.nonEmpty(cellLoader);
                if (param == LibraryController.class) {
                    return new LibraryController(navigator, context.bookRegistry(), cellLoader);
                } else if (param == BookCellController.class) {
                    return new BookCellController(navigator);
                } else if (param == ChapterController.class) {
                    return new ChapterController(navigator);
                }

                try {
                    return param.getDeclaredConstructor().newInstance();
                }
                catch (Exception e) {
                    LOG.log(Level.SEVERE, "Constructor cannot be instanced", e);
                    return null;
                    
                }
            }
            
        };
        return factory;
    }
    
}
