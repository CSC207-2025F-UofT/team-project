package use_case.account;

import java.util.Objects;
import java.util.UUID;

/** Proof that the account service authenticated a user. The access token is opaque to the use cases. */
public record AuthSession(UUID userId, String accessToken) {
    public AuthSession {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(accessToken);
    }

    @Override
    public String toString() {
        return "AuthSession[userId=" + userId + ", accessToken=redacted]";
    }
}
