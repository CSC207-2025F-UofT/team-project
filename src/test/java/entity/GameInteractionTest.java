package entity;

import entity.state.ObjectState;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import support.GameFixtures;
import static org.junit.jupiter.api.Assertions.*;

class GameInteractionTest {
    @Test
    void successfulCommandTransfersSameInstancesAndRecordsOneShotSnapshot() {
        Collectable card = item("card", false);
        Collectable note = item("note", false);
        Action action = action("open", Optional.empty(), Set.of("card", "note"), false,
                List.of(), List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Interactable drawer = object("drawer", Map.of("open", action));
        Room room = room("lab", Optional.empty(), Map.of("drawer", drawer), Map.of("card", card, "note", note));
        Game game = game(room);

        DomainResult result = game.interact("drawer", "open", Optional.empty());

        assertTrue(result.changed());
        assertEquals(ResultCode.SUCCESS, result.code());
        assertTrue(card.isRevealed());
        assertTrue(note.isRevealed());
        assertSame(card, game.inventoryItems().stream().filter(i -> i.id().equals("card")).findFirst().orElseThrow());
        assertSame(note, game.inventoryItems().stream().filter(i -> i.id().equals("note")).findFirst().orElseThrow());
        assertTrue(room.allCollectables().isEmpty());
        assertEquals("Open drawer", drawer.describe());
        ObjectState snapshot = drawer.snapshot();
        assertEquals("open", snapshot.values().get("mode"));
        assertEquals(Set.of("open"), snapshot.completedActionIds());
        assertTrue(result.resultText().contains("Obtained:"));
    }

    @Test
    void preflightsEveryAppearanceBeforeAnyStateCompletionOrTransferChanges() {
        LinkedHashSet<String> appearances = new LinkedHashSet<>(List.of("card", "missing"));
        Action action = action("open", Optional.empty(), appearances, false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Interactable drawer = object("drawer", Map.of("open", action));
        Collectable card = item("card", false);
        Room room = room("lab", Optional.empty(), Map.of("drawer", drawer), Map.of("card", card));
        Game game = game(room);
        ObjectState before = drawer.snapshot();

        DomainResult result = game.interact("drawer", "open", Optional.empty());

        assertEquals(ResultCode.INVALID_ACQUISITION, result.code());
        assertFalse(result.changed());
        assertEquals(before, drawer.snapshot());
        assertTrue(game.inventoryItems().isEmpty());
        assertSame(card, room.findCollectable("card").orElseThrow());
        assertFalse(card.isRevealed());
    }

    @Test
    void validatesEveryEffectBeforeApplyingTheFirstEffect() {
        Action action = action("open", Optional.empty(), Set.of("card"), false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open"),
                        new SetObjectStateEffect("nonexistent", "mode", "open")));
        Interactable drawer = object("drawer", Map.of("open", action));
        Room room = room("lab", Optional.empty(), Map.of("drawer", drawer), Map.of("card", item("card", false)));
        Game game = game(room);

        DomainResult result = game.interact("drawer", "open", Optional.empty());

        assertEquals(ResultCode.INVALID_EFFECT, result.code());
        assertEquals("closed", drawer.snapshot().values().get("mode"));
        assertFalse(drawer.isActionCompleted("open"));
        assertTrue(game.inventoryItems().isEmpty());
    }

    @Test
    void unmetConditionsAndWrongOrUnheldToolsLeaveEverythingUnchanged() {
        Action requiringTool = action("open", Optional.of("tool"), Set.of("card"), false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Action unmet = action("blocked", Optional.empty(), Set.of("card"), false,
                List.of(new ObjectStateCondition("drawer", "mode", "open")), List.of());
        Interactable drawer = object("drawer", Map.of("open", requiringTool, "blocked", unmet));
        Room room = room("lab", Optional.empty(), Map.of("drawer", drawer),
                Map.of("card", item("card", false), "tool", item("tool", true), "wrong", item("wrong", true)));
        Game game = game(room);
        ObjectState before = drawer.snapshot();

        assertEquals(ResultCode.MISSING_ITEM, game.interact("drawer", "open", Optional.empty()).code());
        assertEquals(ResultCode.MISSING_ITEM, game.interact("drawer", "open", Optional.of("unheld")).code());
        assertEquals(ResultCode.WRONG_ITEM, game.interact("drawer", "open", Optional.of("wrong")).code());
        assertEquals(ResultCode.UNMET_CONDITION, game.interact("drawer", "blocked", Optional.empty()).code());
        assertEquals(ResultCode.WRONG_ITEM, game.interact("drawer", "blocked", Optional.of("tool")).code());
        assertEquals(before, drawer.snapshot());
        assertEquals(2, game.inventoryItems().size());
        assertFalse(room.findCollectable("card").orElseThrow().isRevealed());
    }

    @Test
    void completedOneShotChecksBeforeEvenMalformedOrUnheldSelectedTools() {
        Game game = GameFixtures.drawerGame();
        assertTrue(game.interact("drawer", "open", Optional.empty()).changed());
        assertEquals(ResultCode.ACTION_COMPLETED, game.interact("drawer", "open", Optional.of("missing")).code());
        assertEquals(ResultCode.ACTION_COMPLETED, game.interact("drawer", "open", null).code());
        assertEquals(1, game.inventoryItems().size());
        assertFalse(game.interact("drawer", "open", Optional.empty()).resultText().contains("Obtained:"));
    }

    @Test
    void repeatableActionsChangeStateAgainWithoutDuplicatingHeldAppearances() {
        Action open = action("open", Optional.empty(), Set.of("card"), true, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Action close = action("close", Optional.empty(), Set.of(), true, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "closed")));
        Interactable drawer = object("drawer", Map.of("open", open, "close", close));
        Game game = game(room("lab", Optional.empty(), Map.of("drawer", drawer), Map.of("card", item("card", false))));
        game.interact("drawer", "open", Optional.empty());
        assertTrue(game.interact("drawer", "close", Optional.empty()).changed());

        DomainResult repeated = game.interact("drawer", "open", Optional.empty());
        assertTrue(repeated.changed());
        assertFalse(repeated.resultText().contains("Obtained:"));
        assertEquals(1, game.inventoryItems().size());
        assertTrue(drawer.snapshot().completedActionIds().isEmpty());
        assertEquals(ResultCode.NO_CHANGE, game.interact("drawer", "open", Optional.empty()).code());
    }

    @Test
    void differentActionsCanNameTheSameHeldItemWithoutDuplicatingItOrItsNotice() {
        Collectable card = item("card", false);
        Action first = action("open", Optional.empty(), Set.of("card"), false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Action second = action("search", Optional.empty(), Set.of("card"), false, List.of(),
                List.of(new SetObjectStateEffect("cabinet", "mode", "open")));
        Interactable drawer = object("drawer", Map.of("open", first));
        Interactable cabinet = object("cabinet", Map.of("search", second));
        Room room = room("lab", Optional.empty(), Map.of("drawer", drawer, "cabinet", cabinet),
                Map.of("card", card));
        Game game = game(room);

        assertTrue(game.interact("drawer", "open", Optional.empty()).resultText().contains("Obtained:"));
        DomainResult result = game.interact("cabinet", "search", Optional.empty());

        assertTrue(result.changed(), "The second object's state and completion still change");
        assertEquals(ResultCode.SUCCESS, result.code());
        assertFalse(result.resultText().contains("Obtained:"));
        assertEquals(List.of(card), game.inventoryItems());
        assertSame(card, game.inventoryItems().get(0));
        assertTrue(cabinet.isActionCompleted("search"));
        assertEquals("open", cabinet.snapshot().values().get("mode"));
        assertTrue(room.allCollectables().isEmpty());
    }

    @Test
    void roomLocalPuzzlesCanReadAndChangeOtherObjectsBeforeAcquiringTheirItem() {
        Action switchOn = action("on", Optional.empty(), Set.of(), false, List.of(),
                List.of(new SetObjectStateEffect("switch", "mode", "open"),
                        new SetObjectStateEffect("drawer", "mode", "open")));
        Action search = action("search", Optional.empty(), Set.of("card"), false,
                List.of(new ObjectStateCondition("switch", "mode", "open"),
                        new ObjectStateCondition("drawer", "mode", "open")), List.of());
        Interactable drawer = object("drawer", Map.of("search", search));
        Interactable lightSwitch = object("switch", Map.of("on", switchOn));
        Room room = room("lab", Optional.empty(), Map.of("drawer", drawer, "switch", lightSwitch),
                Map.of("card", item("card", false)));
        Game game = game(room);

        assertEquals(ResultCode.UNMET_CONDITION, game.interact("drawer", "search", Optional.empty()).code());
        assertFalse(room.availableActions(Set.of()).stream().anyMatch(choice -> choice.objectId().equals("drawer")));
        assertTrue(game.interact("switch", "on", Optional.empty()).changed());
        assertEquals("open", drawer.snapshot().values().get("mode"));
        assertEquals("open", lightSwitch.snapshot().values().get("mode"));
        assertTrue(room.availableActions(Set.of()).stream().anyMatch(choice -> choice.objectId().equals("drawer")));
        assertTrue(game.interact("drawer", "search", Optional.empty()).changed());
        assertEquals("card", game.inventoryItems().get(0).id());
    }

    @Test
    void changedReflectsFinalStateRatherThanIntermediateEffects() {
        Action action = action("cycle", Optional.empty(), Set.of(), true, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open"),
                        new SetObjectStateEffect("drawer", "mode", "closed")));
        Interactable drawer = object("drawer", Map.of("cycle", action));
        Game game = game(room("lab", Optional.empty(), Map.of("drawer", drawer), Map.of("card", item("card", true))));

        DomainResult result = game.interact("drawer", "cycle", Optional.empty());

        assertFalse(result.changed());
        assertEquals(ResultCode.NO_CHANGE, result.code());
        assertEquals("closed", drawer.snapshot().values().get("mode"));
        assertFalse(result.resultText().isBlank());
    }

    @Test
    void currentRoomAccessBlocksForeignObjectsAndForeignPhysicalAppearances() {
        Action foreignAppearance = action("open", Optional.empty(), Set.of("later-card"), false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Interactable drawer = object("drawer", Map.of("open", foreignAppearance));
        Room first = room("lab", Optional.of("later"), Map.of("drawer", drawer), Map.of("card", item("card", true)));
        Room later = room("later", Optional.empty(), Map.of("foreign", object("foreign", Map.of())),
                Map.of("later-card", item("later-card", false)));
        Game game = new Game("Game", "Story", orderedRooms(first, later), "lab");
        assertEquals(ResultCode.INVALID_TARGET, game.interact("foreign", "open", Optional.empty()).code());
        assertEquals(ResultCode.INVALID_ACQUISITION, game.interact("drawer", "open", Optional.empty()).code());
        assertEquals("closed", drawer.snapshot().values().get("mode"));
        assertFalse(later.findCollectable("later-card").orElseThrow().isRevealed());
    }

    @Test
    void aHeldToolFromEarlierRoomRemainsUsableAndIsNeverConsumed() {
        Collectable tool = item("tool", true);
        Room first = room("lab", Optional.of("later"), Map.of(), Map.of("card", item("card", true), "tool", tool));
        Action action = action("open", Optional.of("tool"), Set.of("later-card"), false, List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Room later = room("later", Optional.empty(), Map.of("drawer", object("drawer", Map.of("open", action))),
                Map.of("later-card", item("later-card", false)));
        Game game = new Game("Game", "Story", orderedRooms(first, later), "lab");
        game.useItems("lab-door");
        game.submitAnswer("lab-question", "007");
        assertTrue(game.move().changed());

        assertTrue(game.interact("drawer", "open", Optional.of("tool")).changed());
        assertSame(tool, game.inventoryItems().stream().filter(item -> item.id().equals("tool")).findFirst().orElseThrow());
        assertEquals(3, game.inventoryItems().size());
    }

    @Test
    void plansCannotBeReplayedIntoAnotherWorldOrAfterTheirConditionsBecomeStale() {
        Action open = action("open", Optional.empty(), Set.of(), false,
                List.of(new ObjectStateCondition("drawer", "mode", "closed")),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Interactable one = object("drawer", Map.of("open", open));
        Interactable two = object("drawer", Map.of("open", open));
        Room first = room("lab", Optional.empty(), Map.of("drawer", one), Map.of("card", item("card", false)));
        Room second = room("lab", Optional.empty(), Map.of("drawer", two), Map.of("card", item("card", false)));
        ActionPlan plan = first.prepareInteraction("drawer", "open", Optional.empty());
        assertThrows(DomainException.class, () -> second.applyInteraction(plan));
        one.setStateValue("mode", "open");
        DomainException stale = assertThrows(DomainException.class, () -> first.applyInteraction(plan));
        assertEquals(ResultCode.UNMET_CONDITION, stale.code());
        assertFalse(one.isActionCompleted("open"));
        assertEquals("closed", two.snapshot().values().get("mode"));
    }

    @Test
    void readOnlyQueriesNeverAcquireItemsChangeObjectsOrExposeMutableCollections() {
        Game game = GameFixtures.drawerGame();
        Room room = game.currentRoom();
        ObjectState before = room.interactables().get(0).snapshot();
        for (int i = 0; i < 3; i++) {
            game.inventoryItems();
            game.itemDetails(Set.of("card"));
            room.availableActions(Set.of());
            room.availableLocks();
            room.interactables();
            room.interactables().get(0).describe();
            game.canMove();
        }
        assertTrue(game.inventoryItems().isEmpty());
        assertFalse(room.findCollectable("card").orElseThrow().isRevealed());
        assertEquals(before, room.interactables().get(0).snapshot());
        assertThrows(UnsupportedOperationException.class, () -> game.inventoryItems().clear());
        assertThrows(UnsupportedOperationException.class, () -> room.interactables().clear());
        assertThrows(UnsupportedOperationException.class, () -> room.availableActions(Set.of()).clear());
    }

    @Test
    void bothGatesControlMovementAndEscapedGameRejectsFurtherMutations() {
        Game game = GameFixtures.drawerGame();
        assertEquals(ResultCode.DOOR_LOCKED, game.move().code());
        game.interact("drawer", "open", Optional.empty());
        assertTrue(game.useItems("door").changed());
        assertEquals(ResultCode.TRIVIA_LOCKED, game.move().code());
        assertTrue(game.submitAnswer("question", " 007 ").changed());
        assertTrue(game.canMove());
        assertTrue(game.move().changed());
        assertEquals(GameStatus.ESCAPED, game.status());
        assertFalse(game.canMove());
        assertEquals(ResultCode.GAME_ESCAPED, game.interact("drawer", "read", Optional.empty()).code());
        assertEquals(ResultCode.GAME_ESCAPED, game.move().code());
        assertEquals(ResultCode.GAME_ESCAPED, game.useItems("door").code());
        assertEquals(ResultCode.GAME_ESCAPED, game.submitAnswer("question", "007").code());
        assertEquals(1, game.inventoryItems().size());
    }

    private static Game game(Room room) {
        return new Game("Game", "Story", Map.of(room.id(), room), room.id());
    }
    private static Collectable item(String id, boolean revealed) {
        return new Collectable(id, id + " label", id + " description", revealed);
    }
    private static Action action(String id, Optional<String> tool, Set<String> appearances,
                                 boolean repeatable, List<Condition> conditions, List<Effect> effects) {
        return new Action(id, id + " label", tool, appearances, "Action result", repeatable, conditions, effects);
    }
    private static Interactable object(String id, Map<String, Action> actions) {
        return new Interactable(id, id + " name", Map.of("closed", "Closed drawer", "open", "Open drawer"),
                Map.of("mode", "closed"), Map.of("mode", Set.of("closed", "open")), actions);
    }
    private static Room room(String id, Optional<String> next, Map<String, Interactable> objects,
                             Map<String, Collectable> items) {
        String required = id.equals("later") ? "later-card" : "card";
        return new Room(id, id + " name", id + " description",
                new TriviaLock(id + "-question", "Exit question", "Passed", Optional.of(new Trivia("Code?", List.of(), "007"))),
                new ItemLock(id + "-door", "Door", "Opened", Set.of(required)), next, objects, items);
    }
    private static Map<String, Room> orderedRooms(Room one, Room two) {
        Map<String, Room> rooms = new LinkedHashMap<>();
        rooms.put(one.id(), one);
        rooms.put(two.id(), two);
        return rooms;
    }
}
