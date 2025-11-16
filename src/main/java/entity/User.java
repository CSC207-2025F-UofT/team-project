package entity;

import java.time.Instant;
import java.util.List;

public class User {
    private final String username;
    private final String password;
    private final Instant createdAt;
    private final List<Visit> visits;
    private final List<Note> privateNotes;

    public User(String username, String password, List<Visit> visits, List<Note> privateNotes) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
        this.createdAt = Instant.now();
        this.visits = visits;
        this.privateNotes = privateNotes;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public List<Note> getPrivateNotes() {
        return privateNotes;
    }
}
