package com.ymt.interfaces;

import com.ymt.enums.SceneType;
import com.ymt.records.CellResult;

public interface CellLoader {
    public <T> CellResult<T> loadCell(SceneType.CellType type);
}
