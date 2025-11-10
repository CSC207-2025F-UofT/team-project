package entity;

/**
 * Entity class representing a Tag.
 */
public class Tag {
    private final Integer tagId;
    private final String name;

    /**
     * Creates a Tag with a unique ID and name.
     * @param tagId unique tag identifier
     * @param name tag name
     * @throws IllegalArgumentException if the password or name are empty
     */
    public Tag(Integer tagId, String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }
        this.tagId = tagId;
        this.name = name;
    }

    public Integer getTagId() { return tagId; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
