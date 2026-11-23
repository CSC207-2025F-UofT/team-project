package view;

import interface_adapter.challenge.ChallengeController;
import interface_adapter.challenge.ChallengeViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import static interface_adapter.challenge.ChallengeViewModel.Screen;

/** One injectable building page; API calls never run on the event dispatch thread. */
public final class ClassChallengeView extends JPanel {
    private final ChallengeController controller;
    private final ChallengeViewModel model;
    private final Runnable backToStreet;
    private final JLabel energy = new JLabel();
    private final JLabel loading = new JLabel(" ");
    private final JTextArea error = text("", 2);
    private final JPanel body = new JPanel(new BorderLayout());
    private final JButton back = new JButton("Back to Street");
    private boolean busy;
    private ChallengeViewModel.State lastLeft;

    public ClassChallengeView(ChallengeController controller, ChallengeViewModel model,
                              Runnable backToStreet) {
        this.controller = controller;
        this.model = model;
        this.backToStreet = backToStreet;
        setName("lecture");
        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JPanel heading = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Lecture Hall — Class Challenge");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        heading.add(title, BorderLayout.WEST);
        heading.add(energy, BorderLayout.EAST);
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.add(heading);
        north.add(loading);
        error.setForeground(new Color(160, 25, 25));
        error.setName("challenge.error");
        north.add(error);
        add(north, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
        back.setName("challenge.back");
        back.addActionListener(event -> requestLeave());
        add(back, BorderLayout.SOUTH);
        model.addListener(event -> SwingUtilities.invokeLater(() -> render(model.getState())));
        addHierarchyListener(event -> {
            if ((event.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                refresh();
            }
        });
        render(model.getState());
    }

    /** Call when entering the page after choosing a major, resting or loading a checkpoint. */
    public void refresh() { run(controller::open); }

    /** Navigation must use this hook to respect the unfinished-activity cancellation rule. */
    public void requestLeave() {
        if (busy) { return; }
        Screen screen = model.getState().screen;
        boolean active = screen == Screen.QUESTION || screen == Screen.FEEDBACK;
        if (active && JOptionPane.showConfirmDialog(this,
                "Leave this challenge? Spent energy will not be refunded and no points will be recorded.",
                "Leave challenge", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        run(() -> controller.leave(active));
    }

    private void run(Runnable action) {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("Invoke view actions on the event dispatch thread");
        }
        if (busy) { return; }
        busy = true;
        loading.setText("Working…");
        enableControls(this, false);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() { action.run(); return null; }
            @Override protected void done() {
                busy = false;
                loading.setText(" ");
                String failure = null;
                try { get(); }
                catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    failure = "The operation was interrupted. Please retry.";
                }
                catch (ExecutionException exception) {
                    java.util.logging.Logger.getLogger(ClassChallengeView.class.getName())
                            .log(java.util.logging.Level.WARNING, "Challenge operation failed", exception.getCause());
                    failure = "The operation failed unexpectedly. Please retry or return to Street.";
                }
                render(model.getState());
                if (failure != null) { error.setText(failure); }
            }
        }.execute();
    }

    private void render(ChallengeViewModel.State state) {
        energy.setText(state.energy);
        error.setText(state.error);
        error.setVisible(!state.error.isBlank());
        body.removeAll();
        JPanel content = column();
        switch (state.screen) {
            case COURSES: courses(content, state); break;
            case CONFIRM:
                content.add(text(state.courseName, 1));
                content.add(text(state.instructions, 5));
                content.add(button("Load questions and start", "challenge.start",
                        () -> run(() -> controller.start(state.courseId))));
                content.add(button("Back to courses", "challenge.courses", () -> run(controller::open)));
                break;
            case QUESTION:
            case FEEDBACK: question(content, state); break;
            case RESULT:
                content.add(text(state.courseName, 1));
                content.add(text(state.result, 5));
                content.add(button("Back to courses", "challenge.courses", () -> run(controller::open)));
                break;
            case LEFT:
                if (!busy && lastLeft != state) { lastLeft = state; backToStreet.run(); }
                break;
            default: throw new IllegalStateException("Unexpected screen");
        }
        content.add(Box.createVerticalGlue());
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        body.add(scroll);
        enableControls(this, !busy);
        if (state.screen == Screen.FEEDBACK) { disableAnswers(content); }
        revalidate();
        repaint();
    }

    private void courses(JPanel content, ChallengeViewModel.State state) {
        content.add(text(state.courses.isEmpty() ? "Choose a major at the Registrar first."
                : "Select an assigned course to start a challenge or view its result.", 2));
        JList<ChallengeViewModel.CourseItem> list = new JList<>(state.courses.toArray(
                new ChallengeViewModel.CourseItem[0]));
        list.setName("challenge.courseList");
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(4);
        if (!state.courses.isEmpty()) { list.setSelectedIndex(0); }
        JScrollPane courses = new JScrollPane(list);
        courses.setAlignmentX(Component.LEFT_ALIGNMENT);
        courses.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        content.add(courses);
        JButton select = button("Select course", "challenge.select", () -> {
            ChallengeViewModel.CourseItem selected = list.getSelectedValue();
            if (selected != null) { run(() -> controller.select(selected.id)); }
        });
        select.setVisible(!state.courses.isEmpty());
        content.add(select);
    }

    private void question(JPanel content, ChallengeViewModel.State state) {
        content.add(text(state.courseName + " — Question " + (state.questionIndex + 1)
                + " / " + state.questionCount, 1));
        JTextArea prompt = text(state.question, Math.min(9, Math.max(3, state.question.length() / 85)));
        prompt.setName("challenge.question");
        content.add(prompt);
        ButtonGroup group = new ButtonGroup();
        List<JRadioButton> answers = new ArrayList<>();
        for (int index = 0; index < state.options.size(); index++) {
            JPanel row = new JPanel(new BorderLayout(8, 0));
            JRadioButton radio = new JRadioButton(String.valueOf((char) ('A' + index)));
            radio.setName("challenge.option." + index);
            group.add(radio);
            answers.add(radio);
            row.add(radio, BorderLayout.WEST);
            row.add(text(state.options.get(index), Math.max(2, state.options.get(index).length() / 80)),
                    BorderLayout.CENTER);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
            content.add(row);
        }
        if (state.screen == Screen.QUESTION) {
            content.add(button("Submit answer", "challenge.submit", () -> {
                int selected = -1;
                for (int index = 0; index < answers.size(); index++) {
                    if (answers.get(index).isSelected()) { selected = index; }
                }
                final int answer = selected;
                run(() -> controller.answer(state.attemptId, state.questionIndex, answer));
            }));
        }
        else {
            content.add(text(state.feedback, 4));
            content.add(button(state.questionIndex + 1 == state.questionCount ? "Finish" : "Next question",
                    "challenge.next", () -> run(() -> controller.next(state.attemptId, state.questionIndex))));
        }
    }

    private static JPanel column() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    private static JTextArea text(String value, int rows) {
        int wrappedRows = 0;
        for (String line : value.split("\n", -1)) {
            wrappedRows += Math.max(1, (line.length() + 79) / 80);
        }
        JTextArea area = new JTextArea(value, Math.max(rows, wrappedRows), 50);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        area.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, area.getPreferredSize().height));
        return area;
    }

    private static JButton button(String label, String name, Runnable action) {
        JButton button = new JButton(label);
        button.setName(name);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(420, 42));
        button.addActionListener(event -> action.run());
        return button;
    }

    private static void enableControls(Container container, boolean enabled) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton || component instanceof JRadioButton || component instanceof JList) {
                component.setEnabled(enabled);
            }
            if (component instanceof Container) { enableControls((Container) component, enabled); }
        }
    }

    private static void disableAnswers(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JRadioButton) { component.setEnabled(false); }
            if (component instanceof Container) { disableAnswers((Container) component); }
        }
    }
}
