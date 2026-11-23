package main.interface_adapters.game;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;

public class GameViewModel {

    public static final String VIEW_NAME = "play";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final List<String> icons = new ArrayList<>();
    private List<Color> playerColors;
    public final Map<String, Map<String, String>> players = new HashMap<>();

    private String turnState = "none"; // "none", "unopened", or "facingBet"
    private int currentBetToMatch = 0;

    // Human player's hole cards
    private String playerCard1 = "/cards/null_card.png";
    private String playerCard2 = "/cards/null_card.png";
    private String playerCardStatus = "";

    // Community cards: index 0-2 = flop, 3 = turn, 4 = river
    private final List<String> communityCards = new ArrayList<>(
            Arrays.asList("/cards/null_card.png", "/cards/null_card.png", "/cards/null_card.png",
                    "/cards/null_card.png", "/cards/null_card.png")
    );
    private final List<String> communityCardStatuses = new ArrayList<>(
            Arrays.asList("", "", "", "", "")
    );

    private String name;
    private int chipsHeld;
    private int num_players;
    private int dealer;
    public final Map<Integer, Integer> chips = new HashMap<>();
    private final List<Integer> chipValues = new ArrayList<>();

    public GameViewModel() {
        icons.add("/Users/AI_User_Blue.png");
        icons.add("/Users/AI_User_Green.png");
        icons.add("/Users/AI_User_Orange.png");
        icons.add("/Users/AI_User_Pink.png");
        icons.add("/Users/AI_User_Purple.png");
    }

    public void startGame() {
        if (num_players == 0 || chips.isEmpty() || chipValues.isEmpty() || players.isEmpty()) {
            return;
        }

        int[] denominations = {1, 5, 25, 50, 100};
        int tempChips = 0;
        for (int i = 0; i < denominations.length; i++) {
            int chipCount = chips.getOrDefault(denominations[i], 0);
            tempChips += chipCount * chipValues.get(i);
        }
        chipsHeld = tempChips;

        if (chipsHeld == 0) {
            return;
        }

        // Sync every opponent's starting stack to match the player's starting chipsHeld
        String startingStack = Integer.toString(chipsHeld);
        for (int i = 0; i < 6; i++) {
            Map<String, String> oppInfo = players.get("player" + i);
            if (oppInfo != null) {
                oppInfo.put("Chips", startingStack);
            }
        }

        support.firePropertyChange("gameStart", null, null);
    }

    public void beginDealing() {
        support.firePropertyChange("beginDealing", null, null);
    }

    public void setPlayers(int num) {
        num_players = num;
    }

    public int getPlayers() {
        return num_players;
    }

