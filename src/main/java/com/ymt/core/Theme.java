package com.ymt.core;

import javafx.scene.Parent;

/**
 * App-wide light / dark theme state.
 *
 * The base stylesheets (library.css, reader.css) define the DARK palette.
 * Light mode is layered on top by adding theme-light.css to a view's root
 * AFTER its own stylesheet, so the light overrides win the cascade.
 *
 * The chosen mode is static so it survives scene switches; every view calls
 * {@link #apply(Parent)} right after it loads.
 */
public final class Theme {

    public enum Mode { DARK, LIGHT }

    private static final String LIGHT_CSS = Theme.class.getResource("/styles/theme-light.css").toExternalForm();

    private static final String DARK_CSS = Theme.class.getResource("/styles/theme-dark.css").toExternalForm();

    private static Mode current = Mode.LIGHT;

    private Theme() { }

    public static Mode getMode() { return current; }

    public static void setMode(Mode mode) { current = mode; }

    /** Applies the current mode to a view's root node. Safe to call repeatedly. */
    public static void apply(Parent root) {
        if (root == null)
            return;
        
        if (current == Mode.LIGHT) {
            root.getStylesheets().remove(DARK_CSS);
            root.getStylesheets().add(LIGHT_CSS);
        } else {
            root.getStylesheets().remove(LIGHT_CSS);
            root.getStylesheets().add(DARK_CSS);
        }
    }
}
