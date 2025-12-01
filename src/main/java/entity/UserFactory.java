package entity;

public class UserFactory {
    public User create(String name, String password) {
        return new User(0, name, "", User.idType.PHOTO_CARD, 0, "", "", password);
    }
}
