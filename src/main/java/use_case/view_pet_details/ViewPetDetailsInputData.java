package use_case.view_pet_details;

/**
 * Which cat the visitor wants to look at.
 */
public class ViewPetDetailsInputData {
    private final String petId;

    /**
     * Creates the request.
     *
     * @param petId the id of the cat to show
     */
    public ViewPetDetailsInputData(String petId) {
        this.petId = petId;
    }

    /**
     * Returns the id of the cat to show.
     *
     * @return the cat's id
     */
    public String getPetId() {
        return petId;
    }
}
