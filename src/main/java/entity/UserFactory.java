package entity;

import java.time.Instant;
import java.util.List;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String name, String password) {
        return new User(name, password, null, null);
    }

    public User create(String name, String password, Instant createdAt, List<Visit> visits, List<Note> privateNotes) {
        return new User(name, password, createdAt, visits, privateNotes);
    }
}
