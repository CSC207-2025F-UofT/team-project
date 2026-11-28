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
        List<String> answers = List.of(code, "MOTH", "42", "EAST", "DAWN");
        for (int i = 0; i < answers.size(); i++) {
            Room room = first.currentRoom();
            Interactable obj = room.interactables().get(0);
            Action action = obj.actions().get(0);
            DomainResult result = first.interact(obj.id(), action.id(), action.requiredItemId());
            assertTrue(result.changed(), result.resultText());
            if (i == 0) assertTrue(result.resultText().contains(code));
            assertTrue(first.submitAnswer(room.exitChallenge().id(), answers.get(i)).changed());
            assertTrue(first.useItems(room.doorLock().id()).changed());
            assertTrue(first.move().changed());
        }
        assertEquals(GameStatus.ESCAPED, first.status());
        assertEquals("intake", other.currentRoom().id());
        assertEquals(1, other.inventoryItems().size());
        assertFalse(other.currentRoom().interactables().get(0).isActionCompleted("inspect"));
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
            case "cycle": room.put("nextRoomId", "intake"); break;
            case "short chain": room.putNull("nextRoomId"); break;
            case "unknown room": room.put("nextRoomId", "missing"); break;
            case "duplicate": ((ObjectNode) story.at("/items/0")).put("id", "desk"); break;
            case "bad effect": ((ObjectNode) action.at("/effects/0")).put("key", "broken"); break;
            case "foreign item": action.putArray("appearingItemIds").add("fuse"); break;
            case "missing items": ((ObjectNode) room.get("doorLock")).putArray("requiredItemIds"); break;
            case "future tool": action.put("requiredItemId", "fuse"); break;
            case "self prerequisite": action.put("requiredItemId", "card"); break;
            case "future door": ((ObjectNode) room.get("doorLock")).putArray("requiredItemIds").add("fuse"); break;
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
            await(() -> ((JButton) named(app.view(), "action:desk:inspect")).isEnabled());
            edt(() -> { ((JButton) named(app.view(), "action:desk:inspect")).doClick(0); return null; });
            await(() -> ((JButton) named(app.view(), "unlock:intake-door")).isEnabled());
            String narrative = edt(() -> ((JTextArea) named(app.view(), "narrative")).getText());
            assertTrue(narrative.contains("Obtained: Striped access card."));
            assertFalse(narrative.contains("Obtained: Visitor wristband."));
        }
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
