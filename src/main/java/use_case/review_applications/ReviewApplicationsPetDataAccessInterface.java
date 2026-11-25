package use_case.review_applications;

/**
 * Where the Review Applications use case reads pet names.
 */
public interface ReviewApplicationsPetDataAccessInterface {

    /**
     * Returns the name of a pet.
     * @param petId the pet's id
     * @return the pet's name
     * @throws IllegalArgumentException if no pet has this id
     */
    String getPetName(String petId);
}
