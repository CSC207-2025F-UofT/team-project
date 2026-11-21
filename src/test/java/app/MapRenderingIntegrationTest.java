package app;

import data_access.FileMapDataAccessObject;
import entity.DirtTileType;
import entity.TileTypeRegistry;
import entity.WaterTileType;
import interface_adapter.map.MapController;
import interface_adapter.map.MapPresenter;
import interface_adapter.map.MapState;
import interface_adapter.map.MapViewModel;
import org.junit.jupiter.api.Test;
import use_case.load_map.LoadMapInteractor;
import view.MapRenderer;
import view.TileSprites;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Wires the production resources and map flow into the renderer without opening a window. */
class MapRenderingIntegrationTest {
    @Test
    void rendersTheRealMapAndPreservesItWhenTheNextLoadFails() throws Exception {
        TileTypeRegistry registry = new TileTypeRegistry(List.of(new WaterTileType(), new DirtTileType()));
        FileMapDataAccessObject dataAccess = new FileMapDataAccessObject(registry, 32);
        MapViewModel viewModel = new MapViewModel();
        MapController controller = new MapController(
                new LoadMapInteractor(dataAccess, new MapPresenter(viewModel)));
        TileSprites sprites = new TileSprites(Map.of(0, "/tiles/water.png", 1, "/tiles/dirt.png"), 32);
        MapRenderer renderer = new MapRenderer(sprites);

        controller.load("level1.txt");
        BufferedImage image = renderer.render(viewModel.getState());

        assertNotNull(image);
        assertEquals("", renderer.getErrorMessage());
        assertEquals(960, image.getWidth());
        assertEquals(640, image.getHeight());
        MapState state = viewModel.getState();
        Map<Integer, Color> colors = Map.of(0, Color.BLUE, 1, Color.GREEN);
        for (int row = 0; row < state.getRowCount(); row++) {
            for (int col = 0; col < state.getColumnCount(); col++) {
                assertEquals(colors.get(state.getTileId(col, row)).getRGB(), image.getRGB(col * 32 + 16, row * 32 + 16));
            }
        }

        controller.load("missing-map.txt");
        assertSame(image, renderer.render(viewModel.getState()));
        assertTrue(renderer.getErrorMessage().contains("missing-map.txt"));
        controller.load("level1.txt");
        assertSame(image, renderer.render(viewModel.getState()));
        assertEquals("", renderer.getErrorMessage());
    }
}
