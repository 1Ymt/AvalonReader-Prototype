package com.ymt.interfaces;

import com.ymt.enums.SceneType;
import com.ymt.records.SceneResult;

public interface Navigator {
    public void show(SceneType type);

    public void show(SceneResult<?> result);
    
    public <T> SceneResult<T> load(SceneType type);
}
