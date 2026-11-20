package org.accessgraph.use_case.sign_in;

/** Validates credentials through the authentication boundary. The caller owns the password buffer. */
public final class SignIn {
    private final Authenticator authenticator;

    public SignIn(Authenticator authenticator) { this.authenticator = authenticator; }

    public boolean authenticate(String username, char[] password) {
        return !username.isBlank() && password.length != 0 && authenticator.matches(username.strip(), password);
    }
}
