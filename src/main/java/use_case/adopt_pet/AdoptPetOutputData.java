package use_case.adopt_pet;

public class AdoptPetOutputData {

    private final String petName;
    private final String species;
    private final String imageUrl;

    public AdoptPetOutputData(
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