package data_access;

import com.google.gson.JsonObject;
import use_case.account.AuthSession;
import use_case.login.SignInGateway;
import use_case.logout.SessionRevocationGateway;
import use_case.register.SignUpGateway;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.UUID;

import static data_access.SupabaseJson.id;
import static data_access.SupabaseJson.object;
import static data_access.SupabaseJson.parse;
import static data_access.SupabaseJson.string;

/** Translates Supabase Auth responses into the results the account use cases understand. */
public final class SupabaseAuthGateway implements SignUpGateway, SignInGateway, SessionRevocationGateway {
    private static final System.Logger LOGGER = System.getLogger(SupabaseAuthGateway.class.getName());

    private final SupabaseClient client;

    public SupabaseAuthGateway(SupabaseClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public SignUpGateway.Result signUp(String email, String password, String username, String displayName) {
        final JsonObject body = credentials(email, password);
        final JsonObject profile = new JsonObject();
        profile.addProperty("username", username);
        profile.addProperty("display_name", displayName);
        body.add("data", profile);

        final HttpResponse<String> response;
        try {
            response = client.post("/auth/v1/signup", body, null);
        } catch (HttpConnectTimeoutException | ConnectException | UnknownHostException ignored) {
            return SignUpGateway.Failure.RETRYABLE_FAILURE;
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return SignUpGateway.Failure.SIGNUP_UNCERTAIN;
        } catch (IOException ignored) {
            // The request may have reached Supabase before the connection failed.
            return SignUpGateway.Failure.SIGNUP_UNCERTAIN;
        }

        final JsonObject reply = object(parse(response.body()));
        if (response.statusCode() != 200) {
            return signUpFailure(response.statusCode(), reply);
        }
        return switch (readSession(reply)) {
            case Issued(AuthSession session) -> new SignUpGateway.SignedUp(session);
            case NoSession.USER_WITHOUT_SESSION -> SignUpGateway.Failure.NO_USABLE_SESSION;
            case NoSession.MALFORMED -> SignUpGateway.Failure.SIGNUP_UNCERTAIN;
        };
    }

    private static SignUpGateway.Failure signUpFailure(int status, JsonObject reply) {
        final String code = string(reply, "error_code");
        // Supabase hides which database constraint failed when the profile trigger rejects the new user.
        if (status == 500 && "unexpected_failure".equals(code)
                && "Database error saving new user".equals(string(reply, "msg"))) {
            return SignUpGateway.Failure.PROFILE_REJECTED;
        }
        if (status == 408 || status >= 500) {
            return SignUpGateway.Failure.SIGNUP_UNCERTAIN;
        }
        return switch (code) {
            case "email_address_invalid" -> SignUpGateway.Failure.INVALID_EMAIL;
            case "weak_password" -> SignUpGateway.Failure.PASSWORD_REJECTED;
            case "user_already_exists", "email_exists", "validation_failed" ->
                    SignUpGateway.Failure.ACCOUNT_UNAVAILABLE;
            case null, default -> SignUpGateway.Failure.RETRYABLE_FAILURE;
        };
    }

    @Override
    public SignInGateway.Result signIn(String email, String password) {
        final HttpResponse<String> response;
        try {
            response = client.post("/auth/v1/token?grant_type=password", credentials(email, password), null);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return SignInGateway.Failure.RETRYABLE_FAILURE;
        } catch (IOException ignored) {
            return SignInGateway.Failure.RETRYABLE_FAILURE;
        }

        final JsonObject reply = object(parse(response.body()));
        if (response.statusCode() != 200) {
            return switch (string(reply, "error_code")) {
                case "invalid_credentials", "user_not_found" -> SignInGateway.Failure.BAD_CREDENTIALS;
                case "email_not_confirmed" -> SignInGateway.Failure.NO_USABLE_SESSION;
                case null, default -> SignInGateway.Failure.RETRYABLE_FAILURE;
            };
        }
        return switch (readSession(reply)) {
            case Issued(AuthSession session) -> new SignInGateway.SignedIn(session);
            case NoSession.USER_WITHOUT_SESSION -> SignInGateway.Failure.NO_USABLE_SESSION;
            case NoSession.MALFORMED -> SignInGateway.Failure.RETRYABLE_FAILURE;
        };
    }

    @Override
    public void revoke(AuthSession session) {
        try {
            final HttpResponse<String> response =
                    client.post("/auth/v1/logout?scope=local", null, session.accessToken());
            if (response.statusCode() != 204) {
                LOGGER.log(System.Logger.Level.WARNING, "Session revocation returned HTTP {0}.",
                        response.statusCode());
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            LOGGER.log(System.Logger.Level.WARNING, "Session revocation was interrupted.");
        } catch (IOException ignored) {
            LOGGER.log(System.Logger.Level.WARNING, "Could not confirm session revocation.");
        }
    }

    private static JsonObject credentials(String email, String password) {
        final JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("password", password);
        return body;
    }

    /**
     * A successful reply normally carries an access token and the user. When email confirmation is required, it
     * carries only the user.
     */
    private static SessionReply readSession(JsonObject reply) {
        if (reply == null) {
            return NoSession.MALFORMED;
        }
        final UUID userId = id(object(reply.get("user")));
        final String token = string(reply, "access_token");
        if (token == null || token.isBlank()) {
            return userId != null || id(reply) != null ? NoSession.USER_WITHOUT_SESSION : NoSession.MALFORMED;
        }
        return userId == null ? NoSession.MALFORMED : new Issued(new AuthSession(userId, token));
    }

    private sealed interface SessionReply permits Issued, NoSession { }

    private record Issued(AuthSession session) implements SessionReply { }

    private enum NoSession implements SessionReply {
        USER_WITHOUT_SESSION,
        MALFORMED
    }
}
