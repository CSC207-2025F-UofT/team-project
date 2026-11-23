package view;

import interface_adapter.new_game.NewGameController;
import interface_adapter.logout.LogoutController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * TEMPORARY first screen with a single New Game button, so Story 2 can be
 * demoed on its own. Story 1's TitleView replaces it and calls the same
 * NewGameController.
 */
public class StartView extends JPanel {

    public static final String VIEW_NAME = "start";

    private LogoutController logoutController;

    public StartView(NewGameController newGameController) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGameController.execute());
        add(newGameButton, gbc);

        gbc.gridy = 1;

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });
        add(logoutButton, gbc);
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
