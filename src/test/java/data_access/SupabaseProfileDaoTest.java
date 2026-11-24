package data_access;

import com.google.gson.JsonParser;
import entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import use_case.account.AuthSession;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class SupabaseProfileDaoTest {
    private static final UUID ID = UUID.fromString("11111111-1111-4111-8111-111111111111");
    private static final UUID OTHER_ID = UUID.fromString("22222222-2222-4222-8222-222222222222");
    private static final AuthSession SESSION = new AuthSession(ID, "first.user.token");

    private LoopbackSupabase server;
    private SupabaseProfileDao profiles;

    @BeforeEach
    void setUp() throws IOException {
        server = new LoopbackSupabase();
        profiles = new SupabaseProfileDao(server.client(Duration.ofSeconds(3)));
    }

    @AfterEach
    void tearDown() {
        server.close();
    }

    @Test
    void loadsTheSessionUsersProfileAsThatUser() throws InterruptedException {
        server.enqueue(200, profile(ID, "first_user", "First User"));

        final User user = profiles.loadProfile(SESSION).orElseThrow();

        assertEquals(ID, user.getUserID());
        assertEquals("first_user", user.getUsername());
        assertEquals("First User", user.getDisplayName());
        final var call = server.takeCall();
        assertEquals("/rest/v1/profiles?select=id,username,display_name&id=eq." + ID, call.path());
        assertEquals("Bearer first.user.token", call.authorization());
    }

    private static Stream<String> unusableProfiles() {
        return Stream.of("[]", "null", "[null]", "[{}, {}]",
                profile(OTHER_ID, "first_user", "First User"),
                profile(ID, "First_user", "First User"),
                profile(ID, "first_user", "\\u00a0"),
                "[{\"id\":\"%s\",\"username\":12345,\"display_name\":\"First\"}]".formatted(ID));
    }

    @ParameterizedTest
    @MethodSource("unusableProfiles")
    void unusableProfileIsNotLoaded(String body) {
        server.enqueue(200, body);

        assertTrue(profiles.loadProfile(SESSION).isEmpty());
    }

    @Test
    void failedProfileRequestIsNotLoaded() {
        server.enqueue(503, "{\"message\":\"provider detail\"}");

        assertTrue(profiles.loadProfile(SESSION).isEmpty());
    }

    @Test
    void usernameIsTakenOnlyWhenTheServiceSaysItIsUnavailable() throws InterruptedException {
        server.enqueue(200, "\n false \t");

        assertTrue(profiles.isTaken("first_user"));

        final var call = server.takeCall();
        assertEquals("/rest/v1/rpc/username_is_available", call.path());
        final var body = JsonParser.parseString(call.body()).getAsJsonObject();
        assertEquals("first_user", body.get("candidate").getAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "\"false\"", "[false]", "{\"available\":false}", "FALSE", "null"})
    void anyOtherAvailabilityReplyDoesNotCountAsTaken(String reply) {
        server.enqueue(200, reply);

        assertFalse(profiles.isTaken("first_user"));
    }

    @Test
    void failedAvailabilityRequestDoesNotCountAsTaken() {
        server.enqueue(403, "false");

        assertFalse(profiles.isTaken("first_user"));
    }

    private static String profile(UUID id, String username, String displayName) {
        return """
                [{"id":"%s","username":"%s","display_name":"%s"}]
                """.formatted(id, username, displayName);
    }
}
