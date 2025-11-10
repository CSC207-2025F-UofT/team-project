package entity;

import java.time.Instant;

public class Note {
    private final String noteId;
    private final Landmark landmark;
    private final String content;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Note(String noteId, Landmark landmark, String content) {
        this.noteId = noteId;
        this.landmark = landmark;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }
    public String getNoteId() {
        return noteId;
    }
    public Landmark getLandmark() {
        return landmark;
    }
    public String getContent() {
        return content;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
