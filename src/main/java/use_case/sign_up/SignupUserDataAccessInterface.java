package use_case.sign_up;
import entity.User;

public interface SignupUserDataAccessInterface {
    boolean existsByName(String username);
    void save(User user);
}
