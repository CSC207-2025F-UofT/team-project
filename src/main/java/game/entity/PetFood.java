package game.entity;

public class PetFood extends Item{
    private int price;
    private int energyIncrease;
    public PetFood(int price,int energyIncrease) {
        super(price);
        this.energyIncrease=energyIncrease;
    }

    public int getEnergyIncrease() {
        return energyIncrease;
    }
}
