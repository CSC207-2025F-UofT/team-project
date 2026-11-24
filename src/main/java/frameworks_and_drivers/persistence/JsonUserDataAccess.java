package frameworks_and_drivers.persistence;
import entity.UserProfile;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.login.LoginDataAccessInterface;
import use_case.signup.SignUpDataAccessInterface;

/** Single JSON gateway for signup and login; stores password hashes, never raw passwords. */
public final class JsonUserDataAccess implements SignUpDataAccessInterface, LoginDataAccessInterface {
    @Override
    public boolean emailExists(String email) {
        return false;
    }

    @Override
    public int nextUserId() {
        return 0;
    }

    @Override
    public void saveUserProfile(UserProfile profile) {

    }

    @Override
    public UserProfile findUserByUsername(String username) {
        return null;
    }

    @Override
    public boolean passwordMatches(UserProfile userProfile, String rawPassword) {
        return false;
    }
}