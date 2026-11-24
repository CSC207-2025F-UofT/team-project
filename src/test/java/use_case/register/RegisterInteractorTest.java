package use_case.register;

import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import use_case.account.AccountProfile;
import use_case.account.AuthSession;
import use_case.account.ProfileGateway;
import use_case.account.SessionStarter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegisterInteractorTest {
    private static final UUID ID = UUID.randomUUID();
    private static final AuthSession SESSION = new AuthSession(ID, "token");
    private static final RegisterRequest VALID =
            new RegisterRequest("person@example.com", "new_user", "New User", "abcdefgh", "abcdefgh");

    private final FakeAccounts accounts = new FakeAccounts();
    private final List<RegisterResponse> responses = new ArrayList<>();
    private final RegisterInputBoundary interactor =
            new RegisterInteractor(accounts, accounts, accounts, accounts, responses::add);

    @Test
    void normalizesProfileInputAndSignsInWithThePersistedProfile() {
        final String password = " 123456 ";
        final User persisted = new User(ID, "new_user", "Persisted User");
        accounts.signUp = new SignUpGateway.SignedUp(SESSION);
        accounts.profile = Optional.of(persisted);

        interactor.execute(new RegisterRequest("  Person+test@example.com ", "  NEW_User  ", " New User ",
                password, password));

        assertEquals("Person+test@example.com", accounts.email);
        assertEquals("new_user", accounts.username);
        assertEquals("New User", accounts.displayName);
        assertEquals(password, accounts.password);
        assertEquals(SESSION, accounts.startedSession);
        assertEquals(persisted, accounts.startedUser);
        assertEquals(List.of(new RegisterResponse.Success(new AccountProfile(ID, "new_user", "Persisted User"))),
                responses);
    }

    @Test
    void createdAccountWhoseProfileCannotLoadStaysSignedOut() {
        accounts.signUp = new SignUpGateway.SignedUp(SESSION);

        interactor.execute(VALID);

        assertNull(accounts.startedSession);
        assertEquals(List.of(RegisterResponse.Failure.ACCOUNT_CREATED_PROFILE_UNAVAILABLE), responses);
    }

    @ParameterizedTest
    @MethodSource("invalidRequests")
    void rejectsInvalidFieldsWithoutContactingTheAccountService(RegisterRequest request,
                                                                 RegisterResponse.Failure expected) {
        interactor.execute(request);

        assertEquals(List.of(expected), responses);
        assertEquals(0, accounts.signUpCalls);
    }

    private static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of(new RegisterRequest("missing-at.example.com", "new_user", "New User", "abcdefgh",
                        "abcdefgh"), RegisterResponse.Failure.INVALID_EMAIL),
                Arguments.of(new RegisterRequest("a..b@example.com", "new_user", "New User", "abcdefgh", "abcdefgh"),
                        RegisterResponse.Failure.INVALID_EMAIL),
                Arguments.of(new RegisterRequest("person@example.com", "ab", "New User", "abcdefgh", "abcdefgh"),
                        RegisterResponse.Failure.INVALID_USERNAME),
                Arguments.of(new RegisterRequest("person@example.com", "new-user", "New User", "abcdefgh",
                        "abcdefgh"), RegisterResponse.Failure.INVALID_USERNAME),
                Arguments.of(new RegisterRequest("person@example.com", "new_user", " ", "abcdefgh", "abcdefgh"),
                        RegisterResponse.Failure.INVALID_DISPLAY_NAME),
                Arguments.of(new RegisterRequest("person@example.com", "new_user", "New User", "1234567", "1234567"),
                        RegisterResponse.Failure.PASSWORD_TOO_SHORT),
                Arguments.of(new RegisterRequest("person@example.com", "new_user", "New User", " abcdefgh", "abcdefgh"),
                        RegisterResponse.Failure.PASSWORD_MISMATCH));
    }

    @ParameterizedTest
    @CsvSource({"true, USERNAME_TAKEN", "false, RETRYABLE_FAILURE"})
    void rejectedProfileIsReportedAsTakenOnlyWhenTheUsernameIsConfirmedTaken(
            boolean taken, RegisterResponse.Failure expected) {
        accounts.signUp = SignUpGateway.Failure.PROFILE_REJECTED;
        accounts.usernameTaken = taken;

        interactor.execute(VALID);

        assertEquals("new_user", accounts.checkedUsername);
        assertEquals(List.of(expected), responses);
    }

    @ParameterizedTest
    @CsvSource({
        "INVALID_EMAIL, INVALID_EMAIL",
        "PASSWORD_REJECTED, PASSWORD_REJECTED",
        "ACCOUNT_UNAVAILABLE, ACCOUNT_UNAVAILABLE",
        "NO_USABLE_SESSION, NO_USABLE_SESSION",
        "SIGNUP_UNCERTAIN, SIGNUP_UNCERTAIN",
        "RETRYABLE_FAILURE, RETRYABLE_FAILURE"
    })
    void presentsOtherSignUpFailuresWithoutFurtherRequests(SignUpGateway.Failure failure,
                                                           RegisterResponse.Failure expected) {
        accounts.signUp = failure;

        interactor.execute(VALID);

        assertEquals(List.of(expected), responses);
        assertNull(accounts.checkedUsername);
        assertNull(accounts.startedSession);
    }

    @Test
    void diagnosticStringOmitsSubmittedCredentials() {
        final RegisterRequest request =
                new RegisterRequest("person@example.com", "new_user", "New User", "abcdefgh", "confirmation");
        assertFalse(request.toString().contains(request.email()));
        assertFalse(request.toString().contains(request.password()));
        assertFalse(request.toString().contains(request.passwordConfirmation()));
    }

    private static final class FakeAccounts
            implements SignUpGateway, ProfileGateway, UsernameAvailabilityGateway, SessionStarter {
        private SignUpGateway.Result signUp;
        private Optional<User> profile = Optional.empty();
        private boolean usernameTaken;
        private int signUpCalls;
        private String email;
        private String password;
        private String username;
        private String displayName;
        private String checkedUsername;
        private AuthSession startedSession;
        private User startedUser;

        @Override
        public SignUpGateway.Result signUp(String email, String password, String username, String displayName) {
            signUpCalls++;
            this.email = email;
            this.password = password;
            this.username = username;
            this.displayName = displayName;
            return signUp;
        }

        @Override
        public Optional<User> loadProfile(AuthSession session) {
            return profile;
        }

        @Override
        public boolean isTaken(String candidate) {
            checkedUsername = candidate;
            return usernameTaken;
        }

        @Override
        public void start(AuthSession session, User user) {
            startedSession = session;
            startedUser = user;
        }
    }
}
