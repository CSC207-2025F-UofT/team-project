package use_case.manage_pet;

/** Admin actions on pet listings. */
public interface ManagePetInputBoundary {

    /** Loads the current listings. */
    void refresh();

    /**
     * Publishes a pet.
     * @param inputData the new profile
     */
    void add(ManagePetInputData inputData);

    /**
     * Updates a pet's profile.
     * @param petId the selected pet
     * @param inputData the revised profile
     */
    void edit(String petId, ManagePetInputData inputData);

    /**
     * Removes a pet's listing.
     * @param petId the selected pet
     */
    void remove(String petId);
}
