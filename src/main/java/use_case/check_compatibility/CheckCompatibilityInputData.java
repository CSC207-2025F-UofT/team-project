package use_case.check_compatibility;

/**
 * Which cat to check, and whose saved preferences to check it against.
 */
public class CheckCompatibilityInputData {
    private final String petId;
    private final String adopterId;

    /**
     * Creates the input.
     *
     * @param petId the cat being viewed
     * @param adopterId the adopter whose saved preferences are used
     */
    public CheckCompatibilityInputData(String petId, String adopterId) {
        this.petId = petId;
        this.adopterId = adopterId;
    }

    /**
     * Returns the cat's id.
     *
     * @return the pet id
     */
    public String getPetId() {
        return petId;
    }

    /**
     * Returns the adopter's id.
     *
     * @return the adopter id
     */
    public String getAdopterId() {
        return adopterId;
    }
}
