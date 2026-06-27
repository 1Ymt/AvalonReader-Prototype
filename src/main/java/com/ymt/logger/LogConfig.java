package com.ymt.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogConfig {
    public static void init() {
        Logger root = Logger.getLogger("");
        for (var h : root.getHandlers()) root.removeHandler(h);
        root.setLevel(Level.ALL);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new AppFormatter());
        consoleHandler.setLevel(Level.INFO);
        root.addHandler(consoleHandler);
    }
}
