package use_case.load_map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/** Checks that mutable arrays cannot change a published use-case result. */
class LoadMapOutputDataTest {
    @Test
    void copiesBothTheOuterArrayAndItsRowsOnConstruction() {
        int[][] supplied = {{1, 0}, {0, 1}};
        LoadMapOutputData output = new LoadMapOutputData(supplied, 32);

        supplied[0][0] = 99;
        supplied[1] = new int[] {99};

        assertArrayEquals(new int[] {1, 0}, output.getTileIds()[0]);
        assertArrayEquals(new int[] {0, 1}, output.getTileIds()[1]);
    }

    @Test
    void returnsIndependentCopiesOfTheGrid() {
        LoadMapOutputData output = new LoadMapOutputData(new int[][] {{1, 0}, {0, 1}}, 32);
        int[][] retrieved = output.getTileIds();

        retrieved[0][0] = 99;
        retrieved[1] = new int[] {99};

        assertArrayEquals(new int[] {1, 0}, output.getTileIds()[0]);
        assertArrayEquals(new int[] {0, 1}, output.getTileIds()[1]);
    }
}
