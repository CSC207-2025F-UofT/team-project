package use_case.account;

/**
 * Password rules shared by the account use cases.
 * Keeps the minimum length in one place so register and change password can't disagree.
 */
public final class AccountPassword {
    // Matches the minimum set in our Supabase project
    public static final int MIN_LENGTH = 8;

    private AccountPassword() { }

    /** Returns true if the password has at least MIN_LENGTH characters. */
    public static boolean isLongEnough(String password) {
        if (password == null) {
            return false;
        }

        // Counts real characters, so an emoji counts as one (same as RegisterInteractor)
        return password.codePointCount(0, password.length()) >= MIN_LENGTH;
    }
}
