package use_case.track_applications;

/**
 * DAO interface for the pet information the Track Applications Use Case needs.
 */
public interface TrackApplicationsPetDataAccessInterface {

    /**
     * Returns the name of the pet.
     * @param petId the pet
     * @return the pet's name
     */
    String getPetName(String petId);
}
