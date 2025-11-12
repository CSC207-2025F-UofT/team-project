package entities;

public class UserFactory {
    public User makeUser(String name, String lobbyID) {
        return new User(name, lobbyID);
    }
}
