package entity.card;

public abstract class Card {
    private final String owner;
    private final String name;

    protected Card(String owner, String name) {
        this.owner = name;
        this.name = name;
    }

    public String getName() {return name;}
    public String getOwner() {return owner;}
}
