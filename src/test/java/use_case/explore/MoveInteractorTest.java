package use_case.explore;

import data_access.InMemoryExploreDataAccessObject;
import entity.Direction;
import entity.Dungeon;
import entity.Inventory;
import entity.Item;
import entity.ItemType;
import entity.Monster;
import entity.Room;
import entity.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.explore.move.MoveInputData;
import use_case.explore.move.MoveInteractor;
import use_case.explore.move.MoveOutputBoundary;
import use_case.explore.move.MoveOutputData;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for MoveInteractor (Use Case 2, steps 1-4 and extensions 2a, 4a).
 *
 * <p>Each test builds a tiny dungeon and passes in an anonymous presenter that
 * checks the output. If the interactor calls the wrong presenter method,
 * fail(...) makes the test fail.</p>
 */
class MoveInteractorTest {
    private Room start;
    private Room north;
    private Room monsterRoom;
    private Dungeon dungeon;
    private InMemoryExploreDataAccessObject dataAccess;

    @BeforeEach
    void setUp() {
        start = new Room("Start", "The first room.");
        north = new Room("North Room", "A room to the north.");
        monsterRoom = new Room("Lair", "Something lives here.");
        start.connect(Direction.NORTH, north);
        start.connect(Direction.EAST, monsterRoom);
        north.addItem(new Item("Dagger", "Sharp.", ItemType.WEAPON, 4));
        monsterRoom.setMonster(new Monster("Goblin", "goblin", 7, 15, new Stats(8, 14, 10, 10, 8, 8)));

        dungeon = new Dungeon(1, Arrays.asList(start, north, monsterRoom), start);
        dataAccess = new InMemoryExploreDataAccessObject(dungeon, new Inventory(3));
    }

    @Test
    void moveToConnectedRoomShowsRoom() {
        MoveOutputBoundary presenter = new MoveOutputBoundary() {
            @Override
            public void prepareSuccessView(MoveOutputData data) {
                assertEquals("North Room", data.getRoomName());
                assertEquals("A room to the north.", data.getRoomDescription());
                assertEquals(1, data.getRoomItems().size());
                assertEquals("Dagger", data.getRoomItems().get(0).getName());
                assertEquals(Arrays.asList("SOUTH"), data.getExits());
                assertEquals(1, data.getFloorNumber());
                assertNull(data.getMonsterName());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected fail view: " + errorMessage);
            }

            @Override
            public void prepareCombatView(MoveOutputData data) {
                fail("Unexpected combat view");
            }
        };

        new MoveInteractor(dataAccess, presenter).execute(new MoveInputData(Direction.NORTH));
        assertSame(north, dungeon.getCurrentRoom());
    }

    @Test
    void moveWithNoExitStaysInRoom() {
        final boolean[] failed = {false};
        MoveOutputBoundary presenter = new MoveOutputBoundary() {
            @Override
            public void prepareSuccessView(MoveOutputData data) {
                fail("Unexpected success view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertTrue(errorMessage.contains("west"));
                failed[0] = true;
            }

            @Override
            public void prepareCombatView(MoveOutputData data) {
                fail("Unexpected combat view");
            }
        };

        new MoveInteractor(dataAccess, presenter).execute(new MoveInputData(Direction.WEST));
        assertTrue(failed[0]);
        assertSame(start, dungeon.getCurrentRoom());
    }

    @Test
    void moveIntoMonsterRoomStartsCombat() {
        final boolean[] combat = {false};
        MoveOutputBoundary presenter = new MoveOutputBoundary() {
            @Override
            public void prepareSuccessView(MoveOutputData data) {
                fail("Unexpected success view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected fail view");
            }

            @Override
            public void prepareCombatView(MoveOutputData data) {
                assertEquals("Goblin", data.getMonsterName());
                combat[0] = true;
            }
        };

        new MoveInteractor(dataAccess, presenter).execute(new MoveInputData(Direction.EAST));
        assertTrue(combat[0]);
        assertSame(monsterRoom, dungeon.getCurrentRoom());
    }

    @Test
    void deadMonsterDoesNotStartCombat() {
        monsterRoom.getMonster().takeDamage(100);
        final boolean[] shown = {false};
        MoveOutputBoundary presenter = new MoveOutputBoundary() {
            @Override
            public void prepareSuccessView(MoveOutputData data) {
                shown[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected fail view");
            }

            @Override
            public void prepareCombatView(MoveOutputData data) {
                fail("Dead monsters should not start combat");
            }
        };

        new MoveInteractor(dataAccess, presenter).execute(new MoveInputData(Direction.EAST));
        assertTrue(shown[0]);
    }

    @Test
    void showCurrentRoomDoesNotMove() {
        final boolean[] shown = {false};
        MoveOutputBoundary presenter = new MoveOutputBoundary() {
            @Override
            public void prepareSuccessView(MoveOutputData data) {
                assertEquals("Start", data.getRoomName());
                assertEquals(3, data.getInventoryCapacity());
                assertTrue(data.getExits().contains("NORTH"));
                assertTrue(data.getExits().contains("EAST"));
                shown[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected fail view");
            }

            @Override
            public void prepareCombatView(MoveOutputData data) {
                fail("Unexpected combat view");
            }
        };

        new MoveInteractor(dataAccess, presenter).showCurrentRoom();
        assertTrue(shown[0]);
        assertSame(start, dungeon.getCurrentRoom());
    }

    @Test
    void showCurrentRoomWithMonsterStartsCombat() {
        dungeon.setCurrentRoom(monsterRoom);
        final boolean[] combat = {false};
        MoveOutputBoundary presenter = new MoveOutputBoundary() {
            @Override
            public void prepareSuccessView(MoveOutputData data) {
                fail("Unexpected success view");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected fail view");
            }

            @Override
            public void prepareCombatView(MoveOutputData data) {
                combat[0] = true;
            }
        };

        new MoveInteractor(dataAccess, presenter).showCurrentRoom();
        assertTrue(combat[0]);
    }
}
