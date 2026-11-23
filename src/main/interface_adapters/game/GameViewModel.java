package main.interface_adapters.game;

import main.view.GameView;

import java.awt.*;
import java.beans.PropertyChangeEvent;
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

    public String playerTurn;
    private String name;
    private String chipsHeld;
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
        if (num_players == 0) {
            return;
        } else if (chips.equals(new HashMap<>())) {
            return;
        } else if (chipValues.isEmpty()) {
            return;
        } else if (chipsHeld == null) {
            return;
        } else if (players.isEmpty()) {
            return;
        }
        support.firePropertyChange("gameStart", null, null);
    }

    public void beginDealing() {
        support.firePropertyChange("beginDealing", null, null);
    }

    public void setPlayers(int num) {
        int old_players = num_players;
        num_players = num;
    }

    public int getPlayers() {
        return num_players;
    }

    public List<String> generateUsers() {
        List<String> toReturn = icons;
        Collections.shuffle(toReturn);
        savePlayerColors(toReturn);
        return(toReturn);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
    
    private void savePlayerColors(List<String> colors) {
        playerColors = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).equals("/Users/AI_User_Blue.png")) {
                playerColors.add(new Color(142, 170, 224));
            } else if (colors.get(i).equals("/Users/AI_User_Green.png")) {
                playerColors.add(new Color(142, 224, 173));
            } else if (colors.get(i).equals("/Users/AI_User_Orange.png")) {
                playerColors.add(new Color(224, 169, 142));
            } else if (colors.get(i).equals("/Users/AI_User_Pink.png")) {
                playerColors.add(new Color(224, 142, 221));
            } else {
                playerColors.add(new Color(157, 142, 224));
            }
        }

        
    }

    public Color getPlayerColors(int idx) {return this.playerColors.get(idx);}

    public void saveOpponentData(List<String> names, List<String> chips) {
        for (int i = 0; i < 6; i++) {
            Map<String, String> playerInfo = new HashMap<>();
            playerInfo.put("Name", names.get(i));
            playerInfo.put("Chips", chips.get(i));
            playerInfo.put("Card1", "/cardHolders/blank_card");
            playerInfo.put("Card2", "/cardHolders/blank_card");
            players.put("player" + Integer.toString(i), playerInfo);
        }

    }

    public void setChipValues(int minBet) {
        chipValues.add(minBet);
        chipValues.add(minBet * 5);
        chipValues.add(minBet * 25);
        chipValues.add(minBet * 50);
        chipValues.add(minBet * 100);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return(name);
    }

    public void setChipsHeld(String chips) {
        this.chipsHeld = chips;
    }

    public String getChipsHeld() {
        return(chipsHeld);
    }

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int newDealer) {
        dealer = newDealer;
    }

    public void playerCheck() {
        String old_value = playerTurn;
        playerTurn = "false";
        support.firePropertyChange("playerTurn", old_value, playerTurn);
    }
}
