package view;

import interface_adapter.leaderboard.LeaderboardController;
import interface_adapter.save.SaveController;
import interface_adapter.save.SaveState;
import interface_adapter.save.SaveViewModel;
import interface_adapter.save.SaveViewModelListener;
import interface_adapter.load.LoadController;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;

/**
 * Test screen for Use Case 4: type a username and floor, save, and see the leaderboard.
 */
public class SaveLeaderboardView extends JPanel implements SaveViewModelListener {
    private final SaveViewModel viewModel;
    private SaveController saveController;
    private LeaderboardController leaderboardController;
    private LoadController loadController;

    private final JTextField usernameField = new JTextField(15);
    private final JTextField floorField = new JTextField(5);
    private final JButton saveButton = new JButton("Save");
    private final JButton leaderboardButton = new JButton("Show Leaderboard");
    private final JButton loadButton = new JButton("Load");
    private final JLabel messageLabel = new JLabel(" ");
    private final JTextArea leaderboardArea = new JTextArea(10, 25);

    public SaveLeaderboardView(SaveViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);   // "tell me when the state changes"

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Save & Leaderboard");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel usernameRow = new JPanel();
        usernameRow.add(new JLabel("Username"));
        usernameRow.add(usernameField);

        JPanel floorRow = new JPanel();
        floorRow.add(new JLabel("Floor"));
        floorRow.add(floorField);

        JPanel buttons = new JPanel();
        buttons.add(saveButton);
        buttons.add(leaderboardButton);
        buttons.add(loadButton);

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardArea.setEditable(false);

        saveButton.addActionListener(evt -> onSaveClicked());
        leaderboardButton.addActionListener(evt -> leaderboardController.execute());
        loadButton.addActionListener(evt -> loadController.execute(usernameField.getText().trim()));

        add(title);
        add(usernameRow);
        add(floorRow);
        add(buttons);
        add(messageLabel);
        add(new JScrollPane(leaderboardArea));
    }

    private void onSaveClicked() {
        int floor;
        try {
            floor = Integer.parseInt(floorField.getText().trim());
        }
        catch (NumberFormatException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Floor must be a whole number.");
            return;
        }
        saveController.execute(usernameField.getText().trim(), floor);
        leaderboardController.execute();   // refresh the list after saving
    }

    public void setSaveController(SaveController saveController) {
        this.saveController = saveController;
    }

    public void setLeaderboardController(LeaderboardController leaderboardController) {
        this.leaderboardController = leaderboardController;
    }

    public void setLoadController(LoadController loadController) {
        this.loadController = loadController;
    }

    /** Called by the view model whenever a presenter changes the state. */
    @Override
    public void stateChanged() {
        SaveState state = viewModel.getState();
        messageLabel.setText(state.getMessage());
        if (state.isError()) {
            messageLabel.setForeground(Color.RED);
        }
        else {
            messageLabel.setForeground(new Color(0, 128, 0));   // green
        }
        leaderboardArea.setText(String.join("\n", state.getLeaderboardLines()));
    }
}