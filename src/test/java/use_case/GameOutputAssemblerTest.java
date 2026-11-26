package use_case;

import entity.Game;
import entity.Action;
import entity.Collectable;
import entity.Interactable;
import entity.ItemLock;
import entity.Room;
import entity.TriviaLock;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import support.GameFixtures;
import static org.junit.jupiter.api.Assertions.*;

class GameOutputAssemblerTest {
    private final GameOutputAssembler assembler = new GameOutputAssembler();

    @Test
    void questionOutputHasOnlyPromptAndChoicesAndReadsNeverGrantItems() {
        Game game = GameFixtures.drawerGame();
        GameOutputData first = assembler.fromGame(game, "Question opened.", Optional.of("question"));
        GameOutputData second = assembler.fromGame(game, "Refreshed.", Optional.of("question"));
        assertEquals("What three-digit code is on the desk?", first.activeTrivia().orElseThrow().prompt());
        assertEquals(List.of(), first.activeTrivia().orElseThrow().choices());
        assertFalse(first.narrative().contains("007"));
        assertTrue(first.inventory().isEmpty());
        assertTrue(second.inventory().isEmpty());
        assertEquals("Refreshed.", second.message());
        assertTrue(assembler.fromGame(game, "", Optional.of("foreign-lock")).activeTrivia().isEmpty());
    }

    @Test
    void completingTheSecondGateImmediatelyUpdatesMovementAndEscapeOutput() {
        Game game = GameFixtures.drawerGame();
        game.submitAnswer("question", "007");
        assertFalse(assembler.fromGame(game, "", Optional.empty()).canMove());
        game.interact("drawer", "open", Optional.empty());
        game.useItems("door");
        GameOutputData ready = assembler.fromGame(game, "Door opened.", Optional.empty());
        assertTrue(ready.canMove());
        assertTrue(ready.finalRoom());
        game.move();
        GameOutputData escaped = assembler.fromGame(game, "Escaped.", Optional.empty());
        assertTrue(escaped.escaped());
        assertFalse(escaped.canMove());
        assertTrue(escaped.actions().isEmpty());
    }

    @Test
    void outputCollectionsCannotMutateTheDomainAndMenuFlagsAreFalse() {
        Game game = GameFixtures.drawerGame();
        GameOutputData data = assembler.fromGame(game, "", Optional.empty());
        assertThrows(UnsupportedOperationException.class, () -> data.objectDescriptions().clear());
        assertThrows(UnsupportedOperationException.class, () -> data.actions().clear());
        assertThrows(UnsupportedOperationException.class, () -> data.locks().clear());
        assertThrows(UnsupportedOperationException.class, () -> data.locks().get(0).requiredItems().clear());
        GameOutputData menu = assembler.fromStories(List.of(new StorySummary("story", "Title", "Background")), "");
        assertFalse(menu.canMove());
        assertFalse(menu.finalRoom());
        assertTrue(menu.roomId().isEmpty());
        assertEquals("story", menu.stories().get(0).templateId());
    }

    @Test
    void identicallyNamedActionsDisplayTheirObjectNamesAndKeepSeparateRoutingIds() {
        Action open = new Action("open", "Open", Optional.empty(), Set.of(), "Opened.",
                false, List.of(), List.of());
        Interactable drawer = new Interactable("drawer", "Desk drawer", Map.of("closed", "A drawer."),
                Map.of("mode", "closed"), Map.of("mode", Set.of("closed")), Map.of("open", open));
        Interactable cabinet = new Interactable("cabinet", "Wall cabinet", Map.of("closed", "A cabinet."),
                Map.of("mode", "closed"), Map.of("mode", Set.of("closed")), Map.of("open", open));
        Room room = new Room("room", "Room", "Look around.",
                new TriviaLock("question", "Question", "Passed.", Optional.empty()),
                new ItemLock("door", "Door", "Unlocked.", Set.of("card")), Optional.empty(),
                Map.of("drawer", drawer, "cabinet", cabinet),
                Map.of("card", new Collectable("card", "Card", "A card.", false)));
        Game game = new Game("Story", "An abandoned room.", Map.of("room", room), "room");

        GameOutputData data = assembler.fromGame(game, "", Optional.empty());

        assertEquals(2, data.actions().size());
        assertEquals("Desk drawer — Open", data.actions().stream()
                .filter(action -> action.objectId().equals("drawer")).findFirst().orElseThrow().label());
        assertEquals("Wall cabinet — Open", data.actions().stream()
                .filter(action -> action.objectId().equals("cabinet")).findFirst().orElseThrow().label());
        assertTrue(data.actions().stream().allMatch(action -> action.actionId().equals("open")));
    }
}
