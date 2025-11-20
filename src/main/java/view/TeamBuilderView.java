package view;

import entity.Pokemon;
import entity.Team;
import interface_adapter.team_builder.TeamBuilderController;
import interface_adapter.team_builder.TeamBuilderState;
import interface_adapter.team_builder.TeamBuilderViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class TeamBuilderView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName;
    private final TeamBuilderViewModel teamBuilderViewModel;

    private final JTextField teamNameInputField = new JTextField(15);
    private TeamBuilderController teamBuilderController = null;

    final JPanel teamDisplayPanel = new JPanel();

    private final JButton saveButton;

    public TeamBuilderView(TeamBuilderViewModel teamBuilderViewModel) {

        this.teamBuilderViewModel = teamBuilderViewModel;
        this.teamBuilderViewModel.addPropertyChangeListener(this);

        this.teamBuilderViewModel.getState().setTeam(new Team("Team 1"));

        this.viewName = teamBuilderViewModel.getViewName();

        final JLabel title = new JLabel(TeamBuilderViewModel.TITLE_LABEL);
        title.setAlignmentX(CENTER_ALIGNMENT);

        final LabelTextPanel teamNameInfo = new LabelTextPanel(
                new JLabel(TeamBuilderViewModel.TEAM_NAME_LABEL), teamNameInputField);

        final JPanel buttons = new JPanel();
        saveButton = new JButton(TeamBuilderViewModel.SAVE_BUTTON_LABEL);
        buttons.add(saveButton);

        teamDisplayPanel.setLayout(new GridLayout(3, 2, 5, 5));

        DisplayPokemonJPanel teamSlot0 = new DisplayPokemonJPanel();
        teamDisplayPanel.add(teamSlot0);
        DisplayPokemonJPanel teamSlot1 = new DisplayPokemonJPanel();
        teamDisplayPanel.add(teamSlot1);
        DisplayPokemonJPanel teamSlot2 = new DisplayPokemonJPanel();
        teamDisplayPanel.add(teamSlot2);
        DisplayPokemonJPanel teamSlot3 = new DisplayPokemonJPanel();
        teamDisplayPanel.add(teamSlot3);
        DisplayPokemonJPanel teamSlot4 = new DisplayPokemonJPanel();
        teamDisplayPanel.add(teamSlot4);
        DisplayPokemonJPanel teamSlot5 = new DisplayPokemonJPanel();
        teamDisplayPanel.add(teamSlot5);

        setTeamSlotBorders();
        addTeamSlotMouseListeners();
        updateSlotDisplays();

        saveButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(saveButton)) {
                            final TeamBuilderState currentState = teamBuilderViewModel.getState();
                            try {
                                teamBuilderController.saveTeam(currentState.getTeamName(), currentState.getTeam());
                            } catch (IOException e) {
                                JOptionPane.showMessageDialog(null, "Error saving team");
                            }

                        }
                    }
                }
        );

        addTeamNameListener();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(teamNameInfo);
        this.add(teamDisplayPanel);
        this.add(buttons);

    }

    private void updateSlotDisplays() {
        for(int i = 0; i < teamDisplayPanel.getComponentCount(); i++) {
            DisplayPokemonJPanel component = (DisplayPokemonJPanel) teamDisplayPanel.getComponent(i);
            Team team = teamBuilderViewModel.getState().getTeam();
            if (team != null) {
                component.setPokemon(team.getPokemon(i), 94, 94);
            }
        }
        teamDisplayPanel.revalidate();
        teamDisplayPanel.repaint();
    }

    private void addTeamSlotMouseListeners() {
        for(int i = 0; i < teamDisplayPanel.getComponentCount(); i++) {
            final int index = i;
            JPanel component = (JPanel) teamDisplayPanel.getComponent(i);
            component.addMouseListener(
                    new  MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {}
                        @Override
                        public void mousePressed(MouseEvent evt) {
                            teamBuilderController.switchToPokemonLookupView(index);
                            System.out.println("Pokemon Slot " + index + " clicked");
                        }
                        @Override
                        public void mouseReleased(MouseEvent e) {}
                        @Override
                        public void mouseEntered(MouseEvent e) {}
                        @Override
                        public void mouseExited(MouseEvent e) {}
                    }
            );
        }
    }

    private void setTeamSlotBorders() {
        for(int i = 0; i < teamDisplayPanel.getComponentCount(); i++) {
            JPanel component = (JPanel) teamDisplayPanel.getComponent(i);
            component.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    }

    private void addTeamNameListener() {
        teamNameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final TeamBuilderState currentState = teamBuilderViewModel.getState();
                currentState.setTeamName(teamNameInputField.getText());
                teamBuilderViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });
    }

    public void setTeamBuilderController(TeamBuilderController teamBuilderController) {
        this.teamBuilderController = teamBuilderController;
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateSlotDisplays();
    }
}
