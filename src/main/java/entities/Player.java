package entities;

public class Player {
    private final String name;
    private int deckSize;

    public Player(String name){
        this.name = name;
        this.deckSize = 0;
    }

    public String getName(){
        return name;
    }

    public int getDeckSize(){
        return deckSize;
    }

    public void setDeckSize(int deckSize){
        this.deckSize = deckSize;
    }
}
