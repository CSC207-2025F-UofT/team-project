package main.view;

import main.interface_adapters.game.GameController;
import main.interface_adapters.game.GameViewModel;
import main.view.swing_assets.OpponentInfoPanel;
import main.view.swing_assets.PlayerChoicePanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GameView extends JPanel implements PropertyChangeListener {

    private final GameViewModel gameViewModel;
    private GameController gameController;
    private List<JLabel> players = new ArrayList<>();
    private List<Color> playerColors = new ArrayList<>();

    public PlayerChoicePanel playerChoice = new PlayerChoicePanel();

    private final JLabel nameLabel;
    private final JLabel chipsHeldLabel;
    private JLabel role;

    private JLabel card1;
    private JLabel card2;

    private final List<OpponentInfoPanel> infoPanels = new ArrayList<>();

    public GameView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
        this.setLayout(null);
        gameViewModel.addPropertyChangeListener(this);
        this.setBackground(new Color(5, 5, 5));

        JLabel pokerTable = ViewUtils.createImage("/Poker_Table.png", 0.15, 0.5, 0.7);

        nameLabel = new JLabel(gameViewModel.getName());
        ViewUtils.setFont(nameLabel, "Figtree SemiBold", Font.PLAIN, 28, 0.06);
        ViewUtils.setRelativeBounds(nameLabel, 0.01, 0.78 , 0.28, 0.1);
        nameLabel.setForeground(new Color(171, 221, 255));

        chipsHeldLabel = new JLabel("$" + gameViewModel.getChipsHeld());
        chipsHeldLabel.setForeground(new Color(241, 251, 255));
        ViewUtils.setFont(chipsHeldLabel, "Figtree SemiBold", Font.PLAIN, 16, 0.05);
        ViewUtils.setRelativeBounds(chipsHeldLabel, 0.01, 0.86, 0.28, 0.06);

        revealFlop("/cards/card_clubs_04.png", "/cards/card_diamonds_12.png", "/cards/card_hearts_13.png");
        revealTurn("/cards/card_spades_09.png");
        revealRiver("/cards/card_diamonds_10.png");


        this.add(playerChoice);
        this.add(nameLabel);
        this.add(chipsHeldLabel);
        this.add(pokerTable);
    }

    public void updatePlayers(int totalPlayers) {
        for (JLabel p : players) {
            this.remove(p);
        }
        for (OpponentInfoPanel panel : infoPanels) {
            this.remove(panel);
        }
        players.clear();
        infoPanels.clear();
        playerColors.clear();

        int numOpp = totalPlayers - 1;
        List<String> userIcons = gameViewModel.generateUsers();

        double[][] avatarCoords = {
                {},
                {0.445},
                {0.315, 0.575},
                {0.255, 0.445, 0.635},
                {0.205, 0.365, 0.535, 0.695},
                {0.180, 0.310, 0.445, 0.580, 0.705}
        };
        double[][] avatarY = {
                {},
                {0.35},
                {0.37, 0.37},
                {0.38, 0.35, 0.38},
                {0.39, 0.36, 0.36, 0.39},
                {0.40, 0.37, 0.35, 0.37, 0.40}
        };
        double[][] panelX = {
                {},
                {0.425},
                {0.295, 0.555},
                {0.235, 0.425, 0.615},
                {0.175, 0.345, 0.515, 0.685},
                {0.085, 0.255, 0.425, 0.595, 0.765}
        };

        for (int i = 0; i < numOpp; i++) {
            JLabel avatar = ViewUtils.createImage(userIcons.get(i), 2, 2, 0.1);
            ViewUtils.setRelativeLocation(avatar, avatarCoords[numOpp][i], avatarY[numOpp][i]);
            players.add(avatar);
            this.add(avatar);

            OpponentInfoPanel panel = new OpponentInfoPanel(gameViewModel, i + 1, panelX[numOpp][i]);
            infoPanels.add(panel);
            this.add(panel);
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
        nameLabel.setText(gameViewModel.getName());
        chipsHeldLabel.setText(gameViewModel.getChipsHeld());
        if (evt.getPropertyName().equals("gameStart")) {
            gameController.gameStarted();
        }
        if (evt.getPropertyName().equals("beginDealing")) {
            beginDealing();
        }
    }

    private void beginDealing() {
        int totalPlayers = gameViewModel.getPlayers();
        int dealerIdx = gameViewModel.getDealer();

        int sbIdx = (totalPlayers == 2) ? dealerIdx : (dealerIdx + 1) % totalPlayers;
        int bbIdx = (totalPlayers == 2) ? (dealerIdx + 1) % totalPlayers : (dealerIdx + 2) % totalPlayers;

        // 1. Assign roles for all seats (0 = you, 1..totalPlayers-1 = AI opponents)
        String[] seatRoles = new String[totalPlayers];
        Arrays.fill(seatRoles, "");

        seatRoles[dealerIdx] = "/blinds/dealer.png";
        seatRoles[sbIdx] = "/blinds/small_blind.png";
        seatRoles[bbIdx] = "/blinds/big_blind.png";

        if (totalPlayers == 2) {
            seatRoles[dealerIdx] = "/blinds/dealer_small_blind.png";
        }

        // 2. Update your own role icon (index 0)
        if (role != null) {
            this.remove(role);
        }
        if (!seatRoles[0].isEmpty()) {
            role = ViewUtils.createImage(seatRoles[0], 0.01, 0.915, 0.045);
            this.add(role);
            this.setComponentZOrder(role, 0);
        }

        // 3. Update active AI opponent panels (indices 1 to totalPlayers - 1)
        for (int i = 0; i < infoPanels.size(); i++) {
            infoPanels.get(i).updateRole(seatRoles[i + 1]);
        }

        this.revalidate();
        this.repaint();

    }

    public void revealFlop(String card1, String card2, String card3) {
        JLabel flop1 = ViewUtils.createImage(card1, 0.4105, 0.55, 0.045);
        JLabel flop2 = ViewUtils.createImage(card2, 0.4415, 0.55, 0.045);
        JLabel flop3 = ViewUtils.createImage(card3, 0.4725, 0.55, 0.045);
        this.add(flop1);
        this.add(flop2);
        this.add(flop3);
        this.setComponentZOrder(flop1, 0);
        this.setComponentZOrder(flop2, 0);
        this.setComponentZOrder(flop3, 0);
        this.revalidate();
        this.repaint();
    }

    public void revealTurn(String card) {
        JLabel turn1 = ViewUtils.createImage(card, 0.5035, 0.55, 0.045);
        this.add(turn1);
        this.setComponentZOrder(turn1, 0);
        this.revalidate();
        this.repaint();
    }

    public void revealRiver(String card) {
        JLabel river1 = ViewUtils.createImage(card, 0.5345, 0.55, 0.045);
        this.add(river1);
        this.setComponentZOrder(river1, 0);
        this.revalidate();
        this.repaint();
    }

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }
    /*
    public void playerTurn() {
        JPanel choicePanel = new JPanel();
        choicePanel.set


    }*/
}
