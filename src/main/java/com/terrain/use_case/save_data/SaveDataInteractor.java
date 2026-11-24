package com.terrain.use_case.save_data;

import com.terrain.entity.model.TerrainMap;

import java.io.File;
import java.io.IOException;

public class SaveDataInteractor implements SaveDataInputBoundary{
    private final DataAccessInterface dataAccess;

    public SaveDataInteractor(DataAccessInterface dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public void save(TerrainMap map, File file) {
        try {
            dataAccess.save(map, file);
        } catch (IOException e) {
            System.out.println("Error while saving TerrainMap: " + e.getMessage());
        }
    }

    @Override
    public TerrainMap load(File file) {
        try {
            return dataAccess.load(file);
        } catch (IOException e) {
            System.out.println("Error while saving TerrainMap: " + e.getMessage());
            return null;
        }
    }
}
