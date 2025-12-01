package view;

import interface_adapter.results.ResultsState;
import interface_adapter.results.ResultsViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for displaying game results and statistics.
 */
public class ResultsView extends JPanel implements PropertyChangeListener {
    private final String viewName = "results";
    private final ResultsViewModel resultsViewModel;

    private final JLabel titleLabel;
    private final JLabel userNameLabel;
    private final JLabel totalMovesLabel;
    private final JLabel finalLocationLabel;
    private final JTextArea pathHistoryArea;
    private final JButton backButton;

    public ResultsView(ResultsViewModel resultsViewModel) {
        this.resultsViewModel = resultsViewModel;
        this.resultsViewModel.addPropertyChangeListener(this);

        // Set layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        titleLabel = new JLabel(ResultsViewModel.TITLE_LABEL);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User info
        userNameLabel = new JLabel();
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        totalMovesLabel = new JLabel();
        totalMovesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalMovesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        finalLocationLabel = new JLabel();
        finalLocationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        finalLocationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Path history
        JLabel pathHistoryLabel = new JLabel("Your Journey:");
        pathHistoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pathHistoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pathHistoryArea = new JTextArea(10, 30);
        pathHistoryArea.setEditable(false);
        pathHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(pathHistoryArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Back button
        backButton = new JButton(ResultsViewModel.BACK_BUTTON_LABEL);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        this.add(titleLabel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(userNameLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(totalMovesLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(finalLocationLabel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(pathHistoryLabel);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(scrollPane);
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        this.add(backButton);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final ResultsState state = (ResultsState) evt.getNewValue();
            updateView(state);
        }
    }

    private void updateView(ResultsState state) {
        userNameLabel.setText("Player: " + state.getUserName());
        totalMovesLabel.setText("Total Moves: " + state.getTotalMoves());
        finalLocationLabel.setText("Final Location: " + state.getFinalLocation());

        if (state.getPathHistory() != null && !state.getPathHistory().isEmpty()) {
            StringBuilder pathText = new StringBuilder();
            for (int i = 0; i < state.getPathHistory().size(); i++) {
                pathText.append(i + 1).append(". ").append(state.getPathHistory().get(i)).append("\n");
            }
            pathHistoryArea.setText(pathText.toString());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
