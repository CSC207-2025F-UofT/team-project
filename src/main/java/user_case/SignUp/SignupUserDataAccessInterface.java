package user_case.SignUp;
import entity.User;

public interface SignupUserDataAccessInterface {
    boolean existByName(String username);
    void save(User user);
}
