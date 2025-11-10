package entity;

import java.time.Instant;
import java.util.List;

public class User {
    private final String username;
    private final String password;
    private final Instant createdAt;
    private final List<Visit> visitedLandmarks;
    private final List<Note> privateNotes;
    // Missing createdAt, visitedLandmarks, privateNotes

    public User(String username, String password, List<Visit> visitedLandmarks, List<Note> privateNotes) {
        if ("".equals(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.username = username;
        this.password = password;
        this.createdAt = Instant.now();
        this.visitedLandmarks = visitedLandmarks;
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

    public List<Visit> getVisitedLandmarks() {
        return visitedLandmarks;
    }

    public List<Note> getPrivateNotes() {
        return privateNotes;
    }
}
