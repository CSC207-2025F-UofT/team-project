package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity representing a user. Users have a username, list of documents they
 * created, and list of documents they have access to.
 */
public class User {

    private final String name;
    private List<Document> documentsCreated = new ArrayList<>();
    private List<Document> documentsAccessed = new ArrayList<>();

    /**
     * Creates a new user with the given non-empty name.
     * @param name the username
     * @throws IllegalArgumentException if the name is empty
     */
    public User(String name) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.name = name;
    }

    /**
     * Creates a new user with the given non-empty name, list of documents created, and
     * list of documents accessed.
     * @param name the username
     * @param documentsCreated the list of documents created
     * @param documentsAccessed the list of documents accessed
     * @throws IllegalArgumentException if the name is empty
     */
    public User(String name, List<Document> documentsCreated, List<Document> documentsAccessed) {
        this(name);
        this.documentsCreated = documentsCreated;
        this.documentsAccessed = documentsAccessed;
        for (Document documentAccessed : documentsAccessed) {
            if (!documentAccessed.getAccessedBy().contains(this)) {
                documentAccessed.addAccessedBy(this);
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<Document> getDocumentsCreated() { return documentsCreated; }

    public List<Document> getDocumentsAccessed() { return documentsAccessed; }

    public void addDocumentCreated(Document document) {
        if (!documentsCreated.contains(document)) {
            documentsCreated.add(document);
        }
    }

    public void addDocumentAccessed(Document document) {
        if (!documentsAccessed.contains(document)) {
            documentsAccessed.add(document);
        }
        if (!document.getAccessedBy().contains(this)) {
            document.addAccessedBy(this);
        }
    }

}