package use_case.signup;
import entity.UserProfile;

public interface SignUpDataAccessInterface {
    boolean emailExists(String email);
    int nextUserId();
    void saveUserProfile(UserProfile profile);
}