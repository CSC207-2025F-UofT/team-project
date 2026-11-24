package use_case.signup;
import entity.UserProfile;
/** Persistence boundary for registration. */
public interface SignUpUserDataAccessInterface {
    boolean emailExists(String email);
    int nextUserId();
    void saveUserProfile(UserProfile profile);
}
