package entity;

public class UserFactory {

    public static User create(String username, double balance, int totalbets, int gamesPlayed, String password) {
        // Hash the password here if needed
        return new User(username, balance, totalbets, gamesPlayed, password);
    }
}
