package com.ymt.records;

import com.ymt.util.Guard;

import javafx.scene.Node;

public record CellResult<T>(Node node, T controller) {
    public CellResult {
        Guard.nonEmpty(node);
        Guard.nonEmpty(controller);
    }
}
