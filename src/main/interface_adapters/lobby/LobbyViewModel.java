package main.interface_adapters.lobby;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class LobbyViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public static final String VIEW_NAME = "lobby";

    private int num_players;
    private int minBet;
    public final List<Integer> betAmounts = new ArrayList<>();

    public LobbyViewModel() {
        num_players = 2;
        betAmounts.add(10);
        betAmounts.add(25);
        betAmounts.add(50);
        betAmounts.add(100);
        betAmounts.add(200);
        betAmounts.add(500);
        betAmounts.add(1000);
        minBet = betAmounts.get(0);
    }

    public void addPlayer() {
        num_players += 1;
        support.firePropertyChange("num_players", num_players - 1, num_players);
    }

    public void removePlayer() {
        num_players -= 1;
        support.firePropertyChange("num_players", num_players + 1, num_players);
    }

    public int getPlayers() {return num_players;}

    public void increaseMinBet() {
        int oldAmount = minBet;
        minBet = betAmounts.get(betAmounts.indexOf(minBet) + 1);
        support.firePropertyChange("minBet", oldAmount, minBet);
    }

    public void decreaseMinBet() {
        int oldAmount = minBet;
        minBet = betAmounts.get(betAmounts.indexOf(minBet) - 1);
        support.firePropertyChange("minBet", oldAmount, minBet);
    }
    
    public int getMinBet() {return minBet;}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
