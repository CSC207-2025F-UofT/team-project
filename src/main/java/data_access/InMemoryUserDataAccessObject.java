package data_access;

import entity.User;
public class InMemoryUserDataAccessObject {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void clearCurrentUser() {
        this.currentUser = null;
    }

}
