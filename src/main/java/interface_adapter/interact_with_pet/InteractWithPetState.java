package interface_adapter.interact_with_pet;

public class InteractWithPetState {

    private String petName = "";
    private String species = "";
    private String imageUrl = "";
    private int hunger;
    private int happiness;
    private int health;

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getHunger() { return hunger; }

    public void setHunger(int hunger) { this.hunger = hunger; }

    public int getHappiness() { return happiness; }

    public void setHappiness(int happiness) { this.happiness = happiness; }

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

}