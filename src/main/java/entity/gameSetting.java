package entity;

public class gameSetting {
    private int numBots;
    private int cardsDealt;


    public gameSetting(int numBots, int cardsDealt) {
        this.numBots = numBots;
        this.cardsDealt = cardsDealt;
    }

    public int getNumBots() {
        return numBots;
    }

    public void setNumBots(int numBots) {
        this.numBots = numBots;
    }

    public int getCardsDealt() {
        return cardsDealt;
    }

    public void setCardsDealt(int cardsDealt) {
        this.cardsDealt = cardsDealt;
    }
}
