package main.view;

import main.interface_adapters.game.GameViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JPanel implements PropertyChangeListener {

    private final GameViewModel gameViewModel;
    private List<JLabel> players = new ArrayList<>();
    private List<Color> playerColors = new ArrayList<>();

    public GameView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
        this.setLayout(null);
        gameViewModel.addPropertyChangeListener(this);

        JLabel pokerTable = ViewUtils.createImage("/Poker_Table.png", 0.15, 0.5, 0.7);


        this.setLayout(null);
        this.setBackground(new Color(5, 5, 5));
        this.add(pokerTable);

    }

    public void updatePlayers(int totalPlayers) {
        int numOpp = totalPlayers - 1;
        players = new ArrayList<>();
        playerColors = new ArrayList<>();
        List<String> userIcons = gameViewModel.generateUsers();
        for (int i = 0; i < gameViewModel.getPlayers() - 1; i++) {
            players.add(ViewUtils.createImage(userIcons.get(i), 2, 2, 0.1));
            this.add(players.get(i));
            if (userIcons.get(i).equals("/Users/AI_User_Blue")) {
                playerColors.add(new Color(142, 170, 224));
            } else if (userIcons.get(i).equals("/Users/AI_User_Green.png")) {
                playerColors.add(new Color(142, 224, 173));
            } else if (userIcons.get(i).equals("/Users/AI_User_Orange.png")) {
                playerColors.add(new Color(224, 169, 142));
            } else if (userIcons.get(i).equals("/Users/AI_User_Pink.png")) {
                playerColors.add(new Color(224, 142, 221));
            } else {
                playerColors.add(new Color(157, 142, 224));
            }
        }
            if (gameViewModel.getPlayers() == 2) {
                ViewUtils.setRelativeLocation(players.get(0), 0.445, 0.35);
            } else if (gameViewModel.getPlayers() == 3) {
                ViewUtils.setRelativeLocation(players.get(0), 0.315, 0.37);
                ViewUtils.setRelativeLocation(players.get(1), 0.575, 0.37);
            } else if (gameViewModel.getPlayers() == 4) {
                ViewUtils.setRelativeLocation(players.get(0), 0.255, 0.38);
                ViewUtils.setRelativeLocation(players.get(1), 0.445, 0.35);
                ViewUtils.setRelativeLocation(players.get(2), 0.635, 0.38);
            } else if (gameViewModel.getPlayers() == 5) {
                ViewUtils.setRelativeLocation(players.get(0), 0.205, 0.39);
                ViewUtils.setRelativeLocation(players.get(1), 0.365, 0.36);
                ViewUtils.setRelativeLocation(players.get(2), 0.535, 0.36);
                ViewUtils.setRelativeLocation(players.get(3), 0.695, 0.39);
            } else {
                ViewUtils.setRelativeLocation(players.get(0), 0.18, 0.4);
                ViewUtils.setRelativeLocation(players.get(1), 0.31, 0.37);
                ViewUtils.setRelativeLocation(players.get(2), 0.445, 0.35);
                ViewUtils.setRelativeLocation(players.get(3), 0.58, 0.37);
                ViewUtils.setRelativeLocation(players.get(4), 0.705, 0.4);
            }
            this.revalidate();
            this.repaint();
        }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        updatePlayers(gameViewModel.getPlayers());
    }

}
