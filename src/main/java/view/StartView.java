package view;

import interface_adapter.new_game.NewGameController;

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

    public StartView(NewGameController newGameController) {
        setLayout(new GridBagLayout());
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> newGameController.execute());
        add(newGameButton);
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
