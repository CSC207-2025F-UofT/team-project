package entity;

/** A physical item. Revealing an item never constructs another copy. */
public final class Collectable {
    private final String id;
    private final String name;
    private final String description;
    private boolean revealed;

    public Collectable(String id, String name, String description, boolean revealed) {
        this.id = text(id, "item id");
        this.name = text(name, "item name");
        this.description = text(description, "item description");
        this.revealed = revealed;
    }

    public String id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public boolean isRevealed() { return revealed; }
    void reveal() { revealed = true; }

    private static String text(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " must not be blank");
        return value;
    }
}
