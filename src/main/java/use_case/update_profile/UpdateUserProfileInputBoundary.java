package use_case.update_profile;

/**
 * Input boundary for updating a user profile.
 * This interface defines all input methods that the controller can call
 * when interacting with the update-profile use case.
 */
public interface UpdateUserProfileInputBoundary {

    /**
     * Request to open the edit-profile page.
     * Controller calls this when the user chooses to edit their profile.
     */
    void openEditPage();

    /**
     * Saves the profile using a Data Transfer Object.
     *
     * @param data all new profile fields bundled together
     */
    void saveProfile(UpdateUserProfileInputData data);

    /**
     * Saves the profile using raw parameters.
     * (An alternative method used by some controllers.)
     *
     * @param name        new name
     * @param email       new email
     * @param address     new address
     * @param phoneNumber new phone number
     */
    void save(String name, String email, String address, int phoneNumber);

    /**
     * Handles the case where the phone number is invalid.
     *
     * @param message error message explaining why validation failed
     */
    void phoneNumberInvalid(String message);
}
