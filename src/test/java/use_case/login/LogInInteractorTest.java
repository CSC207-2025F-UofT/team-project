package use_case.login;

import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import use_case.account.AccountProfile;
import use_case.account.AuthSession;
import use_case.account.ProfileGateway;
import use_case.account.SessionStarter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class LogInInteractorTest {
    private static final UUID ID = UUID.randomUUID();
    private static final AuthSession SESSION = new AuthSession(ID, "token");
    private static final LogInRequest VALID = new LogInRequest("  Person@example.com  ", " p ");

    private final FakeAccounts accounts = new FakeAccounts();
    private final List<LogInResponse> responses = new ArrayList<>();
    private final LogInInputBoundary interactor = new LogInInteractor(accounts, accounts, accounts, responses::add);

    @Test
    void signsInWithTheLoadedProfileAndPresentsASnapshot() {
        final User user = new User(ID, "new_user", "New User");
        accounts.signIn = new SignInGateway.SignedIn(SESSION);
        accounts.profile = Optional.of(user);

        interactor.execute(VALID);

        assertEquals("Person@example.com", accounts.email);
        assertEquals(VALID.password(), accounts.password);
        assertEquals(SESSION, accounts.profileRequestedFor);
        assertEquals(SESSION, accounts.startedSession);
        assertEquals(user, accounts.startedUser);
        user.setDisplayName("Changed After Login");
        assertEquals(List.of(new LogInResponse.Success(new AccountProfile(ID, "new_user", "New User"))), responses);
    }

    @Test
    void profileThatCannotLoadLeavesTheUserSignedOut() {
        accounts.signIn = new SignInGateway.SignedIn(SESSION);
        accounts.profile = Optional.empty();

        interactor.execute(VALID);

        assertNull(accounts.startedSession);
        assertEquals(List.of(LogInResponse.Failure.RETRYABLE_FAILURE), responses);
    }

    @ParameterizedTest
    @CsvSource({"BAD_CREDENTIALS, BAD_CREDENTIALS", "NO_USABLE_SESSION, NO_USABLE_SESSION",
        "RETRYABLE_FAILURE, RETRYABLE_FAILURE"})
    void presentsSignInFailureWithoutLoadingAProfile(SignInGateway.Failure failure, LogInResponse.Failure expected) {
        accounts.signIn = failure;

        interactor.execute(VALID);

        assertEquals(List.of(expected), responses);
        assertNull(accounts.profileRequestedFor);
        assertNull(accounts.startedSession);
    }

    @Test
    void malformedEmailAndEmptyPasswordDoNotContactTheAccountService() {
        interactor.execute(new LogInRequest("not-an-email", "any_password"));
        interactor.execute(new LogInRequest("known@example.com", ""));

        assertEquals(List.of(LogInResponse.Failure.BAD_CREDENTIALS, LogInResponse.Failure.BAD_CREDENTIALS), responses);
        assertEquals(0, accounts.signInCalls);
    }

    @Test
    void diagnosticStringOmitsSubmittedCredentials() {
        final LogInRequest request = new LogInRequest("person@example.com", "password123");
        assertFalse(request.toString().contains(request.email()));
        assertFalse(request.toString().contains(request.password()));
    }

    private static final class FakeAccounts implements SignInGateway, ProfileGateway, SessionStarter {
        private SignInGateway.Result signIn;
        private Optional<User> profile = Optional.empty();
        private int signInCalls;
        private String email;
        private String password;
        private AuthSession profileRequestedFor;
        private AuthSession startedSession;
        private User startedUser;

        @Override
        public SignInGateway.Result signIn(String email, String password) {
            signInCalls++;
            this.email = email;
            this.password = password;
            return signIn;
        }

        @Override
        public Optional<User> loadProfile(AuthSession session) {
            profileRequestedFor = session;
            return profile;
        }

        @Override
        public void start(AuthSession session, User user) {
            startedSession = session;
            startedUser = user;
        }
    }
}
