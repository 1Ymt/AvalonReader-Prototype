package com.ymt.records;

import com.ymt.enums.SceneType;
import com.ymt.util.Guard;

import javafx.scene.Parent;

public record SceneResult<T>(Parent root, T controller, SceneType type) {
    public SceneResult {
        Guard.nonEmpty(root);
        Guard.nonEmpty(controller);
        Guard.nonEmpty(type);
    }
}
