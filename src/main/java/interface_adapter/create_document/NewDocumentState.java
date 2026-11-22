package interface_adapter.create_document;

import entity.User;

/**
 * The state for the New Document View Model.
 */
public class NewDocumentState {
    private String name = "";
    private String nameError;
    private User creator = new User("name");

    public String getName() {
        return name;
    }

    public String getNameError() {
        return nameError;
    }

    public User getCreator() { return creator; }

    public void setName(String name) { this.name = name; }

    public void setNameError(String nameError) { this.nameError = nameError; }

    public void setCreator(User creator) { this.creator = creator; }

    @Override
    public String toString() {
        return "NewDocumentState{"
                + "name='" + name + '\''
                + ", creator='" + creator.getName() + '\''
                + '}';
    }
}