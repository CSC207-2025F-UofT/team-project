package use_case.adopt_pet;

public class AdoptPetInputData {

    private final String username;
    private final String species;
    private final String petName;
    private final String imageUrl;

    public AdoptPetInputData(
            String username, String species,
            String petName, String imageUrl) {
        this.username = username;
        this.species = species;
        this.petName = petName;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getSpecies() {
        return species;
    }

    public String getPetName() {
        return petName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}