package main.interface_adapters.game;

import main.view.GameView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameViewModel {

    public static final String VIEW_NAME = "play";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final List<String> icons = new ArrayList<>();

    private int num_players;

    public GameViewModel() {
        icons.add("/Users/AI_User_Blue.png");
        icons.add("/Users/AI_User_Green.png");
        icons.add("/Users/AI_User_Orange.png");
        icons.add("/Users/AI_User_Pink.png");
        icons.add("/Users/AI_User_Purple.png");
    }

    public void setPlayers(int num) {
        int old_players = num_players;
        num_players = num;
        support.firePropertyChange("num_players", old_players, num_players);
    }

    public int getPlayers() {
        return num_players;
    }

    public List<String> generateUsers() {
        List<String> toReturn = icons;
        Collections.shuffle(toReturn);
        return(toReturn);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

}
