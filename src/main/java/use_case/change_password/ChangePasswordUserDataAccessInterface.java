package use_case.change_password;

/**
 * The DAO interface for the Change Password Use Case.
 * The account service (Supabase) is what actually stores passwords, so the DAO talks to it.
 */
public interface ChangePasswordUserDataAccessInterface {

    /** What the account service said. */
    enum Result {
        CHANGED,
        WRONG_CURRENT_PASSWORD,
        // e.g. the service thinks the new password is too weak
        REJECTED,
        // no internet, service down, etc.
        UNAVAILABLE
    }

    /** Checks currentPassword is right for the signed-in user, then sets newPassword. */
    Result changePassword(String currentPassword, String newPassword);
}
