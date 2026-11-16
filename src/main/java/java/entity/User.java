package entity;

import java.util.ArrayList;
import java.util.List;
import java.entity.Document;

/**
 * An entity representing a user. Users have a username, list of documents they
 * created, and list of documents they have access to.
 */
public class User {

    private final String name;
    private final List<Document> documentsCreated = new ArrayList<>();
    private final List<Document> documentsAccessed = new ArrayList<>();

    /**
     * Creates a new user with the given non-empty name.
     * @param name the username
     * @throws IllegalArgumentException if the name is empty
     */
    public User(String name) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Document> getDocumentsCreated() { return documentsCreated; }

    public List<Document> getDocumentsAccessed() { return documentsAccessed; }

}