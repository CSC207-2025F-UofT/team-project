package view;
import interface_adapter.view_budget_progress.*;
import javax.swing.*;
import java.awt.*;

public class ViewBudgetProgressView extends JPanel {
    private final ViewBudgetProgressViewModel viewModel;
    private final JTextArea summary = new JTextArea(7, 35);
    private final JProgressBar progress = new JProgressBar(0, 100);
    private final JButton refresh = new JButton("Refresh");
    private final JButton back = new JButton("Back");
    private ViewBudgetProgressController controller;
    private Runnable backAction;
    public ViewBudgetProgressView(ViewBudgetProgressViewModel viewModel) {
        this.viewModel = viewModel;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        summary.setEditable(false);
        summary.setLineWrap(true);
        summary.setWrapStyleWord(true);
        add(new JLabel("Daily Budget Progress"), BorderLayout.NORTH);
        add(new JScrollPane(summary), BorderLayout.CENTER);
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(progress, BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        buttons.add(refresh);
        buttons.add(back);
        footer.add(buttons, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);
        refresh.setEnabled(false);
        back.setEnabled(false);
        refresh.addActionListener(event -> controller.execute());
        back.addActionListener(event -> backAction.run());
        viewModel.addListener(this::stateChanged);
        stateChanged();
    }
    public void setController(ViewBudgetProgressController controller) {
        this.controller = controller;
        refresh.setEnabled(controller != null);
    }
    public void setBackAction(Runnable action) {
        backAction = action;
        back.setEnabled(action != null);
    }
    private void stateChanged() {
        ViewBudgetProgressState state = viewModel.getState();
        summary.setText(state.getMessage());
        summary.setCaretPosition(0);
        summary.setForeground(state.isError() ? Color.RED : Color.BLACK);
        progress.setValue(state.getPercentage());
        progress.setVisible(!state.isError());
    }
}
