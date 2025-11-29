package entity;

public class UserFactory_k {
    public User_k create(String name, String password) {
        return new User_k(0, name, "", User_k.idType.PHOTO_CARD, 0, "", "", password);
    }
}