    public List<String> generateUsers() {
        List<String> toReturn = new ArrayList<>(icons);
        Collections.shuffle(toReturn);
        savePlayerColors(toReturn);
        return toReturn;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    private void savePlayerColors(List<String> colors) {
        playerColors = new ArrayList<>();
        for (String colorPath : colors) {
            if (colorPath.equals("/Users/AI_User_Blue.png")) {
                playerColors.add(new Color(142, 170, 224));
            } else if (colorPath.equals("/Users/AI_User_Green.png")) {
                playerColors.add(new Color(142, 224, 173));
            } else if (colorPath.equals("/Users/AI_User_Orange.png")) {
                playerColors.add(new Color(224, 169, 142));
            } else if (colorPath.equals("/Users/AI_User_Pink.png")) {
                playerColors.add(new Color(224, 142, 221));
            } else {
                playerColors.add(new Color(157, 142, 224));
            }
        }
    }

    public Color getPlayerColors(int idx) {
        return this.playerColors.get(idx);
    }

    public void saveOpponentData(List<String> names, List<String> chips) {
        for (int i = 0; i < 6; i++) {
            Map<String, String> playerInfo = new HashMap<>();
            playerInfo.put("Name", names.get(i));
            playerInfo.put("Chips", chips.get(i));
            playerInfo.put("Card1", "/cards/null_card.png");
            playerInfo.put("Card2", "/cards/null_card.png");
            playerInfo.put("CardStatus", "");
            players.put("player" + i, playerInfo);
        }
    }

    // --- Opponent Card Methods ---
    public void setOpponentCards(int opponentIndex, String card1Path, String card2Path, String status) {
        Map<String, String> playerInfo = players.get("player" + opponentIndex);
        if (playerInfo != null) {
            playerInfo.put("Card1", card1Path);
            playerInfo.put("Card2", card2Path);
            playerInfo.put("CardStatus", status);
            support.firePropertyChange("opponentCardsUpdated", null, opponentIndex);
        }
    }

    public void setOpponentCardStatus(int opponentIndex, String status) {
        Map<String, String> playerInfo = players.get("player" + opponentIndex);
        if (playerInfo != null) {
            playerInfo.put("CardStatus", status);
            support.firePropertyChange("opponentCardsUpdated", null, opponentIndex);
        }
    }

    // --- Human Player Card Methods ---
    public void setPlayerCards(String card1Path, String card2Path, String status) {
        this.playerCard1 = card1Path;
        this.playerCard2 = card2Path;
        this.playerCardStatus = status;
        support.firePropertyChange("playerCardsUpdated", null, null);
    }

    public void setPlayerCardStatus(String status) {
        this.playerCardStatus = status;
        support.firePropertyChange("playerCardsUpdated", null, null);
    }

    public String getPlayerCard1() {
        return playerCard1;
    }

    public String getPlayerCard2() {
        return playerCard2;
    }

    public String getPlayerCardStatus() {
        return playerCardStatus;
    }

    // --- Community Card Methods (Flop, Turn, River) ---
    public void setCommunityCardsHidden(List<String> cards) {
        for (int i = 0; i < 5; i++) {
            communityCards.set(i, cards.get(i));
            communityCardStatuses.set(i, "hidden");
        }
        support.firePropertyChange("communityCardsUpdated", null, "dealt");
    }

    public void setFlop(String card1, String card2, String card3) {
        communityCards.set(0, card1);
        communityCards.set(1, card2);
        communityCards.set(2, card3);
        communityCardStatuses.set(0, "revealed");
        communityCardStatuses.set(1, "revealed");
        communityCardStatuses.set(2, "revealed");
        support.firePropertyChange("communityCardsUpdated", null, "flop");
    }

    public void setTurn(String card) {
        communityCards.set(3, card);
        communityCardStatuses.set(3, "revealed");
        support.firePropertyChange("communityCardsUpdated", null, "turn");
    }

    public void setRiver(String card) {
        communityCards.set(4, card);
        communityCardStatuses.set(4, "revealed");
        support.firePropertyChange("communityCardsUpdated", null, "river");
    }

    public void resetCommunityCards() {
        for (int i = 0; i < 5; i++) {
            communityCards.set(i, "/cards/null_card.png");
            communityCardStatuses.set(i, "");
        }
        support.firePropertyChange("communityCardsUpdated", null, "reset");
    }

    public List<String> getCommunityCards() {
        return communityCards;
    }

    public List<String> getCommunityCardStatuses() {
        return communityCardStatuses;
    }

    // --- Chip & Betting Methods ---
    public void setChipValues(int minBet) {
        chipValues.clear();
        chipValues.add(minBet);
        chipValues.add(minBet * 5);
        chipValues.add(minBet * 25);
        chipValues.add(minBet * 50);
        chipValues.add(minBet * 100);
    }

    public List<Integer> getChipValues() {
        return chipValues;
    }

    public int getCurrentBetToMatch() {
        return currentBetToMatch;
    }

    public void setCurrentBetToMatch(int bet) {
        this.currentBetToMatch = bet;
    }

    public void deductPlayerChips(Map<Integer, Integer> betChips, int totalAmount) {
        for (Map.Entry<Integer, Integer> entry : betChips.entrySet()) {
            int denom = entry.getKey();
            int used = entry.getValue();
            int remaining = Math.max(0, chips.getOrDefault(denom, 0) - used);
            chips.put(denom, remaining);
        }
        chipsHeld = Math.max(0, chipsHeld - totalAmount);
        support.firePropertyChange("chipsUpdated", null, null);
    }

    public void deductOpponentChips(int opponentIndex, int amount) {
        Map<String, String> oppInfo = players.get("player" + opponentIndex);
        if (oppInfo != null) {
            int current = Integer.parseInt(oppInfo.get("Chips"));
            int updated = Math.max(0, current - amount);
            oppInfo.put("Chips", Integer.toString(updated));
            support.firePropertyChange("opponentChipsUpdated", null, opponentIndex);
        }
    }

    // --- Player & Table Metadata ---
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getChipsHeld() {
        return chipsHeld;
    }

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int newDealer) {
        dealer = newDealer;
    }

    // --- Turn State ---
    public void setTurnState(String newState) {
        String oldState = this.turnState;
        this.turnState = newState;
        support.firePropertyChange("turnStateChanged", oldState, this.turnState);
    }

    public String getTurnState() {
        return turnState;
    }

    public void playerActionSubmitted() {
        setTurnState("none");
    }
}