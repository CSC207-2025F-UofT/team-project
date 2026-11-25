package user_case.Login;

import entity.User;

public interface LoginUserDataAccessInterface {
    boolean existByName(String username);
    void save(User user);

    User get(String username);
    boolean checkPassword(String username, String password);
    void setCurrentUsername(String name);
    String getCurrentUsername();
}
