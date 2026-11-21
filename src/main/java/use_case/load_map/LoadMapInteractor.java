package use_case.load_map;

import entity.TileMap;

/** Loads a terrain map through its data-access boundary and presents the result. */
public final class LoadMapInteractor implements LoadMapInputBoundary {
    private final MapDataAccessInterface mapDataAccess;
    private final LoadMapOutputBoundary presenter;

    public LoadMapInteractor(MapDataAccessInterface mapDataAccess, LoadMapOutputBoundary presenter) {
        this.mapDataAccess = mapDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadMapInputData inputData) {
        String mapName = inputData.getMapName();
        if (mapName == null || mapName.isBlank()) {
            presenter.prepareFailView("Map name must not be blank.");
            return;
        }

        TileMap map;
        try {
            map = mapDataAccess.load(mapName);
        } catch (MapLoadingException error) {
            presenter.prepareFailView(error.getMessage());
            return;
        }

        int[][] tileIds = new int[map.getRowCount()][map.getColumnCount()];
        for (int row = 0; row < map.getRowCount(); row++) {
            for (int col = 0; col < map.getColumnCount(); col++) {
                tileIds[row][col] = map.getTile(col, row).getId();
            }
        }
        presenter.prepareSuccessView(new LoadMapOutputData(tileIds, map.getTileSize()));
    }
}
