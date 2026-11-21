package main.view;

import main.interface_adapters.game.GameViewModel;

import javax.swing.*;
import java.awt.*;

public class GameView extends JPanel {

    private final GameViewModel gameViewModel;

    public GameView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;

        final JLabel title = new JLabel("Yo check this out im playing poker frfr");

        this.setLayout(null);
        this.setBackground(Color.CYAN);
        this.add(title);
    }

}
