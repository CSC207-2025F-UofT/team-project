package integration;

import app.AppBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import data_access.JsonStoryTemplateDataAccessObject;
import entity.*;
import use_case.*;
import java.awt.Component;
import java.awt.Container;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class Package1Test {
    final JsonStoryTemplateDataAccessObject catalog = new JsonStoryTemplateDataAccessObject();
    final GameFactory factory = new GameFactory(new Random(15));

    @Test void playEveryRoomAndKeepASimultaneousSessionIndependent() {
        StoryTemplate template = catalog.load("laboratory");
        Game first = factory.create(template);
        Game other = factory.create(template);
        assertNotSame(first.currentRoom(), other.currentRoom());
        String code = first.generatedValues().get("code");
        assertTrue(code.matches("[0-9]{4}"));
        assertEquals(3, template.rooms.size());
        assertEquals("lounge", first.currentRoom().id());
        assertTrue(first.interact("window", "open", Optional.empty()).resultText().contains("painted shut"));
        assertTrue(first.interact("sofa", "search", Optional.empty()).resultText().contains("laundry tag"));
        assertTrue(first.interact("sofa", "lift-dark", Optional.empty()).resultText().contains("too dark"));
        assertEquals(1, first.inventoryItems().size());
        assertFalse(first.useItems("lounge-door").changed());
        assertTrue(first.interact("lamp", "switch-on", Optional.empty()).changed());
        assertTrue(first.interact("sofa", "lift-lit", Optional.empty()).resultText().contains(code));
        assertFalse(first.interact("sofa", "lift-empty", Optional.empty()).changed());
        leaveRoom(first, code);

        assertEquals("records", first.currentRoom().id());
        assertTrue(first.interact("cabinet", "unlock", Optional.of("key")).changed());
        assertTrue(first.interact("map", "flatten", Optional.empty()).resultText().contains("EAST"));
        leaveRoom(first, "EAST");

        assertEquals("platform", first.currentRoom().id());
        assertFalse(first.interact("press", "push-empty", Optional.empty()).changed());
        assertTrue(first.interact("press", "insert", Optional.of("card")).changed());
        assertTrue(first.interact("press", "stamp", Optional.empty()).resultText().contains("DAWN"));
        leaveRoom(first, "DAWN");
        assertEquals(GameStatus.ESCAPED, first.status());
        assertEquals("lounge", other.currentRoom().id());
        assertEquals(1, other.inventoryItems().size());
        assertTrue(other.interact("sofa", "lift-dark", Optional.empty()).resultText().contains("too dark"));
    }

    private static void leaveRoom(Game game, String answer) {
        Room room = game.currentRoom();
        assertTrue(game.submitAnswer(room.exitChallenge().id(), answer).changed());
        assertTrue(game.useItems(room.doorLock().id()).changed());
        assertTrue(game.move().changed());
    }

    @ParameterizedTest
    @ValueSource(strings = {"cycle", "short chain", "unknown room", "duplicate", "bad effect", "foreign item",
            "missing items", "future tool", "self prerequisite", "future door", "bad clue", "bad trivia"})
    void invalidStoryPreservesActiveSession(String defect, @TempDir Path directory) throws Exception {
        ObjectMapper json = new ObjectMapper();
        ObjectNode story = (ObjectNode) json.readTree(getClass().getResource("/stories/laboratory.json"));
        ObjectNode room = (ObjectNode) story.at("/rooms/0");
        ObjectNode action = (ObjectNode) room.at("/interactables/0/actions/0");
        switch (defect) {
            case "cycle": room.put("nextRoomId", "lounge"); break;
            case "short chain": room.putNull("nextRoomId"); break;
            case "unknown room": room.put("nextRoomId", "missing"); break;
            case "duplicate": ((ObjectNode) story.at("/items/0")).put("id", "sofa"); break;
            case "bad effect": ((ObjectNode) action.at("/effects/0")).put("key", "broken"); break;
            case "foreign item": action.putArray("appearingItemIds").add("card"); break;
            case "missing items": ((ObjectNode) room.get("doorLock")).putArray("requiredItemIds"); break;
            case "future tool": action.put("requiredItemId", "card"); break;
            case "self prerequisite": action.put("requiredItemId", "key"); break;
            case "future door": ((ObjectNode) room.get("doorLock")).putArray("requiredItemIds").add("card"); break;
            case "bad clue": action.put("resultText", "${missing}"); break;
            case "bad trivia": ((ObjectNode) room.at("/exitChallenge/spec")).put("source", "API"); break;
            default: fail(defect);
        }
        json.writeValue(directory.resolve("story.json").toFile(), story);
        Game original = factory.create(catalog.load("laboratory"));
        GameContext context = new GameContext(original);
        AtomicReference<GameOutputData> output = new AtomicReference<>();
        new StartGameInteractor(context, factory, new JsonStoryTemplateDataAccessObject(directory),
                new GameOutputAssembler(), output::set).start("laboratory");
        assertSame(original, context.requireCurrent());
        assertTrue(output.get().message().contains("failed"));
    }

    @Test void assembledStoryPickerStartsAndRendersFirstInventory() throws Exception {
        try (AppBuilder app = edt(AppBuilder::new)) {
            edt(() -> { app.listStories(); return null; });
            await(() -> ((JComboBox<?>) named(app.view(), "stories")).isEnabled());
            edt(() -> {
                ((JComboBox<?>) named(app.view(), "stories")).setSelectedIndex(0);
                ((JButton) named(app.view(), "start")).doClick(0);
                return null;
            });
            await(() -> ((JTextArea) named(app.view(), "narrative")).getText().contains("Obtained: Visitor wristband."));
            click(app, "object:sofa");
            click(app, "action:sofa:lift-dark");
            await(() -> ((JTextArea) named(app.view(), "narrative")).getText().contains("too dark"));
            click(app, "backToRoom");
            click(app, "object:lamp");
            click(app, "action:lamp:switch-on");
            click(app, "backToRoom");
            click(app, "object:sofa");
            click(app, "action:sofa:lift-lit");
            await(() -> ((JButton) named(app.view(), "unlock:lounge-door")).isEnabled());
            String narrative = edt(() -> ((JTextArea) named(app.view(), "narrative")).getText());
            assertTrue(narrative.contains("Obtained: Brass key."));
            assertFalse(narrative.contains("Obtained: Visitor wristband."));
        }
    }

    private static void click(AppBuilder app, String id) throws Exception {
        await(() -> {
            Component button = named(app.view(), id);
            return button != null && button.isEnabled();
        });
        edt(() -> { ((JButton) named(app.view(), id)).doClick(0); return null; });
    }

    private static Component named(Container parent, String name) {
        for (Component child : parent.getComponents()) {
            if (name.equals(child.getName())) return child;
            if (child instanceof Container) {
                Component found = named((Container) child, name);
                if (found != null) return found;
            }
        }
        return null;
    }

    private static void await(Callable<Boolean> condition) throws Exception {
        long deadline = System.nanoTime() + 5_000_000_000L;
        while (!edt(condition)) {
            assertTrue(System.nanoTime() < deadline, "Timed out waiting for view");
            Thread.sleep(10);
        }
    }

    private static <T> T edt(Callable<T> call) throws Exception {
        FutureTask<T> result = new FutureTask<>(call);
        SwingUtilities.invokeAndWait(result);
        return result.get();
    }
}
