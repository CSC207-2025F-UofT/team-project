package game.entities;

public class PetToy extends Item{
    private int price;
    private int affectionIncrease;
    public PetToy(int price,int affectionIncrease) {
        super(price);
        this.affectionIncrease=affectionIncrease;
    }

    public int getAffectionIncrease() {
        return affectionIncrease;
    }
}
