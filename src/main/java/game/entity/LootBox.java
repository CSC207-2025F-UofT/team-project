package game.entity;

import java.util.*;

public class LootBox extends Item{
    private List<String>dogBreedList=new ArrayList<>();
    private List<String>catBreedList=new ArrayList<>();

    public LootBox(String name,int price) {
        super(name,price,"LootBox");
    }

    public LootBox(int price){
        super("LootBox",price,"LootBox");
    }

    public Pet getPet(){
        String type;
        String breed;

        Random random = new Random();
        int typeIndex=random.nextInt(2);
        if (typeIndex==0){
            type="Cat";
            int randomIndex = random.nextInt(this.catBreedList.size());
            breed = this.catBreedList.get(randomIndex);
        }else{
            type="Dog";
            int randomIndex = random.nextInt(this.dogBreedList.size());
            breed = this.dogBreedList.get(randomIndex);
        }

        Pet randomPet=new Pet(type,breed);
        return randomPet;
    }
}
