package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity representing a document. Documents have a name, creator, and list of
 * users that can access it.
 */
public class Document {

    private final String name;
    private final User creator;
    private final List<User> accessedBy = new ArrayList<>();

    /**
     * Creates a new document with the given non-empty name and the user that
     * created it.
     * @param name the name
     * @param creator the creator
     * @throws IllegalArgumentException if the name is empty
     */
    public Document(String name, User creator) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Document name cannot be empty.");
        }
        this.name = name;
        this.creator = creator;
    }

    public String getName() { return name; }

    public User getCreator() { return creator; }

    public List<User> getAccessedBy() { return accessedBy; }

    public void addAccessedBy(User user) { accessedBy.add(user); }
}
