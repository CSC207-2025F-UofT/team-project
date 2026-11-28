package view;

import entity.LockKind;
import interface_adapter.GameController;
import interface_adapter.GameViewModel;
import interface_adapter.GameViewState;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import use_case.ActionData;
import use_case.GameOutputData;
import use_case.ItemData;
import use_case.LockData;
import use_case.StorySummary;
import use_case.TriviaData;

/** Text-and-choice Swing interface. All rendered information comes from immutable UI data. */
public final class GameView extends JPanel {
    private static final long serialVersionUID = 1L;
    private final GameController controller;
    private final GameViewModel viewModel;
    private final JTextArea textArea = new JTextArea();
    private final JPanel optionsPanel = new JPanel();
    private final JTextField answerField = new JTextField(20);
    private final PropertyChangeListener modelListener;
    private JComboBox<StorySummary> stories;
    private JComboBox<ToolChoice> tools;
    private JComboBox<String> answerChoices;
    private String selectedChoiceValue;
    private String selectedChoiceFieldValue;
    private GameOutputData renderedData;
    private String selectedObject;
    private boolean listening;

    public GameView(GameController controller, GameViewModel viewModel) {
        super(new BorderLayout(12, 12));
        this.controller = Objects.requireNonNull(controller, "controller");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel");
        setName("gameView");
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setPreferredSize(new Dimension(900, 680));
        textArea.setName("narrative");
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getAccessibleContext().setAccessibleName("Story, room, inventory and feedback");
        optionsPanel.setName("options");
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        answerField.setName("answer");
        answerField.getAccessibleContext().setAccessibleName("Trivia answer");
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        JScrollPane optionsScroll = new JScrollPane(optionsPanel);
        optionsScroll.setPreferredSize(new Dimension(360, 680));
        add(optionsScroll, BorderLayout.EAST);
        modelListener = event -> render(viewModel.getState());
        attachListener();
        render(viewModel.getState());
    }

    private void attachListener() {
        if (!listening) {
            viewModel.addPropertyChangeListener(modelListener);
            listening = true;
        }
    }

    @Override public void addNotify() {
        super.addNotify();
        attachListener();
        render(viewModel.getState());
    }

    @Override public void removeNotify() {
        if (listening) {
            viewModel.removePropertyChangeListener(modelListener);
            listening = false;
        }
        super.removeNotify();
    }

