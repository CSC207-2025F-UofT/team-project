package com.terrain.use_case.save_data;

import com.terrain.entity.model.TerrainMap;

import java.io.File;
import java.io.IOException;

public interface DataAccessInterface {
    void save(TerrainMap map, File file) throws IOException;
    TerrainMap load(File file) throws IOException;
}
