package use_case.admin_signup;

/**
 * Checks whether an admin invite code is valid.
 * Where the real code is stored (environment variable, config file, ...) is up to the implementation.
 */
public interface AdminInviteCodeChecker {

    /**
     * Checks the given invite code.
     * @param code the invite code entered by the user
     * @return true if the code is valid; false otherwise
     */
    boolean isValid(String code);
}
