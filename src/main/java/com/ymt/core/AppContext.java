package com.ymt.core;

import com.ymt.core.data.BookRegistry;
import com.ymt.core.manager.LoadManager;
import com.ymt.core.module.DataModule;

public final class AppContext {
    private final BookRegistry bookRegistry;
    private final LoadManager loadManager;

    public AppContext(DataModule dataModule) {
        this.bookRegistry = dataModule.bookRegistry();
        this.loadManager = dataModule.loadManager();
    }

    public BookRegistry bookRegistry() {
        return bookRegistry;
    }

    public LoadManager loadManager() {
        return loadManager;
    }
}
