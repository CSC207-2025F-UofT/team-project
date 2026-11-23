package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.makeNewPlayer.NewPlayerController;
import interface_adapter.makeNewPlayer.NewPlayerState;
import interface_adapter.makeNewPlayer.NewPlayerViewModel;
import interface_adapter.makeNewPlayer.NewPlayerViewModelListener;


/**
 * The view where a player enters their username.
 */
public class NewPlayerView extends JPanel implements NewPlayerViewModelListener{

    private final NewPlayerViewModel newPlayerViewModel;
    private NewPlayerController newPlayerController;

    private final JTextField usernameField = new JTextField(20);
    private final JLabel errorLabel = new JLabel();

    public NewPlayerView(NewPlayerViewModel newPlayerViewModel) {
        this.newPlayerViewModel = newPlayerViewModel;
        this.newPlayerViewModel.addListener(this);

        final JPanel welcomePanel = new JPanel();
        welcomePanel.add(new JLabel("Welcome to CityGuesser"));

        final JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);

        errorLabel.setForeground(Color.RED);
        final JPanel errorPanel = new JPanel();
        errorPanel.add(errorLabel);

        final JPanel buttonPanel = new JPanel();
        final JButton next = new JButton("Next");
        buttonPanel.add(next);

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final NewPlayerState state = newPlayerViewModel.getState();
                state.setUsername(usernameField.getText());
                state.setUsernameError(null);
                newPlayerController.execute(state.getUsername());
            }
        });

        // TODO: add View Stats once that use case exists.

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(welcomePanel);
        add(usernamePanel);
        add(errorPanel);
        add(buttonPanel);
    }

    @Override
    public void stateChanged() {
        final NewPlayerState state = newPlayerViewModel.getState();
        final String error = state.getUsernameError();
        errorLabel.setText(error == null ? "" : error);
    }

    public String getViewName() {
        return NewPlayerViewModel.VIEW_NAME;
    }

    public void setNewPlayerController(NewPlayerController newPlayerController) {
        this.newPlayerController = newPlayerController;
    }
}
