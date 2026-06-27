package com.ymt.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class AppFormatter extends Formatter {

    private static final String RESET  = "[0m";
    private static final String RED    = "[91m";   // SEVERE
    private static final String YELLOW = "[33m";   // WARNING
    private static final String CYAN   = "[36m";   // INFO
    private static final String BLUE   = "[34m";   // CONFIG
    private static final String GRAY   = "[90m";   // FINE and below

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());

    @Override
    public String format(LogRecord record) {
        Level level  = record.getLevel();
        String time  = TIME_FMT.format(Instant.ofEpochMilli(record.getMillis()));
        String cls   = shortName(record.getSourceClassName());
        String msg   = formatMessage(record);
        String color = ANSICode(level);
        String tag   = "[" + level.getName() + "]";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s%-10s%s %s  %-22s: %s%n", color, tag, RESET, time, cls, msg));

        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(sw));
            sb.append(GRAY).append(sw.toString().stripTrailing()).append(RESET).append(System.lineSeparator());
        }

        return sb.toString();
    }

    private static String ANSICode(Level level) {
        if (level == Level.SEVERE)  return RED;
        if (level == Level.WARNING) return YELLOW;
        if (level == Level.INFO)    return CYAN;
        if (level == Level.CONFIG)  return BLUE;
        return GRAY;
    }

    private static String shortName(String className) {
        if (className == null) return "?";
        int dot = className.lastIndexOf('.');
        return dot >= 0 ? className.substring(dot + 1) : className;
    }

}