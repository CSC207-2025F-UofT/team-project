package use_case;

import entity.Game;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import support.GameFixtures;
import static org.junit.jupiter.api.Assertions.*;

class InteractInteractorTest {
    @Test
    void drawerChangesDescriptionInventoryRequirementsAndChoicesInOneFreshOutput() {
        Game game = GameFixtures.drawerGame();
        List<GameOutputData> outputs = new ArrayList<>();
        InteractInteractor interactor = new InteractInteractor(new GameContext(game),
                new GameOutputAssembler(), outputs::add);

        interactor.execute("drawer", "open", Optional.empty());

        assertEquals(1, outputs.size());
        GameOutputData data = outputs.get(0);
        assertEquals("Desk drawer\nThe drawer is open. Its card slot is empty.",
                data.objectDescriptions().get("drawer"));
        assertEquals("card", data.inventory().get(0).collectableId());
        assertTrue(data.message().contains("Obtained:"));
        assertTrue(data.message().contains("Laboratory access card"));
        assertTrue(data.locks().stream().filter(lock -> lock.lockId().equals("door"))
                .findFirst().orElseThrow().canUnlock());
        assertFalse(data.actions().stream().anyMatch(action -> action.actionId().equals("open")));
        assertFalse(data.canMove());
    }

    @Test
    void everyFailureAndCompletedAttemptStillPresentsFreshDataWithoutAcquisitionReplay() {
        GameContext context = new GameContext(GameFixtures.drawerGame());
        List<GameOutputData> outputs = new ArrayList<>();
        InteractInteractor interactor = new InteractInteractor(context, new GameOutputAssembler(), outputs::add);
        interactor.execute("missing", "open", Optional.empty());
        interactor.execute("drawer", "open", Optional.empty());
        interactor.execute("drawer", "open", Optional.of("unheld-tool"));
        interactor.execute("drawer", "read", Optional.empty());

        assertEquals(4, outputs.size());
        assertTrue(outputs.get(0).inventory().isEmpty());
        assertFalse(outputs.get(0).message().isBlank());
        assertEquals(1, outputs.get(2).inventory().size());
        assertFalse(outputs.get(2).message().contains("Obtained:"));
        assertFalse(outputs.get(3).message().contains("Obtained:"));
        assertNotSame(outputs.get(1), outputs.get(2));
        assertNotSame(outputs.get(2), outputs.get(3));
    }

    @Test
    void commandsResolveTheReplacedSessionAndNoGameErrorsArePresented() {
        GameContext context = new GameContext();
        List<GameOutputData> outputs = new ArrayList<>();
        InteractInteractor interactor = new InteractInteractor(context, new GameOutputAssembler(), outputs::add);
        interactor.execute("drawer", "open", Optional.empty());
        assertTrue(outputs.get(0).roomId().isEmpty());
        assertFalse(outputs.get(0).canMove());
        assertFalse(outputs.get(0).finalRoom());

        Game original = GameFixtures.drawerGame();
        context.replace(original);
        interactor.execute("drawer", "open", Optional.empty());
        Game loaded = GameFixtures.drawerGame();
        context.replace(loaded);
        interactor.execute("drawer", "open", Optional.empty());
        assertEquals(1, original.inventoryItems().size());
        assertEquals(1, loaded.inventoryItems().size());
        assertTrue(outputs.get(2).message().contains("Obtained:"));
    }

    @Test
    void malformedInputStillProducesFeedbackAndPreservesTheWorld() {
        Game game = GameFixtures.drawerGame();
        List<GameOutputData> outputs = new ArrayList<>();
        InteractInteractor interactor = new InteractInteractor(new GameContext(game),
                new GameOutputAssembler(), outputs::add);
        interactor.execute(null, "open", Optional.empty());
        interactor.execute("drawer", null, Optional.empty());
        interactor.execute("drawer", "open", null);
        assertEquals(3, outputs.size());
        assertTrue(outputs.stream().allMatch(data -> !data.message().isBlank()));
        assertTrue(game.inventoryItems().isEmpty());
    }
}
