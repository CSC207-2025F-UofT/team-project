package use_case.interact_with_pet;

public class InteractWithPetOutputData {

    private final String petName;
    private final String species;
    private final String imageUrl;

    public InteractWithPetOutputData(
            String petName, String species, String imageUrl) {
        this.petName = petName;
        this.species = species;
        this.imageUrl = imageUrl;
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
}