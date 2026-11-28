package entity;

public class UserFactory {

    public User create(String username, String passwordHash, String email, String salt){
        return new User(username, passwordHash, email, salt);
    }
}
