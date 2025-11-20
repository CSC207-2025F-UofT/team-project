package view;

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

    private final DisplayPokemonJPanel teamSlot0 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot1 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot2 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot3 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot4 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot5 = new DisplayPokemonJPanel();

    private final JButton saveButton;

    public TeamBuilderView(TeamBuilderViewModel teamBuilderViewModel) {
        this.teamBuilderViewModel = teamBuilderViewModel;
        this.teamBuilderViewModel.addPropertyChangeListener(this);

        this.viewName = teamBuilderViewModel.getViewName();

        final JLabel title = new JLabel(TeamBuilderViewModel.TITLE_LABEL);
        title.setAlignmentX(CENTER_ALIGNMENT);

        final LabelTextPanel teamNameInfo = new LabelTextPanel(
                new JLabel(TeamBuilderViewModel.TEAM_NAME_LABEL), teamNameInputField);

        final JPanel buttons = new JPanel();
        saveButton = new JButton(TeamBuilderViewModel.SAVE_BUTTON_LABEL);
        buttons.add(saveButton);

        final JPanel teamDisplayPanel = new JPanel();
        teamDisplayPanel.setLayout(new GridLayout(3, 2, 5, 5));

        teamDisplayPanel.add(teamSlot0);
        teamDisplayPanel.add(teamSlot1);
        teamDisplayPanel.add(teamSlot2);
        teamDisplayPanel.add(teamSlot3);
        teamDisplayPanel.add(teamSlot4);
        teamDisplayPanel.add(teamSlot5);

        for(int i = 0; i < teamDisplayPanel.getComponentCount(); i++) {
            JPanel component = (JPanel) teamDisplayPanel.getComponent(i);
            component.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }

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
    }
}
