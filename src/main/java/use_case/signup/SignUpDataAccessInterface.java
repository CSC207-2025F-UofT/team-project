package use_case.signup;
import entity.UserProfile;

public interface SignUpDataAccessInterface {
    int nextUserId();
    void saveUserProfile(UserProfile profile);
    boolean existUserName(String userName);
}