package entity;

public class UserFactory {
    public UserProfile create(int userID, String name,
                              String email, String passwordHash) {

        return new UserProfile(userID, name, email, passwordHash);
    }
}
