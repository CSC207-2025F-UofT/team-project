package use_case.account;

import java.util.Optional;
import java.util.regex.Pattern;

public final class AccountEmail {
    private static final String ATOM = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+";
    private static final String LABEL = "[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?";
    private static final Pattern EMAIL =
            Pattern.compile(ATOM + "(?:\\." + ATOM + ")*@" + LABEL + "(?:\\." + LABEL + ")+");

    private AccountEmail() { }

    public static Optional<String> parse(String input) {
        final String email = input == null ? "" : input.strip();
        if (email.length() > 254 || email.indexOf('@') > 64 || !EMAIL.matcher(email).matches()) {
            return Optional.empty();
        }
        return Optional.of(email);
    }
}
