package view;

import interface_adapter.new_game.NewGameController;
import interface_adapter.logout.LogoutController;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TitleView extends JPanel {

    public static final String VIEW_NAME = "title";

    private LogoutController logoutController;

    public TitleView(NewGameController newGameController) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("UofTears: Survive the Semester");
        add(titleLabel, gbc);

        gbc.gridy = 1;
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(
                e -> newGameController.execute()
        );
        add(newGameButton, gbc);

        gbc.gridy = 2;
        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.setEnabled(false);
        loadGameButton.setToolTipText(
                "Available after Story 3 integration"
        );
        add(loadGameButton, gbc);

        gbc.gridy = 3;
        JButton logoutButton = new JButton("Log out");
        logoutButton.addActionListener(e -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });
        add(logoutButton, gbc);
    }

    public void setLogoutController(
            LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
