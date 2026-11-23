package main.view;

import main.interface_adapters.game.GameController;
import main.interface_adapters.game.GameViewModel;
import main.view.swing_assets.CardLabel;
import main.view.swing_assets.OpponentInfoPanel;
import main.view.swing_assets.PlayerChoicePanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameView extends JPanel implements PropertyChangeListener {

    private final GameViewModel gameViewModel;
    private GameController gameController;
    private final List<JLabel> players = new ArrayList<>();
    private final List<OpponentInfoPanel> infoPanels = new ArrayList<>();
    private final List<CardLabel> communityCardLabels = new ArrayList<>();
    private final List<JLabel> chipLabels = new ArrayList<>();

    public PlayerChoicePanel playerChoice = new PlayerChoicePanel();

    private final JLabel nameLabel;
    private final JLabel chipsHeldLabel;
    private JLabel role;

    private final CardLabel card1;
    private final CardLabel card2;

    public GameView(GameViewModel gameViewModel) {
        this.gameViewModel = gameViewModel;
        this.setLayout(null);
        gameViewModel.addPropertyChangeListener(this);
        this.setBackground(new Color(5, 5, 5));

        JLabel pokerTable = ViewUtils.createImage("/Poker_Table.png", 0.15, 0.5, 0.7);

        nameLabel = new JLabel(gameViewModel.getName());
        ViewUtils.setFont(nameLabel, "Figtree SemiBold", Font.PLAIN, 28, 0.06);
        ViewUtils.setRelativeBounds(nameLabel, 0.01, 0.78, 0.28, 0.1);
        nameLabel.setForeground(new Color(171, 221, 255));

        chipsHeldLabel = new JLabel("$" + Integer.toString(gameViewModel.getChipsHeld()));
        chipsHeldLabel.setForeground(new Color(241, 251, 255));
        ViewUtils.setFont(chipsHeldLabel, "Figtree SemiBold", Font.PLAIN, 16, 0.05);
        ViewUtils.setRelativeBounds(chipsHeldLabel, 0.01, 0.86, 0.28, 0.06);

        card1 = new CardLabel("/cards/null_card.png", "", 0.205, 0.9, 0.055);
        card2 = new CardLabel("/cards/null_card.png", "", 0.248, 0.9, 0.055);

        double[] communityX = {0.4035, 0.4375, 0.4725, 0.507, 0.5415};
        for (double xPos : communityX) {
            CardLabel communityCard = new CardLabel("/cards/null_card.png", "", xPos, 0.55, 0.045);
            communityCardLabels.add(communityCard);
            this.add(communityCard);
        }

        this.add(card1);
        this.add(card2);
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
        for (JLabel c : chipLabels) {
            this.remove(c);
        }
        chipLabels.clear();

        Random random = new Random();
        int[] denominations = {1, 5, 25, 50, 100};
        String[] prefixes = {"white_chip", "red_chip", "green_chip", "blue_chip", "black_chip"};
        double[] xPositions = {0.42, 0.45, 0.48, 0.51, 0.54};

        for (int d = 0; d < denominations.length; d++) {
            int count = gameViewModel.chips.getOrDefault(denominations[d], 0);
            for (int i = 0; i < count; i++) {
                double stackY = 0.725 - (i * 0.0035);
                String variant = Integer.toString(random.nextInt(2) + 1);
                JLabel chip = ViewUtils.createImage(
                        "/chips/" + prefixes[d] + variant + ".png",
                        xPositions[d],
                        stackY,
                        0.03
                );
                chipLabels.add(chip);
                this.add(chip);
                this.setComponentZOrder(chip, 0);
            }
        }

        this.revalidate();
        this.repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String eventName = evt.getPropertyName();

        if ("gameStart".equals(eventName)) {
            updatePlayers(gameViewModel.getPlayers());
            updatePlayerChips();
            nameLabel.setText(gameViewModel.getName());
            chipsHeldLabel.setText("$" + Integer.toString(gameViewModel.getChipsHeld()));
            if (gameController != null) {
                gameController.gameStarted();
            }
        } else if ("beginDealing".equals(eventName)) {
            beginDealing();
        } else if ("playerCardsUpdated".equals(eventName)) {
            updatePlayerCards();
        } else if ("opponentCardsUpdated".equals(eventName)) {
            int oppIdx = (int) evt.getNewValue();
            updateOpponentCards(oppIdx);
        } else if ("communityCardsUpdated".equals(eventName)) {
            updateCommunityCards();
        } else if ("turnStateChanged".equals(eventName)) {
            playerChoice.configureChips(
                    gameViewModel.chips,
                    gameViewModel.getChipValues(),
                    gameViewModel.getCurrentBetToMatch()
            );
            playerChoice.setTurnState(gameViewModel.getTurnState());
            this.setComponentZOrder(playerChoice, 0);
        } else if ("chipsUpdated".equals(eventName)) {
            updatePlayerChips();
            chipsHeldLabel.setText("$" + gameViewModel.getChipsHeld());
        } else if ("opponentChipsUpdated".equals(eventName)) {
            int oppIdx = (int) evt.getNewValue();
            if (oppIdx >= 0 && oppIdx < infoPanels.size()) {
                String newChips = gameViewModel.players.get("player" + oppIdx).get("Chips");
                infoPanels.get(oppIdx).updateChips("$" + newChips);
            }
        }
    }

    private void beginDealing() {
        int totalPlayers = gameViewModel.getPlayers();
        int dealerIdx = gameViewModel.getDealer();

        int sbIdx = (totalPlayers == 2) ? dealerIdx : (dealerIdx + 1) % totalPlayers;
        int bbIdx = (totalPlayers == 2) ? (dealerIdx + 1) % totalPlayers : (dealerIdx + 2) % totalPlayers;

        String[] seatRoles = new String[totalPlayers];
        Arrays.fill(seatRoles, "");

        seatRoles[dealerIdx] = "/blinds/dealer.png";
        seatRoles[sbIdx] = "/blinds/small_blind.png";
        seatRoles[bbIdx] = "/blinds/big_blind.png";

        if (totalPlayers == 2) {
            seatRoles[dealerIdx] = "/blinds/dealer_small_blind.png";
        }

        if (role != null) {
            this.remove(role);
            role = null;
        }
        if (!seatRoles[0].isEmpty()) {
            role = ViewUtils.createImage(seatRoles[0], 0.01, 0.915, 0.045);
            this.add(role);
            this.setComponentZOrder(role, 0);
        }

        for (int i = 0; i < infoPanels.size(); i++) {
            infoPanels.get(i).updateRole(seatRoles[i + 1]);
        }

        // 3. Update active AI opponent panels (indices 1 to totalPlayers - 1)
        for (int i = 0; i < infoPanels.size(); i++) {
            infoPanels.get(i).updateRole(seatRoles[i + 1]);
            updateOpponentCards(i);
        }

        // 4. Refresh your hole cards ("revealed") and the 5 community cards ("hidden")
        updatePlayerCards();
        updateCommunityCards();

        this.revalidate();
        this.repaint();
    }

    private void updatePlayerCards() {
        card1.setRealFilePath(gameViewModel.getPlayerCard1());
        card2.setRealFilePath(gameViewModel.getPlayerCard2());
        card1.updateStatus(gameViewModel.getPlayerCardStatus());
        card2.updateStatus(gameViewModel.getPlayerCardStatus());
        this.setComponentZOrder(card1, 0);
        this.setComponentZOrder(card2, 0);
        this.revalidate();
        this.repaint();
    }

    private void updateOpponentCards(int oppIdx) {
        if (oppIdx >= 0 && oppIdx < infoPanels.size()) {
            Map<String, String> oppData = gameViewModel.players.get("player" + oppIdx);
            if (oppData != null) {
                infoPanels.get(oppIdx).updateCards(
                        oppData.get("Card1"),
                        oppData.get("Card2"),
                        oppData.get("CardStatus")
                );
            }
        }
    }

    private void updateCommunityCards() {
        List<String> paths = gameViewModel.getCommunityCards();
        List<String> statuses = gameViewModel.getCommunityCardStatuses();
        for (int i = 0; i < communityCardLabels.size(); i++) {
            communityCardLabels.get(i).setRealFilePath(paths.get(i));
            communityCardLabels.get(i).updateStatus(statuses.get(i));
            this.setComponentZOrder(communityCardLabels.get(i), 0);
        }
        this.revalidate();
        this.repaint();
    }

    public void setController(GameController gameController) {
        this.gameController = gameController;
    }
}