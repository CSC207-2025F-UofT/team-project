package use_case.interact_with_pet;

public class InteractWithPetOutputData {

    private final String petName;
    private final String species;
    private final String imageUrl;
    private final int hunger;
    private final int happiness;
    private final int health;

    public InteractWithPetOutputData(
            String petName, String species, String imageUrl,
            int hunger, int happiness, int health) {
        this.petName = petName;
        this.species = species;
        this.imageUrl = imageUrl;
        this.hunger = hunger;
        this.happiness = happiness;
        this.health = health;
    }

    public String getPetName() {
        return petName;
    }

    public String getSpecies() {
        return species;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getHealth() {
        return health;
    }
}