package com.ymt;

import com.ymt.core.AvalonReader;

/**
 * Plain launcher.
 *
 * <p>Kept separate from {@link AvalonReader} (which extends {@code Application}) so the
 * jar can be started without the "JavaFX runtime components are missing" error
 * that occurs when the main class itself extends {@code Application}.
 */
public class Main {
    public static void main(String[] args) {
        AvalonReader.main(args);
    }
}
