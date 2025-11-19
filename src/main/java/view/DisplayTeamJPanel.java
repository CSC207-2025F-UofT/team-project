package view;

import javax.swing.*;
import java.awt.*;

public class DisplayTeamJPanel extends JPanel {
    
    private final DisplayPokemonJPanel teamSlot1 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot2 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot3 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot4 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot5 = new DisplayPokemonJPanel();
    private final DisplayPokemonJPanel teamSlot6 = new DisplayPokemonJPanel();
    
    DisplayTeamJPanel() {
        super();
        this.setLayout(new GridLayout(2, 2, 10, 5));
        teamSlot1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        teamSlot2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        teamSlot3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        teamSlot4.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        teamSlot5.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        teamSlot6.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.add(teamSlot1);
        this.add(teamSlot2);
        this.add(teamSlot3);
        this.add(teamSlot4);
        this.add(teamSlot5);
        this.add(teamSlot6);
        this.add(teamSlot1);
    }
}
