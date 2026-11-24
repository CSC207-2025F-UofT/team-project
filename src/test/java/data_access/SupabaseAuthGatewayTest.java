package data_access;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import use_case.account.AuthSession;
import use_case.login.SignInGateway;
import use_case.register.SignUpGateway;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class SupabaseAuthGatewayTest {
    private static final String EMAIL = "loopback@example.invalid";
    private static final String PASSWORD = " test\"password ";
    private static final UUID ID = UUID.fromString("11111111-1111-4111-8111-111111111111");
    private static final String TOKEN = "first.user.token";

    private LoopbackSupabase server;
    private SupabaseAuthGateway auth;

    @BeforeEach
    void setUp() throws IOException {
        server = new LoopbackSupabase();
        auth = new SupabaseAuthGateway(server.client(Duration.ofSeconds(3)));
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void signUpSendsCredentialsAndProfileMetadataAndReturnsTheSession() throws InterruptedException {
        server.enqueue(200, session(ID, TOKEN));

        assertEquals(new SignUpGateway.SignedUp(new AuthSession(ID, TOKEN)), signUp());

        final var call = server.takeCall();
        assertCall(call, "POST", "/auth/v1/signup", null);
        assertCredentials(call);
        final var metadata = JsonParser.parseString(call.body()).getAsJsonObject().getAsJsonObject("data");
        assertEquals("first_user", metadata.get("username").getAsString());
        assertEquals("First User", metadata.get("display_name").getAsString());
        assertNull(server.unexpectedCall());
    }

    @Test
    void signInReturnsTheSession() throws InterruptedException {
        server.enqueue(200, session(ID, TOKEN));

        assertEquals(new SignInGateway.SignedIn(new AuthSession(ID, TOKEN)), auth.signIn(EMAIL, PASSWORD));

        final var call = server.takeCall();
        assertCall(call, "POST", "/auth/v1/token?grant_type=password", null);
        assertCredentials(call);
    }

    @ParameterizedTest
    @ValueSource(strings = {"<html>error</html>", "{user:{}}", "[]", "{\"access_token\":\"token-without-user\"}"})
    void malformedSuccessReplyIsNotTreatedAsASession(String body) {
        server.enqueue(200, body);
        server.enqueue(200, body);

        assertEquals(SignInGateway.Failure.RETRYABLE_FAILURE, auth.signIn(EMAIL, PASSWORD));
        assertEquals(SignUpGateway.Failure.SIGNUP_UNCERTAIN, signUp());
    }

    @Test
    void replyWithAUserButNoTokenMeansNoUsableSession() {
        server.enqueue(200, "{\"id\":\"" + ID + "\"}");
        server.enqueue(200, "{\"user\":{\"id\":\"" + ID + "\"}}");

        assertEquals(SignUpGateway.Failure.NO_USABLE_SESSION, signUp());
        assertEquals(SignInGateway.Failure.NO_USABLE_SESSION, auth.signIn(EMAIL, PASSWORD));
    }

    @Test
    void signInMapsUnknownEmailAndWrongPasswordToTheSameFailure() {
        server.enqueue(400, "{\"error_code\":\"invalid_credentials\",\"msg\":\"provider: wrong password\"}");
        server.enqueue(400, "{\"error_code\":\"user_not_found\",\"msg\":\"provider: unknown email\"}");
        server.enqueue(400, "{\"error_code\":\"email_not_confirmed\"}");
        server.enqueue(429, "{\"error_code\":\"over_request_rate_limit\"}");

        assertEquals(SignInGateway.Failure.BAD_CREDENTIALS, auth.signIn(EMAIL, PASSWORD));
        assertEquals(SignInGateway.Failure.BAD_CREDENTIALS, auth.signIn(EMAIL, PASSWORD));
        assertEquals(SignInGateway.Failure.NO_USABLE_SESSION, auth.signIn(EMAIL, PASSWORD));
        assertEquals(SignInGateway.Failure.RETRYABLE_FAILURE, auth.signIn(EMAIL, PASSWORD));
    }

    @ParameterizedTest
    @CsvSource({
        "422, email_address_invalid, INVALID_EMAIL",
        "422, weak_password, PASSWORD_REJECTED",
        "422, email_exists, ACCOUNT_UNAVAILABLE",
        "422, user_already_exists, ACCOUNT_UNAVAILABLE",
        "429, over_request_rate_limit, RETRYABLE_FAILURE",
        "500, unexpected_failure, SIGNUP_UNCERTAIN",
        "504, , SIGNUP_UNCERTAIN"
    })
    void signUpMapsRejections(int status, String code, SignUpGateway.Failure expected) {
        server.enqueue(status, code == null ? "" : "{\"error_code\":\"%s\"}".formatted(code));

        assertEquals(expected, signUp());
    }

    @Test
    void hiddenDatabaseRejectionIsReportedAsARejectedProfile() throws InterruptedException {
        server.enqueue(500, """
                {"code":500,"error_code":"unexpected_failure","msg":"Database error saving new user"}
                """);

        assertEquals(SignUpGateway.Failure.PROFILE_REJECTED, signUp());

        server.takeCall();
        assertNull(server.unexpectedCall());
    }

    @Test
    void signUpWhoseReplyStallsIsUncertain() {
        auth = new SupabaseAuthGateway(server.client(Duration.ofMillis(700)));
        server.stallBody(200, session(ID, TOKEN));

        assertEquals(SignUpGateway.Failure.SIGNUP_UNCERTAIN, signUp());
    }

    @Test
    void revokeSendsTheSessionTokenAndToleratesFailure() throws InterruptedException {
        server.enqueue(204, "");
        server.enqueue(500, "{\"msg\":\"provider detail\"}");

        auth.revoke(new AuthSession(ID, TOKEN));
        assertDoesNotThrow(() -> auth.revoke(new AuthSession(ID, TOKEN)));

        final var call = server.takeCall();
        assertCall(call, "POST", "/auth/v1/logout?scope=local", "Bearer " + TOKEN);
        assertEquals("", call.body());
    }

    private SignUpGateway.Result signUp() {
        return auth.signUp(EMAIL, PASSWORD, "first_user", "First User");
    }

    private static void assertCall(LoopbackSupabase.Call call, String method, String path, String authorization) {
        assertEquals(method, call.method());
        assertEquals(path, call.path());
        assertEquals(LoopbackSupabase.API_KEY, call.apiKey());
        assertEquals(authorization, call.authorization());
    }

    private static void assertCredentials(LoopbackSupabase.Call call) {
        assertEquals("application/json", call.contentType());
        final var body = JsonParser.parseString(call.body()).getAsJsonObject();
        assertEquals(EMAIL, body.get("email").getAsString());
        assertEquals(PASSWORD, body.get("password").getAsString());
    }

    private static String session(UUID id, String token) {
        return """
                {"access_token":"%s","refresh_token":"unused-refresh-fixture","user":{"id":"%s"}}
                """.formatted(token, id);
    }
}
