package main.view;

import main.interface_adapters.game.GameViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
        }
        if (gameViewModel.getPlayers() == 2) {
            ViewUtils.setRelativeLocation(players.get(0), 0.445, 0.35);
            JPanel player1Info = new OpponentInfoPanel(gameViewModel, 1, 0.425);
            this.add(player1Info);
        } else if (gameViewModel.getPlayers() == 3) {
            ViewUtils.setRelativeLocation(players.get(0), 0.315, 0.37);
            ViewUtils.setRelativeLocation(players.get(1), 0.575, 0.37);
            JPanel player1Info = new OpponentInfoPanel(gameViewModel, 1, 0.295);
            JPanel player2Info = new OpponentInfoPanel(gameViewModel, 2, 0.555);
            this.add(player1Info);
            this.add(player2Info);
        } else if (gameViewModel.getPlayers() == 4) {
            ViewUtils.setRelativeLocation(players.get(0), 0.255, 0.38);
            ViewUtils.setRelativeLocation(players.get(1), 0.445, 0.35);
            ViewUtils.setRelativeLocation(players.get(2), 0.635, 0.38);
            JPanel player1Info = new OpponentInfoPanel(gameViewModel, 1, 0.235);
            JPanel player2Info = new OpponentInfoPanel(gameViewModel, 2, 0.425);
            JPanel player3Info = new OpponentInfoPanel(gameViewModel, 3, 0.615);
            this.add(player1Info);
            this.add(player2Info);
            this.add(player3Info);
        } else if (gameViewModel.getPlayers() == 5) {
            ViewUtils.setRelativeLocation(players.get(0), 0.205, 0.39);
            ViewUtils.setRelativeLocation(players.get(1), 0.365, 0.36);
            ViewUtils.setRelativeLocation(players.get(2), 0.535, 0.36);
            ViewUtils.setRelativeLocation(players.get(3), 0.695, 0.39);
            JPanel player1Info = new OpponentInfoPanel(gameViewModel, 1, 0.175);
            JPanel player2Info = new OpponentInfoPanel(gameViewModel, 2, 0.345);
            JPanel player3Info = new OpponentInfoPanel(gameViewModel, 3, 0.515);
            JPanel player4Info = new OpponentInfoPanel(gameViewModel, 4, 0.685);
            this.add(player1Info);
            this.add(player2Info);
            this.add(player3Info);
            this.add(player4Info);
        } else {
            ViewUtils.setRelativeLocation(players.get(0), 0.18, 0.4);
            ViewUtils.setRelativeLocation(players.get(1), 0.31, 0.37);
            ViewUtils.setRelativeLocation(players.get(2), 0.445, 0.35);
            ViewUtils.setRelativeLocation(players.get(3), 0.58, 0.37);
            ViewUtils.setRelativeLocation(players.get(4), 0.705, 0.4);
            JPanel player1Info = new OpponentInfoPanel(gameViewModel, 1, 0.085);
            JPanel player2Info = new OpponentInfoPanel(gameViewModel, 2, 0.255);
            JPanel player3Info = new OpponentInfoPanel(gameViewModel, 3, 0.425);
            JPanel player4Info = new OpponentInfoPanel(gameViewModel, 4, 0.595);
            JPanel player5Info = new OpponentInfoPanel(gameViewModel, 5, 0.765);
            this.add(player1Info);
            this.add(player2Info);
            this.add(player3Info);
            this.add(player4Info);
            this.add(player5Info);
        }
        this.revalidate();
        this.repaint();
    }

    public void updatePlayerChips() {
        Random random = new Random();
        for (int i = 0; i < gameViewModel.chips.get(1); i++) {
            double stackY = 0.725 - (i * 0.0035);
            String variant = Integer.toString(random.nextInt (2) + 1);
            JLabel oneChip = ViewUtils.createImage("/chips/white_chip" + variant + ".png", 0.42, stackY, 0.03);
            this.add(oneChip);
            this.setComponentZOrder(oneChip, 0);
        }
        for (int i = 0; i < gameViewModel.chips.get(5); i++) {
            double stackY = 0.725 - (i * 0.0035);
            String variant = Integer.toString(random.nextInt(2) + 1);
            JLabel fiveChip = ViewUtils.createImage("/chips/red_chip" + variant + ".png", 0.45, stackY, 0.03);
            this.add(fiveChip);
            this.setComponentZOrder(fiveChip, 0);
        }
        for (int i = 0; i < gameViewModel.chips.get(25); i++) {
            double stackY = 0.725 - (i * 0.0035);
            String variant = Integer.toString(random.nextInt(2) + 1);
            JLabel twentyFiveChip = ViewUtils.createImage("/chips/green_chip" + variant + ".png", 0.48, stackY, 0.03);
            this.add(twentyFiveChip);
            this.setComponentZOrder(twentyFiveChip, 0);
        }
        for (int i = 0; i < gameViewModel.chips.get(50); i++) {
            double stackY = 0.725 - (i * 0.0035);
            String variant = Integer.toString(random.nextInt(2) + 1);
            JLabel fiftyChip = ViewUtils.createImage("/chips/blue_chip" + variant + ".png", 0.51, stackY, 0.03);
            this.add(fiftyChip);
            this.setComponentZOrder(fiftyChip, 0);
        }
        for (int i = 0; i < gameViewModel.chips.get(100); i++) {
            double stackY = 0.725 - (i * 0.0035);
            String variant = Integer.toString(random.nextInt(2) + 1);
            JLabel hundredChip = ViewUtils.createImage("/chips/black_chip" + variant + ".png", 0.54, stackY, 0.03);
            this.add(hundredChip);
            this.setComponentZOrder(hundredChip, 0);
        }
        this.revalidate();
        this.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        updatePlayers(gameViewModel.getPlayers());
        updatePlayerChips();
    }

}
