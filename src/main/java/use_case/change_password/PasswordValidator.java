package use_case.change_password;

import java.util.regex.Pattern;

/**
 * Utility class for validating password strength.
 * Enforces consistent password requirements across the application.
 */
public class PasswordValidator {
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile(".*\\s.*");
    private static final int MIN_PASSWORD_LENGTH = 8;

    /**
     * Validates password strength and returns a specific error message,
     * or null if the password is strong enough.
     *
     * @param password the password to validate
     * @return an error message string if validation fails, null if password is strong
     */
    public static String validate(String password) {
        StringBuilder errors = new StringBuilder();

        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.append("Password must be at least ")
                    .append(MIN_PASSWORD_LENGTH)
                    .append(" characters long.\n");
        }
        if (WHITESPACE_PATTERN.matcher(password).find()) {
            errors.append("Password must not contain spaces.\n");
        }
        if (!LOWERCASE_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one lowercase letter.\n");
        }
        if (!UPPERCASE_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one uppercase letter.\n");
        }
        if (!DIGIT_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one digit.\n");
        }
        if (!SPECIAL_CHAR_PATTERN.matcher(password).find()) {
            errors.append("Password must contain at least one symbol (e.g. !@#$%^&*(),.?\":{}|<>).\n");
        }

        if (errors.isEmpty()) {
            return null;
        } else {
            return errors.toString().trim();
        }
    }

    private PasswordValidator() {
    }
}
