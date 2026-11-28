package entity;

public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String password) {
        return new User(name, password);
    }
}
