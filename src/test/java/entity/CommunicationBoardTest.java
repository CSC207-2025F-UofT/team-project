package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommunicationBoardTest {

    private Pictogram water;
    private Pictogram apple;
    private CommunicationBoard board;

    @BeforeEach
    void setUp() {
        water = new Pictogram(1, "water", "water.png");
        apple = new Pictogram(2, "apple", "apple.png");
        board = new CommunicationBoard();
    }

    @Test
    void newBoardIsEmptyWithDefaultCapacity() {
        assertTrue(board.isEmpty());
        assertEquals(0, board.size());
        assertEquals(6, board.getCapacity());
    }

    @Test
    void addPictogramKeepsOrder() {
        board.addPictogram(water);
        board.addPictogram(apple);

        assertEquals(List.of(water, apple), board.getPictograms());
    }

    @Test
    void cannotAddSamePictogramTwice() {
        board.addPictogram(water);

        assertThrows(IllegalArgumentException.class,
                () -> board.addPictogram(water));
        assertEquals(1, board.size());
    }

    @Test
    void cannotAddWhenFull() {
        CommunicationBoard small = new CommunicationBoard(1);
        small.addPictogram(water);

        assertTrue(small.isFull());
        assertThrows(IllegalStateException.class,
                () -> small.addPictogram(apple));
    }

    @Test
    void removePictogramRemovesIt() {
        board.addPictogram(water);
        board.addPictogram(apple);

        board.removePictogram(water);

        assertEquals(List.of(apple), board.getPictograms());
        assertFalse(board.contains(water));
    }

    @Test
    void cannotRemovePictogramNotOnBoard() {
        assertThrows(IllegalArgumentException.class,
                () -> board.removePictogram(water));
    }

    @Test
    void returnedListCannotBeModified() {
        board.addPictogram(water);

        assertThrows(UnsupportedOperationException.class,
                () -> board.getPictograms().clear());
        assertEquals(1, board.size());
    }

    @Test
    void capacityMustBePositive() {
        assertThrows(IllegalArgumentException.class,
                () -> new CommunicationBoard(0));
    }
}