package view;

import interface_adapter.hud.HudViewModel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * The main in-game screen. In Story 2 it shows only the stats panel;
 * later stories fill the centre (map, location, dialogue).
 */
public class GameView extends JPanel {

    public static final String VIEW_NAME = "game";

    public GameView(HudViewModel hudViewModel) {
        setLayout(new BorderLayout());
        add(new StatsPanel(hudViewModel), BorderLayout.NORTH);
        add(new JLabel("Your week at UofT begins.", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
