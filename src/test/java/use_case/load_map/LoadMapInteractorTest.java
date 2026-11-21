package use_case.load_map;

import entity.DirtTileType;
import entity.TileMap;
import entity.TileType;
import entity.WaterTileType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Exercises the use case with an in-memory map source and a recording output boundary. */
class LoadMapInteractorTest {
    private final InMemoryMapDataAccess dataAccess = new InMemoryMapDataAccess();
    private final RecordingPresenter presenter = new RecordingPresenter();
    private final LoadMapInputBoundary interactor = new LoadMapInteractor(dataAccess, presenter);

    @Test
    void presentsTheCompleteMapWithTheOriginalRowAndColumnOrder() {
        interactor.execute(new LoadMapInputData("level1.txt"));

        assertEquals("level1.txt", dataAccess.requestedName);
        assertEquals(1, dataAccess.loadCount);
        assertEquals(1, presenter.successCount);
        assertEquals(0, presenter.failureCount);
        assertNull(presenter.error);
        assertEquals(32, presenter.output.getTileSize());
        int[][] ids = presenter.output.getTileIds();
        assertEquals(2, ids.length);
        assertArrayEquals(new int[] {1, 0, 1}, ids[0]);
        assertArrayEquals(new int[] {0, 1, 0}, ids[1]);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void presentsMissingNamesWithoutCallingDataAccess(String name) {
        interactor.execute(new LoadMapInputData(name));

        assertEquals(0, dataAccess.loadCount);
        assertEquals(0, presenter.successCount);
        assertEquals(1, presenter.failureCount);
        assertEquals("Map name must not be blank.", presenter.error);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Map resource not found: maps/missing.txt",
            "Map 'empty.txt' contains no tiles.",
            "Could not read map 'unreadable.txt'.",
            "Map 'uneven.txt', line 2, column 3: Expected 3 tiles but found 2.",
            "Map 'unknown.txt', line 4, column 2: Unknown tile ID: 9.",
            "Map 'invalid.txt', line 1, column 2: Invalid integer tile ID: 'dirt'.",
            "Invalid map 'oversized.txt': Map pixel dimensions exceed the supported range."
    })
    void presentsEachLoadingFailureWithoutExposingAnException(String message) {
        dataAccess.failure = new MapLoadingException(message);

        interactor.execute(new LoadMapInputData("requested-map.txt"));

        assertEquals(1, dataAccess.loadCount);
        assertEquals(0, presenter.successCount);
        assertNull(presenter.output);
        assertEquals(1, presenter.failureCount);
        assertEquals(message, presenter.error);
    }

    @Test
    void preservesAdditionalTerrainIdsAndConfiguredTileSize() {
        TileType additionalTerrain = new TileType() {
            @Override
            public int getId() {
                return 42;
            }

            @Override
            public boolean isWalkable() {
                return true;
            }
        };
        dataAccess.map = new TileMap(new TileType[][] {{additionalTerrain}}, 16);

        interactor.execute(new LoadMapInputData("additional-terrain.txt"));

        assertArrayEquals(new int[] {42}, presenter.output.getTileIds()[0]);
        assertEquals(16, presenter.output.getTileSize());
    }

    /** Supplies a complete map or a configured failure without accessing files. */
    private static final class InMemoryMapDataAccess implements MapDataAccessInterface {
        private TileMap map = new TileMap(new TileType[][] {
                {new DirtTileType(), new WaterTileType(), new DirtTileType()},
                {new WaterTileType(), new DirtTileType(), new WaterTileType()}
        }, 32);
        private MapLoadingException failure;
        private String requestedName;
        private int loadCount;

        @Override
        public TileMap load(String mapName) throws MapLoadingException {
            requestedName = mapName;
            loadCount++;
            if (failure != null) {
                throw failure;
            }
            return map;
        }
    }

    /** Records which output path the interactor selected. */
    private static final class RecordingPresenter implements LoadMapOutputBoundary {
        private LoadMapOutputData output;
        private String error;
        private int successCount;
        private int failureCount;

        @Override
        public void prepareSuccessView(LoadMapOutputData outputData) {
            output = outputData;
            successCount++;
        }

        @Override
        public void prepareFailView(String message) {
            error = message;
            failureCount++;
        }
    }
}
