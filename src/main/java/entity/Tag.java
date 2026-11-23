package entity;

import java.util.Objects;

public final class Tag {
    private final String id;
    private final String name;

    public Tag(String id, String name) {
        this.id = Objects.requireNonNull(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Enter a tag name.");
        }
        this.name = name.trim();
    }
    public String getId() { return id; }
    public String getName() { return name; }
}
