package entity.card;

import entity.Player;

public abstract class Card {
    private final Player owner;
    private final String name;

    protected Card(Player owner, String name) {
        this.owner = owner;
        this.name = name; //name is the name of the card
    }

    public String getName() {return name;}
    public Player getOwner() {return owner;}
}
