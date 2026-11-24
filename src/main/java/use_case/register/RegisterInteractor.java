package use_case.register;

import entity.User;
import use_case.account.AccountEmail;
import use_case.account.AccountProfile;
import use_case.account.AuthSession;
import use_case.account.ProfileGateway;
import use_case.account.SessionStarter;

import java.util.Locale;
import java.util.Optional;

public final class RegisterInteractor implements RegisterInputBoundary {
    private static final int MIN_PASSWORD_LENGTH = 8;

    private final SignUpGateway auth;
    private final ProfileGateway profiles;
    private final UsernameAvailabilityGateway usernames;
    private final SessionStarter sessions;
    private final RegisterOutputBoundary output;

    public RegisterInteractor(SignUpGateway auth, ProfileGateway profiles, UsernameAvailabilityGateway usernames,
                              SessionStarter sessions, RegisterOutputBoundary output) {
        this.auth = auth;
        this.profiles = profiles;
        this.usernames = usernames;
        this.sessions = sessions;
        this.output = output;
    }

    @Override
    public void execute(RegisterRequest request) {
        final Optional<String> email = AccountEmail.parse(request.email());
        final String username = canonicalUsername(request.username());
        final String displayName = request.displayName() == null ? null : request.displayName().strip();
        final String password = request.password();

        if (email.isEmpty()) {
            output.present(RegisterResponse.Failure.INVALID_EMAIL);
        } else if (!User.isValidUsername(username)) {
            output.present(RegisterResponse.Failure.INVALID_USERNAME);
        } else if (!User.isValidDisplayName(displayName)) {
            output.present(RegisterResponse.Failure.INVALID_DISPLAY_NAME);
        } else if (password == null || password.codePointCount(0, password.length()) < MIN_PASSWORD_LENGTH) {
            output.present(RegisterResponse.Failure.PASSWORD_TOO_SHORT);
        } else if (!password.equals(request.passwordConfirmation())) {
            output.present(RegisterResponse.Failure.PASSWORD_MISMATCH);
        } else {
            signUp(email.get(), password, username, displayName);
        }
    }

    /** Usernames are case-insensitive, so they are stored in lowercase. */
    private static String canonicalUsername(String input) {
        return input == null ? null : input.strip().toLowerCase(Locale.ROOT);
    }

    private void signUp(String email, String password, String username, String displayName) {
        switch (auth.signUp(email, password, username, displayName)) {
            case SignUpGateway.SignedUp(AuthSession session) -> startSession(session);
            case SignUpGateway.Failure.PROFILE_REJECTED -> output.present(usernames.isTaken(username)
                    ? RegisterResponse.Failure.USERNAME_TAKEN : RegisterResponse.Failure.RETRYABLE_FAILURE);
            case SignUpGateway.Failure.INVALID_EMAIL -> output.present(RegisterResponse.Failure.INVALID_EMAIL);
            case SignUpGateway.Failure.PASSWORD_REJECTED ->
                    output.present(RegisterResponse.Failure.PASSWORD_REJECTED);
            case SignUpGateway.Failure.ACCOUNT_UNAVAILABLE ->
                    output.present(RegisterResponse.Failure.ACCOUNT_UNAVAILABLE);
            case SignUpGateway.Failure.NO_USABLE_SESSION ->
                    output.present(RegisterResponse.Failure.NO_USABLE_SESSION);
            case SignUpGateway.Failure.SIGNUP_UNCERTAIN -> output.present(RegisterResponse.Failure.SIGNUP_UNCERTAIN);
            case SignUpGateway.Failure.RETRYABLE_FAILURE ->
                    output.present(RegisterResponse.Failure.RETRYABLE_FAILURE);
        }
    }

    private void startSession(AuthSession session) {
        final Optional<User> user = profiles.loadProfile(session);
        if (user.isEmpty()) {
            output.present(RegisterResponse.Failure.ACCOUNT_CREATED_PROFILE_UNAVAILABLE);
            return;
        }
        sessions.start(session, user.get());
        output.present(new RegisterResponse.Success(AccountProfile.from(user.get())));
    }
}
