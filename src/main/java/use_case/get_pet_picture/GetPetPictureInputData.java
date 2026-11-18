package use_case.get_pet_picture;

public class GetPetPictureInputData {

    private final String species;

    public GetPetPictureInputData(String species) {
        this.species = species;
    }

    public String getSpecies() {
        return species;
    }
}