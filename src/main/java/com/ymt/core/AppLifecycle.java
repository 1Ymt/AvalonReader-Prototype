package com.ymt.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.core.module.Module;
import com.ymt.logger.AppLogger;

public class AppLifecycle implements AutoCloseable{

    private static final Logger LOG = AppLogger.get(AppLifecycle.class);

    private final List<Module> modules = new ArrayList<>();
    private final Deque<Module> started = new ArrayDeque<>();

    public AppLifecycle() {}

    
    public void addModule(Module m) {
        modules.add(m);
        LOG.log(Level.INFO, "Adding module: " + m.getClass().getSimpleName());
    }

    public void start() {
        LOG.log(Level.INFO, "Starting modules");
        try {
            for (Module m : modules) {
                m.start();
                started.push(m);
            }
        } catch (Exception e) {
            close(); // stop whatever already started
            LOG.log(Level.SEVERE, "Startup failed", e);
        }
    }


    @Override
    public void close() {
        LOG.log(Level.INFO, "Closing modules");
        while (!started.isEmpty()) {
            try {
                started.pop().close();
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Closing failed", e);
            }
        }
    }
}
