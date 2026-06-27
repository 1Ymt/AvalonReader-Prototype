package com.ymt.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ymt.logger.AppLogger;

public final class Guard {

    private static final Logger LOG = AppLogger.get(Guard.class);

    private Guard() {}

    public static <T> T nonEmpty(T obj) {
        if (obj == null) {
            LOG.log(Level.SEVERE, "Null check failed");
        }
        return obj;
    }

    public static String orEmpty(String s) {
        if (s == null)
            return "";
        return s;
    }
}
