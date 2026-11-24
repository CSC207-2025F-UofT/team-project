package use_case.explore.move;

import entity.Direction;
import entity.Dungeon;
import entity.Inventory;
import entity.Room;
import use_case.explore.ExploreDataAccessInterface;
import use_case.explore.ItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Use Case 2, steps 1-4: move the character into a neighbouring room.
 *
 * <p>This is where the rules for moving live. It doesn't know about Swing or
 * how data is stored: it talks to the outside world only through the
 * ExploreDataAccessInterface (data in) and the MoveOutputBoundary (results out).</p>
 */
public class MoveInteractor implements MoveInputBoundary {
    /** Where the dungeon and inventory come from. */
    private final ExploreDataAccessInterface dataAccess;
    /** Who to report results to. */
    private final MoveOutputBoundary presenter;

    /**
     * Creates the interactor. Both dependencies are interfaces, so tests can pass
     * in fakes.
     *
     * @param dataAccess where to get the game state
     * @param presenter  where to send the results
     */
    public MoveInteractor(ExploreDataAccessInterface dataAccess, MoveOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    /**
     * Steps 1-4: move in the chosen direction and present the new room.
     *
     * @param inputData the chosen direction
     */
    @Override
    public void execute(MoveInputData inputData) {
        Dungeon dungeon = dataAccess.getDungeon();
        // Step 1: the direction the player chose.
        Direction direction = inputData.getDirection();

        // Step 2: find the room connected in that direction.
        Room next = dungeon.getCurrentRoom().getExit(direction);
        if (next == null) {
            // Extension 2a: no exit, so the character stays where they are.
            presenter.prepareFailView("There is no exit to the "
                    + direction.displayName().toLowerCase() + ". You stay where you are.");
            return;
        }

        // Step 3: move the character, then let the DAO save the new position.
        dungeon.moveTo(direction);
        dataAccess.saveState(dungeon, dataAccess.getInventory());

        // Collect everything the screen needs to show about the new room.
        MoveOutputData outputData = buildOutput(dungeon, dataAccess.getInventory());
        if (next.monsterPresent()) {
            // Extension 4a: there is a monster, so hand over to Combat (Use Case 3).
            presenter.prepareCombatView(outputData);
        }
        else {
            // Step 4: present the room's description, items and exits.
            presenter.prepareSuccessView(outputData);
        }
    }

    /**
     * Presents the current room without moving. Used when the explore screen
     * first opens and after loading a saved game.
     */
    @Override
    public void showCurrentRoom() {
        Dungeon dungeon = dataAccess.getDungeon();
        MoveOutputData outputData = buildOutput(dungeon, dataAccess.getInventory());
        // A saved game could put the player in a room with a monster, so check here too.
        if (dungeon.getCurrentRoom().monsterPresent()) {
            presenter.prepareCombatView(outputData);
        }
        else {
            presenter.prepareSuccessView(outputData);
        }
    }

    /**
     * Copies what the screen needs from the entities into a MoveOutputData,
     * so no entity objects leave the use case layer.
     *
     * @param dungeon   the current floor
     * @param inventory the player's inventory
     * @return data describing the current room and the inventory
     */
    private MoveOutputData buildOutput(Dungeon dungeon, Inventory inventory) {
        Room room = dungeon.getCurrentRoom();

        // Turn each Direction into its name ("NORTH"), so outer layers only see Strings.
        List<String> exits = new ArrayList<>();
        for (Direction d : room.getExitDirections()) {
            exits.add(d.name());
        }

        // Only pass a monster name if a living monster is here.
        String monsterName = room.monsterPresent() ? room.getMonster().getName() : null;

        return new MoveOutputData(
                dungeon.getFloorNumber(),
                room.getName(),
                room.getDescription(),
                ItemData.fromAll(room.getItems()),
                exits,
                ItemData.fromAll(inventory.getItems()),
                inventory.getMaxCapacity(),
                monsterName);
    }
}
