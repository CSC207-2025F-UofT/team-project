package view;

import static org.junit.jupiter.api.Assertions.*;

import entity.LockKind;
import interface_adapter.GameController;
import interface_adapter.GameViewModel;
import interface_adapter.GameViewState;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import use_case.*;

class GameViewTest {
    @Test void controlsRouteStableIdsAndRenderAllRequirementsAndFeedback() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(data(false, false, true));
            GameView view = fixture.view;
            named(view, "listStories", JButton.class).doClick(0);
            named(view, "stories", JComboBox.class).setSelectedIndex(1);
            assertEquals("Background B", named(view, "storyBackground", JTextArea.class).getText());
            named(view, "start", JButton.class).doClick(0);
            named(view, "tools", JComboBox.class).setSelectedIndex(2);
            named(view, "action:drawer-b:open", JButton.class).doClick(0);
            named(view, "action:drawer-a:read", JButton.class).doClick(0);
            named(view, "unlock:door-b", JButton.class).doClick(0);
            named(view, "trivia:trivia-b", JButton.class).doClick(0);
            named(view, "answerChoices", JComboBox.class).setSelectedIndex(1);
            assertEquals("001B", named(view, "answer", JTextField.class).getText());
            named(view, "submitAnswer", JButton.class).doClick(0);
            named(view, "move", JButton.class).doClick(0);
            named(view, "save", JButton.class).doClick(0);
            named(view, "load", JButton.class).doClick(0);

