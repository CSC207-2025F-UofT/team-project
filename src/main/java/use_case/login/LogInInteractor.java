package use_case.login;

import entity.User;
import use_case.account.AccountEmail;
import use_case.account.AccountProfile;
import use_case.account.AuthSession;
import use_case.account.ProfileGateway;
import use_case.account.SessionStarter;

import java.util.Optional;

public final class LogInInteractor implements LogInInputBoundary {
    private final SignInGateway auth;
    private final ProfileGateway profiles;
    private final SessionStarter sessions;
    private final LogInOutputBoundary output;

    public LogInInteractor(SignInGateway auth, ProfileGateway profiles, SessionStarter sessions,
                           LogInOutputBoundary output) {
        this.auth = auth;
        this.profiles = profiles;
        this.sessions = sessions;
        this.output = output;
    }

    @Override
    public void execute(LogInRequest request) {
        final Optional<String> email = AccountEmail.parse(request.email());
        final String password = request.password();
        if (email.isEmpty() || password == null || password.isEmpty()) {
            output.present(LogInResponse.Failure.BAD_CREDENTIALS);
            return;
        }
        switch (auth.signIn(email.get(), password)) {
            case SignInGateway.SignedIn(AuthSession session) -> startSession(session);
            case SignInGateway.Failure.BAD_CREDENTIALS -> output.present(LogInResponse.Failure.BAD_CREDENTIALS);
            case SignInGateway.Failure.NO_USABLE_SESSION -> output.present(LogInResponse.Failure.NO_USABLE_SESSION);
            case SignInGateway.Failure.RETRYABLE_FAILURE -> output.present(LogInResponse.Failure.RETRYABLE_FAILURE);
        }
    }

    // The user only counts as signed in once their profile has loaded, so a failed load leaves them signed out.
    private void startSession(AuthSession session) {
        final Optional<User> user = profiles.loadProfile(session);
        if (user.isEmpty()) {
            output.present(LogInResponse.Failure.RETRYABLE_FAILURE);
            return;
        }
        sessions.start(session, user.get());
        output.present(new LogInResponse.Success(AccountProfile.from(user.get())));
    }
}
