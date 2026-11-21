package app;

import data_access.FileMapDataAccessObject;
import entity.DirtTileType;
import entity.TileTypeRegistry;
import entity.WaterTileType;
import interface_adapter.map.MapController;
import interface_adapter.map.MapPresenter;
import interface_adapter.map.MapViewModel;
import use_case.load_map.LoadMapInteractor;
import view.MapLayer;
import view.MapRenderer;
import view.TileSprites;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/** Assembles and prepares the map display before the application starts its game loop. */
public final class MapBuilder {
    /**
     * Loads the map through its controller and prepares a complete background on Swing's event thread.
     * Returns null after reporting a map or image failure once, so startup can stop before opening a game.
     * Resource paths and a positive tile size are application configuration; showError must be non-null.
     */
    public MapLayer build(String mapName, int tileSize, Map<Integer, String> imagePaths,
                          Consumer<String> showError) {
        TileTypeRegistry tileTypes = new TileTypeRegistry(List.of(new WaterTileType(), new DirtTileType()));
        MapViewModel viewModel = new MapViewModel();
        MapPresenter presenter = new MapPresenter(viewModel);
        LoadMapInteractor loadMap = new LoadMapInteractor(
                new FileMapDataAccessObject(tileTypes, tileSize), presenter);
        MapController controller = new MapController(loadMap);
        controller.load(mapName);
        if (!viewModel.getState().getErrorMessage().isEmpty()) {
            showError.accept(viewModel.getState().getErrorMessage());
            return null;
        }

        TileSprites sprites;
        try {
            sprites = new TileSprites(imagePaths, tileSize);
        } catch (IOException error) {
            showError.accept(error.getMessage());
            return null;
        }
        MapRenderer renderer = new MapRenderer(sprites);
        renderer.render(viewModel.getState());
        if (!renderer.getErrorMessage().isEmpty()) {
            showError.accept(renderer.getErrorMessage());
            return null;
        }
        return new MapLayer(viewModel, renderer);
    }
}
