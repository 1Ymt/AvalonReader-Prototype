package com.ymt.core.module;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.core.data.BookRegistry;
import com.ymt.core.manager.LoadManager;
import com.ymt.logger.AppLogger;

public class DataModule implements Module {

    private static final Logger LOG = AppLogger.get(DataModule.class);
    private BookRegistry bookRegistry;
    private LoadManager loadManager;

    @Override
    public void start() throws Exception {
        LOG.log(Level.INFO, "Starting");
        bookRegistry = new BookRegistry();
        loadManager = new LoadManager(bookRegistry);
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "Closing");
    }

    public BookRegistry bookRegistry() {
        return this.bookRegistry;
    }

    public LoadManager loadManager() {
        return this.loadManager;
    }
}
