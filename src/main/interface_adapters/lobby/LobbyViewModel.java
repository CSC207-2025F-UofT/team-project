package main.interface_adapters.lobby;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LobbyViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    public static final String VIEW_NAME = "lobby";

    private int num_players;

    public LobbyViewModel() {num_players = 1;}

    public void addPlayer() {
        num_players += 1;
        support.firePropertyChange("num_players", num_players - 1, num_players);
    }

    public void removePlayer() {
        num_players -= 1;
        support.firePropertyChange("num_players", num_players + 1, num_players);
    }

    public int getPlayers() {return num_players;}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