    /** Rebuilds controls on the EDT. Fresh output clears selections from the previous session. */
    public void render(GameViewState state) {
        Objects.requireNonNull(state, "state");
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> render(state));
            return;
        }
        GameOutputData data = state.data();
        boolean sameOutput = data == renderedData;
        if (!sameOutput) {
            selectedChoiceValue = null;
            selectedChoiceFieldValue = null;
        }
        String selectedStory = sameOutput && stories != null && stories.getSelectedItem() != null
                ? ((StorySummary) stories.getSelectedItem()).templateId() : null;
        Optional<String> selectedTool = sameOutput ? selectedTool() : Optional.empty();
        String previousAnswer = sameOutput ? answerField.getText() : "";
        Optional<String> previousChoice = sameOutput && answerChoices != null
                ? Optional.ofNullable((String) answerChoices.getSelectedItem()) : Optional.empty();
        if (renderedData == null || !renderedData.roomId().equals(data.roomId())) selectedObject = null;
        renderedData = data;
        optionsPanel.removeAll();
        textArea.setText(narrative(data, state.busy()));
        textArea.setCaretPosition(0);
        addStoryControls(data, state.busy(), selectedStory);
        if (data.roomId().isPresent()) {
            addActionControls(data, state.busy(), selectedTool);
            addLockControls(data, state.busy());
            addAnswerControls(data, state.busy(), previousAnswer, previousChoice);
            JButton move = button(data.finalRoom() ? "Escape" : "Next room", "move",
                    !state.busy() && !data.escaped() && data.canMove(), controller::move);
            optionsPanel.add(row(move));
        } else {
            tools = null;
            answerChoices = null;
            answerField.setText("");
            answerField.setEnabled(false);
        }
        optionsPanel.add(row(button("Save", "save", !state.busy() && data.roomId().isPresent(),
                controller::save), button("Load", "load", !state.busy(), controller::load)));
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void addStoryControls(GameOutputData data, boolean busy, String selectedId) {
        optionsPanel.add(row(button("Browse stories", "listStories", !busy, controller::listStories)));
        stories = new JComboBox<>(data.stories().toArray(new StorySummary[0]));
        stories.setName("stories");
        stories.setPreferredSize(new Dimension(280, stories.getPreferredSize().height));
        stories.getAccessibleContext().setAccessibleName("Story template");
        stories.setSelectedIndex(-1);
        if (selectedId != null) {
            for (int i = 0; i < stories.getItemCount(); i++) {
                if (stories.getItemAt(i).templateId().equals(selectedId)) stories.setSelectedIndex(i);
            }
        }
        stories.setEnabled(!busy && !data.stories().isEmpty());
        JButton start = button("Start story", "start", !busy && stories.getSelectedItem() != null,
                () -> {
                    StorySummary selected = (StorySummary) stories.getSelectedItem();
                    if (selected != null) controller.start(selected.templateId());
                });
        JTextArea background = wrappedText("");
        background.setName("storyBackground");
        background.getAccessibleContext().setAccessibleName("Selected story background");
        Runnable updateSelection = () -> {
            StorySummary selected = (StorySummary) stories.getSelectedItem();
            background.setText(selected == null ? "Choose a story" : selected.background());
            background.setToolTipText(selected == null ? null : selected.background());
            start.setEnabled(!busy && selected != null);
        };
        stories.addActionListener(event -> updateSelection.run());
        updateSelection.run();
        optionsPanel.add(row(stories));
        optionsPanel.add(row(textScroll(background)));
        optionsPanel.add(row(start));
    }

    private void addActionControls(GameOutputData data, boolean busy, Optional<String> selectedId) {
        boolean active = !busy && !data.escaped();
        if (data.objectDescriptions().size() > 1) {
            if (selectedObject == null) {
                tools = null;
                data.objectDescriptions().forEach((id, description) ->
                        optionsPanel.add(row(button(description.lines().findFirst().orElse(id), "object:" + id,
                                active, () -> selectObject(id)))));
                return;
            }
            optionsPanel.add(row(button("Back to room", "backToRoom", !busy, () -> selectObject(null))));
        }
        List<ToolChoice> choices = new ArrayList<>();
        choices.add(new ToolChoice(Optional.empty(), "Choose a held tool"));
        for (ItemData item : data.inventory()) {
            choices.add(new ToolChoice(Optional.of(item.collectableId()), item.label()));
        }
        tools = new JComboBox<>(choices.toArray(new ToolChoice[0]));
        tools.setPreferredSize(new Dimension(245, tools.getPreferredSize().height));
        tools.setName("tools");
        tools.getAccessibleContext().setAccessibleName("Held tool for object actions");
        for (int i = 0; i < tools.getItemCount(); i++) {
            if (tools.getItemAt(i).id.equals(selectedId)) tools.setSelectedIndex(i);
        }
        tools.setEnabled(active && !data.inventory().isEmpty());
        JLabel toolLabel = new JLabel("Tool:");
        toolLabel.setLabelFor(tools);
        optionsPanel.add(row(toolLabel, tools));
        List<JButton> toolActions = new ArrayList<>();
        for (ActionData action : data.actions()) {
            if (selectedObject != null && !selectedObject.equals(action.objectId())) continue;
            JButton actionButton = button(action.label(), "action:" + action.objectId() + ":" + action.actionId(),
                    active && (!action.requiresItem() || selectedTool().isPresent()),
                    () -> controller.interact(action.objectId(), action.actionId(),
                            action.requiresItem() ? selectedTool() : Optional.empty()));
            actionButton.setToolTipText(action.requiresItem() ? "Choose a held tool for this action" : action.label());
            if (action.requiresItem()) toolActions.add(actionButton);
            optionsPanel.add(row(actionButton));
        }
        tools.addActionListener(event -> {
            for (JButton action : toolActions) action.setEnabled(active && selectedTool().isPresent());
        });
    }

    private void selectObject(String id) {
        selectedObject = id;
        render(viewModel.getState());
    }

    private Optional<String> selectedTool() {
        if (tools == null || tools.getSelectedItem() == null) return Optional.empty();
        return ((ToolChoice) tools.getSelectedItem()).id;
    }

    private void addLockControls(GameOutputData data, boolean busy) {
        for (LockData lock : data.locks()) {
            if (lock.kind() == LockKind.ITEM) {
                optionsPanel.add(row(button("Unlock door", "unlock:" + lock.lockId(),
                        !busy && !data.escaped() && lock.canUnlock(), () -> controller.useItems(lock.lockId()))));
            } else {
                optionsPanel.add(row(button("Open question", "trivia:" + lock.lockId(),
                        !busy && !data.escaped() && !lock.unlocked(), () -> controller.openTrivia(lock.lockId()))));
            }
        }
    }

    private void addAnswerControls(GameOutputData data, boolean busy, String previousAnswer,
                                   Optional<String> previousChoice) {
        for (java.awt.event.ActionListener listener : answerField.getActionListeners()) {
            answerField.removeActionListener(listener);
        }
        answerField.setText(previousAnswer);
        answerField.setEnabled(!busy && !data.escaped() && data.activeTrivia().isPresent());
        answerChoices = null;
        if (data.activeTrivia().isEmpty()) return;
        TriviaData trivia = data.activeTrivia().get();
        JTextArea prompt = wrappedText(trivia.prompt());
        prompt.setName("triviaPrompt");
        prompt.getAccessibleContext().setAccessibleName("Exit question");
        optionsPanel.add(row(textScroll(prompt)));
        if (!trivia.choices().isEmpty()) {
            JComboBox<String> choices = new JComboBox<>(trivia.choices().toArray(new String[0]));
            choices.setPreferredSize(new Dimension(280, choices.getPreferredSize().height));
            choices.setName("answerChoices");
            choices.getAccessibleContext().setAccessibleName("Answer choices");
            choices.setSelectedIndex(-1);
            previousChoice.ifPresent(choices::setSelectedItem);
            choices.setEnabled(!busy && !data.escaped());
            choices.setToolTipText("Read the complete selected answer below");
            JTextArea fullChoice = wrappedText(previousChoice.orElse(""));
            fullChoice.setName("selectedAnswerChoice");
            fullChoice.getAccessibleContext().setAccessibleName("Complete selected answer choice");
            fullChoice.setCaretPosition(0);
            choices.addActionListener(event -> {
                String selected = (String) choices.getSelectedItem();
                fullChoice.setText(selected == null ? "" : selected);
                fullChoice.setCaretPosition(0);
                selectedChoiceValue = selected;
                if (selected != null) {
                    answerField.setText(selected);
                    // JTextField normalizes newlines; retain the original choice for submission.
                    selectedChoiceFieldValue = answerField.getText();
                } else {
                    selectedChoiceFieldValue = null;
                }
            });
            answerChoices = choices;
            optionsPanel.add(row(choices));
            JLabel selectionLabel = new JLabel("Selected answer:");
            selectionLabel.setLabelFor(fullChoice);
            optionsPanel.add(row(selectionLabel));
            optionsPanel.add(row(textScroll(fullChoice)));
        }
        Runnable submit = () -> {
            String entered = answerField.getText();
            String answer = selectedChoiceValue != null && entered.equals(selectedChoiceFieldValue)
                    ? selectedChoiceValue : entered;
            controller.submitAnswer(trivia.lockId(), answer);
        };
        answerField.addActionListener(event -> {
            if (answerField.isEnabled()) submit.run();
        });
        optionsPanel.add(row(answerField));
        optionsPanel.add(row(button("Submit answer", "submitAnswer", !busy && !data.escaped(), submit)));
    }

    private static String narrative(GameOutputData data, boolean busy) {
        StringBuilder text = new StringBuilder(data.title()).append("\n\n").append(data.narrative());
        data.objectDescriptions().forEach((id, description) -> text.append("\n\n").append(description));
        if (data.roomId().isPresent()) {
            text.append("\n\nInventory\n");
            if (data.inventory().isEmpty()) text.append("Empty\n");
            for (ItemData item : data.inventory()) text.append("• ").append(item.label()).append('\n');
            for (LockData lock : data.locks()) {
                text.append("\n").append(lock.label()).append(lock.unlocked() ? " — complete\n" : " — incomplete\n");
                for (ItemData item : lock.requiredItems()) {
                    text.append(lock.heldRequiredIds().contains(item.collectableId()) ? "[held] " : "[missing] ")
                            .append(item.label()).append('\n');
                }
            }
        }
        if (data.escaped()) text.append("\nEscaped!\n");
        if (!data.message().isEmpty()) text.append("\n").append(data.message()).append('\n');
        if (busy) text.append("\nWorking…\n");
        return text.toString();
    }

    private static JButton button(String label, String name, boolean enabled, Runnable command) {
        JButton button = new JButton(label);
        button.setName(name);
        button.getAccessibleContext().setAccessibleName(label);
        button.setEnabled(enabled);
        button.addActionListener(event -> command.run());
        return button;
    }

    private static JPanel row(Component... controls) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEADING));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Component control : controls) row.add(control);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
        return row;
    }

    private static JTextArea wrappedText(String text) {
        JTextArea area = new JTextArea(text, 3, 24);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        return area;
    }

    private static JScrollPane textScroll(JTextArea text) {
        JScrollPane scroll = new JScrollPane(text);
        scroll.setPreferredSize(new Dimension(300, 80));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scroll;
    }

    private static final class ToolChoice {
        private final Optional<String> id;
        private final String label;
        private ToolChoice(Optional<String> id, String label) { this.id = id; this.label = label; }
        @Override public String toString() { return label; }
    }
}
