package data_access;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.User;
import use_case.account.AuthSession;
import use_case.account.ProfileGateway;
import use_case.register.UsernameAvailabilityGateway;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;

import static data_access.SupabaseJson.id;
import static data_access.SupabaseJson.object;
import static data_access.SupabaseJson.parse;
import static data_access.SupabaseJson.string;

/** Reads profiles from the {@code profiles} table, which Supabase fills in when an account is created. */
public final class SupabaseProfileDao implements ProfileGateway, UsernameAvailabilityGateway {
    private final SupabaseClient client;

    public SupabaseProfileDao(SupabaseClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public Optional<User> loadProfile(AuthSession session) {
        final HttpResponse<String> response;
        try {
            response = client.get("/rest/v1/profiles?select=id,username,display_name&id=eq." + session.userId(),
                    session.accessToken());
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (IOException ignored) {
            return Optional.empty();
        }
        final JsonElement rows = parse(response.body());
        if (response.statusCode() != 200 || rows == null || !rows.isJsonArray() || rows.getAsJsonArray().size() != 1) {
            return Optional.empty();
        }
        final JsonObject row = object(rows.getAsJsonArray().get(0));
        final String username = string(row, "username");
        final String displayName = string(row, "display_name");
        if (!session.userId().equals(id(row)) || !User.isValidUsername(username)
                || !User.isValidDisplayName(displayName)) {
            return Optional.empty();
        }
        return Optional.of(new User(session.userId(), username, displayName));
    }

    @Override
    public boolean isTaken(String username) {
        final JsonObject body = new JsonObject();
        body.addProperty("candidate", username);
        try {
            final HttpResponse<String> response = client.post("/rest/v1/rpc/username_is_available", body, null);
            final JsonElement available = parse(response.body());
            return response.statusCode() == 200 && available != null && available.isJsonPrimitive()
                    && available.getAsJsonPrimitive().isBoolean() && !available.getAsBoolean();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            return false;
        } catch (IOException ignored) {
            return false;
        }
    }
}
