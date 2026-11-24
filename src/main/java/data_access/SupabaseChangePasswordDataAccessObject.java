package data_access;

import com.google.gson.JsonObject;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.SignInGateway;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * Change Password (#22) using Supabase Auth.
 * Supabase doesn't check the old password itself, so we sign in again with it first, then set the new one.
 */
public class SupabaseChangePasswordDataAccessObject implements ChangePasswordUserDataAccessInterface {
    private final SupabaseClient client;
    private final SignInGateway signInGateway;
    private final InMemorySessionStore sessionStore;

    public SupabaseChangePasswordDataAccessObject(SupabaseClient client, SignInGateway signInGateway,
                                                  InMemorySessionStore sessionStore) {
        this.client = client;
        this.signInGateway = signInGateway;
        this.sessionStore = sessionStore;
    }

    @Override
    public Result changePassword(String currentPassword, String newPassword) {
        Optional<String> accessToken = sessionStore.accessToken();
        if (accessToken.isEmpty()) {
            return Result.UNAVAILABLE;
        }

        try {
            // Step 1: find the email, since signing in needs it and User doesn't store it
            String email = findEmail(accessToken.get());
            if (email == null) {
                return Result.UNAVAILABLE;
            }

            // Step 2: check the current password with the same sign in that Log In uses
            SignInGateway.Result signInResult = signInGateway.signIn(email, currentPassword);
            if (signInResult == SignInGateway.Failure.BAD_CREDENTIALS) {
                return Result.WRONG_CURRENT_PASSWORD;
            }

            // Step 3: set the new password with the fresh session from step 2
            if (signInResult instanceof SignInGateway.SignedIn signedIn) {
                return setPassword(signedIn.session().accessToken(), newPassword);
            }

            return Result.UNAVAILABLE;

        } catch (IOException e) {
            return Result.UNAVAILABLE;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Result.UNAVAILABLE;
        }
    }

    /** Asks Supabase for the signed-in user's email. Returns null if it can't. */
    private String findEmail(String accessToken) throws IOException, InterruptedException {
        HttpResponse<String> response = client.get("/auth/v1/user", accessToken);
        if (response.statusCode() != 200) {
            return null;
        }

        JsonObject user = SupabaseJson.object(SupabaseJson.parse(response.body()));
        return SupabaseJson.string(user, "email");
    }

    /** Sends the new password to Supabase and turns its reply into a Result. */
    private Result setPassword(String accessToken, String newPassword) throws IOException, InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("password", newPassword);

        HttpResponse<String> response = client.put("/auth/v1/user", body, accessToken);
        if (response.statusCode() == 200) {
            return Result.CHANGED;
        }

        JsonObject reply = SupabaseJson.object(SupabaseJson.parse(response.body()));
        String errorCode = SupabaseJson.string(reply, "error_code");

        // Supabase's codes for a new password it won't take
        if ("weak_password".equals(errorCode) || "same_password".equals(errorCode)) {
            return Result.REJECTED;
        }

        // Anything else, e.g. "reauthentication_needed" if Secure password change is on in the Supabase project
        return Result.UNAVAILABLE;
    }
}
