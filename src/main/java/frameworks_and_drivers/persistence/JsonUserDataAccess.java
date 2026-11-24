package frameworks_and_drivers.persistence;
import entity.UserProfile;
import frameworks_and_drivers.PasswordUtils;
import use_case.signup.SignUpDataAccessInterface;
import use_case.login.LoginDataAccessInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

/** Single JSON gateway for signup and login; stores password hashes, never raw passwords. */
public final class JsonUserDataAccess implements SignUpDataAccessInterface, LoginDataAccessInterface {
    private final Path filePath;

    public JsonUserDataAccess() {this(Path.of("data/Profiles.json"));
    }
    public JsonUserDataAccess(Path filePath) {
        this.filePath = filePath;
    }
    private JSONObject read() throws IOException {
        if (!Files.exists(filePath)) throw new IOException("Missing profile database: " + filePath);
        return new JSONObject(Files.readString(filePath));
    }

    private void write(JSONObject data) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) Files.createDirectories(parent);
        Files.writeString(filePath, data.toString(2));
    }
    private JSONObject find(String key,String value){
        try {
            JSONArray profiles = read().getJSONArray("profiles");

            for (int i = 0; i < profiles.length(); i++) {
                JSONObject user = profiles.getJSONObject(i);

                if (user.optString(key).equalsIgnoreCase(value)) {
                    return user;
                }
            }

            return null;

        } catch (IOException e) {
            throw new IllegalStateException("Cannot read profiles", e);
        }
    }
    @Override
    public boolean emailExists(String email) {
        return find("email", email) != null;
    }

    @Override
    public int nextUserId() {
            try {
                return read().getInt("nextUserId");
            } catch (IOException e) {
                throw new IllegalStateException("Cannot get user ID", e);
            }
    }

    @Override
    public void saveUserProfile(UserProfile profile) {
        try {
            JSONObject data = read();
            JSONArray profiles = data.getJSONArray("profiles");

            JSONObject user = new JSONObject();

            user.put("id", profile.getId());
            user.put("name", profile.getName());
            user.put("email", profile.getEmail());
            user.put("passwordHash", profile.getPasswordHash());
            user.put("createdAt", profile.getCreatedAt().toString());
            user.put("accounts", new JSONArray());

            profiles.put(user);

            data.put("nextUserId", profile.getId() + 1);

            write(data);

        } catch (IOException e) {
            throw new IllegalStateException("Cannot save profile", e);
        }
    }

    @Override
    public UserProfile findUserByUsername(String username) {
        JSONObject user = find("name", username);
        if (user == null) {
            return null;
        }
        return new UserProfile(user.getInt("id"),
                user.getString("name"),
                user.getString("email"),
                user.optString("passwordHash", ""),
                LocalDateTime.parse(user.getString("createdAt"))
        );
    }
    @Override
    public boolean existUserName(String username) {
        return find("name", username) != null;
    }

    @Override
    public boolean passwordMatches(UserProfile userProfile, String rawPassword) {
        return userProfile != null && PasswordUtils.verifyPassword(userProfile.getPasswordHash(), rawPassword);
    }
}