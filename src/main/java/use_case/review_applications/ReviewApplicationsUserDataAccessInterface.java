package use_case.review_applications;

/**
 * Where the Review Applications use case reads adopter names.
 */
public interface ReviewApplicationsUserDataAccessInterface {

    /**
     * Returns the display name of a user.
     * @param userId the user's id
     * @return the user's name, or null if no user has this id
     */
    String getUserName(String userId);
}
