package use_case.signup;
import entities.User;

public interface SignupUserDataAccessInterface {
    boolean existsByName(String username);
    void save(User user);
}
