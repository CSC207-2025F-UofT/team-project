package com.terrain.interface_adapter.terrain_cleanup;

import com.terrain.use_case.terrain_cleanup.TerrainCleanupOutputData;
import com.terrain.use_case.terrain_cleanup.TerrainCleanupOutputDataBoundary;

public class TerrainCleanupPresenter implements TerrainCleanupOutputDataBoundary{

    @Override
    public void prepareSuccessView(TerrainCleanupOutputData outputData) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO
    } 

    @Override
    public void prepareFailView(String errorMessage) {
        throw new UnsupportedOperationException("Not supported yet."); // TODO
    }

}
