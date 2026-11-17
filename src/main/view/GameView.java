package main.view;

import main.interface_adapters.GameViewModel;

import javax.swing.*;

public class GameView extends JPanel {

    private final GameViewModel gameViewModel;

    public GameView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;

        final JLabel title = new JLabel("Yo check this out im playing poker frfr");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
    }

}
