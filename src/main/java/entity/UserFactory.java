package entity;

public class UserFactory {
    public UserProfile create(int userID, String name, String passwordHash) {

        return new UserProfile(userID, name, passwordHash);
    }
}
