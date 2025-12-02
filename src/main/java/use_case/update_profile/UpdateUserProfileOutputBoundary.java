package use_case.update_profile;

/**
 * Output boundary for the Update Profile use case.
 * Defines how the interactor communicates results to the presenter.
 */
public interface UpdateUserProfileOutputBoundary {

    /**
     * Displays the edit-profile page.
     */
    void showEditProfile();

    /**
     * Displays a successful profile-update response.
     */
    void showProfileUpdateSuccess();

    /**
     * Displays a failed profile-update response with an error message.
     *
     * @param message explanation of the failure
     */
    void showProfileUpdateFailure(String message);
}
