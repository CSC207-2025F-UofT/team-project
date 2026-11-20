package Data_access;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import use_case.admin_signup.AdminInviteCodeChecker;

/**
 * Checks admin invite codes against the PAWPORTAL_ADMIN_INVITE_CODE environment variable.
 * If the variable is not set (or empty), every code is rejected, so nobody can sign up as an admin.
 */
public class EnvAdminInviteCodeChecker implements AdminInviteCodeChecker {

    public static final String ENV_VAR = "PAWPORTAL_ADMIN_INVITE_CODE";

    private final String expectedCode;

    public EnvAdminInviteCodeChecker() {
        this.expectedCode = System.getenv(ENV_VAR);
    }

    @Override
    public boolean isValid(String code) {
        if (expectedCode == null || expectedCode.isEmpty() || code == null) {
            return false;
        }
        // Constant-time comparison, so the code cannot be guessed from response timing.
        return MessageDigest.isEqual(expectedCode.getBytes(StandardCharsets.UTF_8),
                                     code.getBytes(StandardCharsets.UTF_8));
    }
}