            assertEquals(List.of("list", "start:story-b", "interact:drawer-b:open:key-b",
                    "interact:drawer-a:read:none", "use:door-b", "open:trivia-b", "answer:trivia-b:001B",
                    "move", "save", "load"), fixture.calls);
            String text = named(view, "narrative", JTextArea.class).getText();
            assertTrue(text.contains("The drawer is open"));
            assertTrue(text.contains("[held] Key"));
            assertTrue(text.contains("[missing] Badge"));
            assertTrue(text.contains("Obtained: Key"));
            assertEquals("Next room", named(view, "move", JButton.class).getText());
        });
    }

    @Test void busyDisablesInputsAndPreservesSelectionsUntilFreshOutputClearsThem() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(data(false, false, true));
            named(fixture.view, "stories", JComboBox.class).setSelectedIndex(1);
            named(fixture.view, "tools", JComboBox.class).setSelectedIndex(2);
            named(fixture.view, "answer", JTextField.class).setText("old answer");
            fixture.model.setBusy(true);
            for (Component component : descendants(fixture.view)) {
                if (component.getName() != null && (component instanceof AbstractButton
                        || component instanceof JComboBox || component instanceof JTextField)) {
                    assertFalse(component.isEnabled(), component.getName() + " must be disabled while busy");
                }
            }
            assertEquals(1, named(fixture.view, "stories", JComboBox.class).getSelectedIndex());
            assertEquals(2, named(fixture.view, "tools", JComboBox.class).getSelectedIndex());
            assertEquals("old answer", named(fixture.view, "answer", JTextField.class).getText());
            fixture.model.setBusy(false);
            assertTrue(named(fixture.view, "action:drawer-b:open", JButton.class).isEnabled());
            fixture.model.setState(new GameViewState(data(true, false, true), false));
            assertEquals(-1, named(fixture.view, "stories", JComboBox.class).getSelectedIndex());
            assertEquals(0, named(fixture.view, "tools", JComboBox.class).getSelectedIndex());
            assertEquals("", named(fixture.view, "answer", JTextField.class).getText());
            assertEquals("Escape", named(fixture.view, "move", JButton.class).getText());
            assertFalse(named(fixture.view, "action:drawer-b:open", JButton.class).isEnabled());
        });
    }

    @Test void menuAllowsLoadAndStorySelectionAndEscapedGameAllowsSave() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(menu());
            assertTrue(named(fixture.view, "load", JButton.class).isEnabled());
            assertTrue(named(fixture.view, "listStories", JButton.class).isEnabled());
            assertFalse(named(fixture.view, "save", JButton.class).isEnabled());
            assertFalse(named(fixture.view, "start", JButton.class).isEnabled());
            fixture.model.setState(new GameViewState(data(true, true, false), false));
            assertTrue(named(fixture.view, "save", JButton.class).isEnabled());
            assertFalse(named(fixture.view, "move", JButton.class).isEnabled());
            assertFalse(named(fixture.view, "trivia:trivia-b", JButton.class).isEnabled());
            assertFalse(named(fixture.view, "answer", JTextField.class).isEnabled());
        });
    }

    @Test void answerEnterUsesCurrentPromptAndOutputCollectionsAreImmutable() throws Exception {
        Map<String, String> source = new LinkedHashMap<>();
        source.put("Object", "description");
        GameOutputData output = new GameOutputData(Optional.empty(), "Title", "", source, List.of(),
                List.of(), List.of(), Optional.empty(), List.of(), "", false, false, false);
        source.put("Later", "not in output");
        assertEquals(1, output.objectDescriptions().size());
        assertThrows(UnsupportedOperationException.class, () -> output.objectDescriptions().put("x", "x"));
        assertThrows(UnsupportedOperationException.class, () -> output.inventory().add(new ItemData("x", "x")));
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(data(false, false, false));
            JTextField answer = named(fixture.view, "answer", JTextField.class);
            answer.setText(" 001a ");
            answer.postActionEvent();
            assertEquals(List.of("answer:trivia-b: 001a "), fixture.calls);
        });
    }

    @Test void workerModelUpdatesRenderOnEdt() throws Exception {
        AtomicReference<Fixture> reference = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> reference.set(new Fixture(menu())));
        reference.get().model.setState(new GameViewState(data(true, false, false), false));
        SwingUtilities.invokeAndWait(() -> assertEquals("Escape",
                named(reference.get().view, "move", JButton.class).getText()));
    }

    @Test void longPromptsAndStoryBackgroundsRemainScrollableToTheirFinalLine() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String longText = "A long clue that wraps across the available width.\n".repeat(40) + "FINAL CLUE";
            GameOutputData output = new GameOutputData(Optional.of("room-b"), "Laboratory", "Find the exit",
                    Map.of(), List.of(), List.of(), List.of(),
                    Optional.of(new TriviaData("trivia-b", longText, List.of())),
                    List.of(new StorySummary("story-b", "Long story", longText)), "", false, false, false);
            Fixture fixture = new Fixture(output);
            named(fixture.view, "stories", JComboBox.class).setSelectedIndex(0);
            fixture.view.setSize(900, 680);
            layoutTree(fixture.view);
            layoutTree(fixture.view);
            for (String name : List.of("triviaPrompt", "storyBackground")) {
                JTextArea text = named(fixture.view, name, JTextArea.class);
                JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, text);
                assertNotNull(scroll, "Long text must provide a scrollable viewport");
                assertEquals(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, scroll.getVerticalScrollBarPolicy());
                assertTrue(text.getText().endsWith("FINAL CLUE"));
                try {
                    java.awt.Rectangle lastLine = text.modelToView2D(text.getDocument().getLength() - 1).getBounds();
                    assertTrue(lastLine.y > scroll.getViewport().getExtentSize().height);
                    text.scrollRectToVisible(lastLine);
                    assertTrue(scroll.getViewport().getViewRect().intersects(lastLine),
                            name + " final line " + lastLine + " must be reachable in "
                                    + scroll.getViewport().getViewRect() + "; text size " + text.getSize());
                } catch (javax.swing.text.BadLocationException exception) {
                    throw new AssertionError(exception);
                }
            }
        });
    }

    @Test void longChoicesWithTheSamePrefixCanBeReadToTheirDistinctEndingsAndSubmittedWhole() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String prefix = "<html><b>A shared answer prefix & literal markup.</b>\n".repeat(40);
            List<String> answers = List.of(prefix + "ENDING ALPHA", prefix + "ENDING BETA");
            Fixture fixture = new Fixture(choiceData(answers));
            fixture.view.setSize(900, 680);
            JComboBox<?> choices = named(fixture.view, "answerChoices", JComboBox.class);
            for (int index = 0; index < answers.size(); index++) {
                choices.setSelectedIndex(index);
                layoutTree(fixture.view);
                layoutTree(fixture.view);
                JTextArea preview = named(fixture.view, "selectedAnswerChoice", JTextArea.class);
                assertEquals(answers.get(index), preview.getText(), "Full choices must remain literal text");
                assertEquals(answers.get(index).replace('\n', ' '),
                        named(fixture.view, "answer", JTextField.class).getText(),
                        "The single-line field may normalize display while submission retains the original");
                assertScrollableToLastCharacter(preview);
                named(fixture.view, "submitAnswer", JButton.class).doClick(0);
            }
            assertEquals(List.of("answer:question:" + answers.get(0),
                    "answer:question:" + answers.get(1)), fixture.calls);
        });
    }

    @Test void busyPreservesFullChoiceWhileFreshLoadedOutputClearsItAndUsesTheNewSelection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            String prefix = "A complete choice with a shared prefix.\n".repeat(30);
            List<String> answers = List.of(prefix + "FIRST", prefix + "SECOND");
            Fixture fixture = new Fixture(choiceData(answers));
            named(fixture.view, "answerChoices", JComboBox.class).setSelectedIndex(1);

            fixture.model.setBusy(true);
            assertEquals(1, named(fixture.view, "answerChoices", JComboBox.class).getSelectedIndex());
            assertEquals(answers.get(1), named(fixture.view, "selectedAnswerChoice", JTextArea.class).getText());
            assertEquals(answers.get(1).replace('\n', ' '), named(fixture.view, "answer", JTextField.class).getText());
            assertFalse(named(fixture.view, "answerChoices", JComboBox.class).isEnabled());
            assertFalse(named(fixture.view, "submitAnswer", JButton.class).isEnabled());
            fixture.model.setBusy(false);
            assertEquals(1, named(fixture.view, "answerChoices", JComboBox.class).getSelectedIndex());
            assertEquals(answers.get(1), named(fixture.view, "selectedAnswerChoice", JTextArea.class).getText());

            fixture.model.setState(new GameViewState(choiceData(answers), false));
            assertEquals(-1, named(fixture.view, "answerChoices", JComboBox.class).getSelectedIndex());
            assertEquals("", named(fixture.view, "selectedAnswerChoice", JTextArea.class).getText());
            assertEquals("", named(fixture.view, "answer", JTextField.class).getText());
            assertTrue(fixture.calls.isEmpty(), "Busy and load rendering must not submit an old answer");
            named(fixture.view, "answerChoices", JComboBox.class).setSelectedIndex(0);
            named(fixture.view, "submitAnswer", JButton.class).doClick(0);
            assertEquals(List.of("answer:question:" + answers.get(0)), fixture.calls);
        });
    }

    @Test void typingAnAnswerOverridesTheRawSelectedChoiceEvenAcrossBusyUpdates() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Fixture fixture = new Fixture(choiceData(List.of("An answer\nwith a second line", "Another answer")));
            named(fixture.view, "answerChoices", JComboBox.class).setSelectedIndex(0);
            named(fixture.view, "answer", JTextField.class).setText("  My edited answer  ");
            fixture.model.setBusy(true);
            fixture.model.setBusy(false);
            named(fixture.view, "submitAnswer", JButton.class).doClick(0);
            assertEquals(List.of("answer:question:  My edited answer  "), fixture.calls);
        });
    }

    private static void assertScrollableToLastCharacter(JTextArea text) {
        JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, text);
        assertNotNull(scroll, "A complete selected answer needs a scrollable viewport");
        try {
            java.awt.Rectangle lastLine = text.modelToView2D(text.getDocument().getLength() - 1).getBounds();
            assertTrue(lastLine.y > scroll.getViewport().getExtentSize().height);
            text.scrollRectToVisible(lastLine);
            assertTrue(scroll.getViewport().getViewRect().intersects(lastLine),
                    "The ending that distinguishes the answer must be readable");
        } catch (javax.swing.text.BadLocationException exception) {
            throw new AssertionError(exception);
        }
    }

    private static GameOutputData choiceData(List<String> answers) {
        return new GameOutputData(Optional.of("room"), "Laboratory", "Choose an answer.",
                Map.of(), List.of(), List.of(), List.of(),
                Optional.of(new TriviaData("question", "Which explanation is correct?", answers)),
                List.of(), "", false, false, false);
    }

    private static void layoutTree(Container parent) {
        parent.doLayout();
        for (Component child : parent.getComponents()) {
            if (child instanceof Container) layoutTree((Container) child);
        }
    }

    private static GameOutputData menu() {
        return new GameOutputData(Optional.empty(), "Escape Room", "Choose a story", Map.of(), List.of(),
                List.of(), List.of(), Optional.empty(), stories(), "Menu feedback", false, false, false);
    }

    private static GameOutputData data(boolean finalRoom, boolean escaped, boolean choices) {
        return new GameOutputData(Optional.of("room-b"), "Laboratory", "Find the exit",
                Map.of("Drawer [drawer-b]", "The drawer is open"),
                List.of(new ItemData("key-a", "Key"), new ItemData("key-b", "Key")),
                List.of(new ActionData("drawer-b", "open", "Open", true),
                        new ActionData("drawer-a", "read", "Read", false)),
                List.of(new LockData("door-b", "room-b", "Laboratory door", LockKind.ITEM, false,
                        List.of(new ItemData("key-b", "Key"), new ItemData("badge", "Badge")), Set.of("key-b"), true),
                        new LockData("trivia-b", "room-b", "Exit question", LockKind.TRIVIA,
                                false, List.of(), Set.of(), false)),
                Optional.of(new TriviaData("trivia-b", "What is the code?", choices ? List.of("001A", "001B") : List.of())),
                stories(), "Obtained: Key", !escaped, finalRoom, escaped);
    }

    private static List<StorySummary> stories() {
        return List.of(new StorySummary("story-a", "Laboratory", "Background A"),
                new StorySummary("story-b", "Laboratory", "Background B"));
    }

    private static List<Component> descendants(Container parent) {
        List<Component> all = new ArrayList<>();
        for (Component child : parent.getComponents()) {
            all.add(child);
            if (child instanceof Container) all.addAll(descendants((Container) child));
        }
        return all;
    }

    private static <T extends Component> T named(Container parent, String name, Class<T> type) {
        return descendants(parent).stream().filter(c -> name.equals(c.getName()) && type.isInstance(c))
                .map(type::cast).findFirst().orElseThrow(() -> new AssertionError("Missing component: " + name));
    }

    private static final class Fixture {
        private final List<String> calls = new ArrayList<>();
        private final GameViewModel model;
        private final GameView view;
        private Fixture(GameOutputData output) {
            model = new GameViewModel(new GameViewState(output, false));
            GameController controller = new GameController(new StartGameInputBoundary() {
                public void listStories() { calls.add("list"); }
                public void start(String id) { calls.add("start:" + id); }
            }, (object, action, item) -> calls.add("interact:" + object + ":" + action + ":" + item.orElse("none")),
                    id -> calls.add("use:" + id), new TriviaInputBoundary() {
                        public void open(String id) { calls.add("open:" + id); }
                        public void submit(String id, String answer) { calls.add("answer:" + id + ":" + answer); }
                    }, () -> calls.add("move"), () -> calls.add("save"), () -> calls.add("load"), Runnable::run);
            view = new GameView(controller, model);
        }
    }
}
