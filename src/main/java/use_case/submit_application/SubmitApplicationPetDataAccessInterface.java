package use_case.submit_application;

/**
 * DAO interface for the pet information the Submit Application Use Case needs.
 */
public interface SubmitApplicationPetDataAccessInterface {

    /**
     * Checks whether the pet exists and can still be applied for.
     * @param petId the pet
     * @return true if the pet is AVAILABLE
     */
    boolean isAvailable(String petId);

    /**
     * Returns the name of the pet.
     * @param petId the pet
     * @return the pet's name
     */
    String getPetName(String petId);
}
