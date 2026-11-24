package view;

import interface_adapter.create_teams.CreateTeamState;
import interface_adapter.create_teams.CreateTeamViewModel;
import interface_adapter.create_teams.CreateTeamViewModelListener;
import interface_adapter.create_teams.StartGameController;
import interface_adapter.game_running.GameRunningViewModel;
import interface_adapter.game_running.LoadQuestionController;

import javax.swing.*;
import java.awt.*;

public class CreateTeamView extends JPanel implements CreateTeamViewModelListener {
    private final CreateTeamViewModel createTeamViewModel;
    private StartGameController startGameController = null;

    private final JTextField team1Input = new JTextField(1);
    private final JTextField team2Input = new JTextField(1);

    public CreateTeamView(CreateTeamViewModel createTeamViewModel) {
        this.createTeamViewModel = createTeamViewModel;
        createTeamViewModel.addListener(this);

        this.setUpDisplay();
    }

    private void setUpDisplay() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Main Title
        final JLabel title = new JLabel("Family Feud");
        title.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 48));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Team input sections
        final JPanel teamNames = new JPanel();
        teamNames.setLayout(new BoxLayout(teamNames, BoxLayout.X_AXIS));
        teamNames.setAlignmentX(Component.CENTER_ALIGNMENT);

        final Font teamLabelFont = new Font("Bernard MT Condensed", Font.PLAIN, 36);

        teamNames.add(Box.createHorizontalGlue());
        teamNames.add(createTeamPanel("Team 1", team1Input, teamLabelFont));
        teamNames.add(Box.createRigidArea(new Dimension(40, 0))); // gap between teams
        teamNames.add(createTeamPanel("Team 2", team2Input, teamLabelFont));
        teamNames.add(Box.createHorizontalGlue());

        // Next button
        final JButton nextButton = new JButton();
        nextButton.setText("Next");
        nextButton.setFont(teamLabelFont);
        nextButton.addActionListener(
                evt -> {
                    this.startGameController.execute(team1Input.getText(), team2Input.getText());
                }
        );
        nextButton.setLayout(new BoxLayout(nextButton, BoxLayout.X_AXIS));
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);



        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20));
        this.add(teamNames);
        this.add(Box.createVerticalStrut(20));
        this.add(nextButton);
    }

    private JPanel createTeamPanel(String labelText, JTextField input, Font labelFont) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        input.setColumns(15);
        input.setMaximumSize(input.getPreferredSize());
        input.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(input);
        return panel;
    }

    @Override
    public void stateChanged() {
        // TODO: Implement stateChanged
    }

    public void setStartGameController(StartGameController startGameController) {
        this.startGameController = startGameController;
    }

}
