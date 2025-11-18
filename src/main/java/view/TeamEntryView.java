package view;

import interface_adapter.open_team_entry.OpenTeamEntryController;
import interface_adapter.open_team_entry.OpenTeamEntryState;
import interface_adapter.open_team_entry.OpenTeamEntryViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Team Entry Page of the Premier League Fantasy App
 */
public class TeamEntryView extends JPanel implements PropertyChangeListener {

    private final String viewName = "team entry";

    private final OpenTeamEntryViewModel teamEntryViewModel;
    private OpenTeamEntryController teamEntryController = null;

    private JTextField[] playerInputFields;

    private final JButton confirmButton;
    private final JButton menuButton;

    public TeamEntryView(OpenTeamEntryViewModel teamEntryViewModel) {
        this.teamEntryViewModel = teamEntryViewModel;
        teamEntryViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        // Menu button
        JPanel topMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        menuButton = new JButton(OpenTeamEntryViewModel.MENU_BUTTON_LABEL);
        topMenuPanel.add(menuButton);
        this.add(topMenuPanel, BorderLayout.NORTH);

        // Team entry panel
        JPanel teamEntryPanel = new JPanel();
        teamEntryPanel.setLayout(new BoxLayout(teamEntryPanel, BoxLayout.Y_AXIS));
        teamEntryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel header = new JLabel(teamEntryViewModel.getTitleLabel());
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        teamEntryPanel.add(header);
        teamEntryPanel.add(Box.createVerticalStrut(15));

        // Player entry rows
        String[] playerLabels = teamEntryViewModel.getPlayerLabels();
        playerInputFields = new JTextField[playerLabels.length];

        for (int i = 0; i < playerLabels.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // right-justify textfields
            row.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel label = new JLabel(playerLabels[i] + ":");
            JTextField inputField = new JTextField(15);

            row.add(label);
            row.add(inputField);

            playerInputFields[i] = inputField;
            teamEntryPanel.add(row);
        }

        // Scroll
        JPanel centeringWrapper = new JPanel(new GridBagLayout());
        centeringWrapper.add(teamEntryPanel); // this centers the whole block

        JScrollPane scrollPane = new JScrollPane(centeringWrapper);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(scrollPane, BorderLayout.CENTER);

        // Confirm button
        JPanel buttonPanel = new JPanel();
        confirmButton = new JButton(teamEntryViewModel.getConfirmButtonLabel());
        buttonPanel.add(confirmButton);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Menu button action listener
        menuButton.addActionListener(evt -> {
            if (teamEntryController != null) {
                teamEntryController.switchToHomePage();
            }
        });

        addFieldListeners();
    }

    private void addFieldListeners() {
        for (int i = 0; i < playerInputFields.length; i++) {
            final int index = i;
            playerInputFields[i].getDocument().addDocumentListener(new DocumentListener() {
                private void update() {
                    OpenTeamEntryState state = teamEntryViewModel.getState();
                    String[] players = getFieldTexts();
                    state.setPlayers(players);
                    teamEntryViewModel.setState(state);
                }
                @Override public void insertUpdate(DocumentEvent e) { update(); }
                @Override public void removeUpdate(DocumentEvent e) { update(); }
                @Override public void changedUpdate(DocumentEvent e) { update(); }
            });
        }
    }

    private String[] getFieldTexts() {
        String[] texts = new String[playerInputFields.length];
        for (int i = 0; i < playerInputFields.length; i++) {
            texts[i] = playerInputFields[i].getText();
        }
        return texts;
    }

    private void updateFieldsFromState(OpenTeamEntryState state) {
        String[] players = state.getPlayers();
        if (players != null && players.length == playerInputFields.length) {
            for (int i = 0; i < players.length; i++) {
                playerInputFields[i].setText(players[i] != null ? players[i] : "");
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        OpenTeamEntryState state = (OpenTeamEntryState) evt.getNewValue();
        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage());
        }
        updateFieldsFromState(state);
    }

    public String getViewName() {
        return viewName;
    }

    public void setTeamEntryController(OpenTeamEntryController controller) {
        this.teamEntryController = controller;
    }

}
