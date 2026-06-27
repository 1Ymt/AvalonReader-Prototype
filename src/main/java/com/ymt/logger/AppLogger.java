package com.ymt.logger;

import java.util.logging.Logger;

public final class AppLogger {
    public static Logger get(Class<?> cls) {
        return Logger.getLogger(cls.getName());
    }
}
