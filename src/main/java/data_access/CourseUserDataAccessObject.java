package data_access;

import entity.User;
import entity.UserFactory;
import okhttp3.*;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;

import java.io.IOException;

public class CourseUserDataAccessObject
        implements LoginUserDataAccessInterface {

    private static final String BASE_URL =
            "https://csc207-20269-vm.teach.cs.toronto.edu";

    private final OkHttpClient client = new OkHttpClient();

    private final UserFactory userFactory;

    private String currentUsername;

    public CourseUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public boolean existsByName(String username) {

        HttpUrl url = HttpUrl.parse(
                        BASE_URL + "/checkIfUserExists")
                .newBuilder()
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        JSONObject result = executeRequest(request);

        return result.getInt("status_code") == 200;
    }

    @Override
    public User get(String username) {

        HttpUrl url = HttpUrl.parse(BASE_URL + "/user")
                .newBuilder()
                .addQueryParameter("username", username)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        JSONObject result = executeRequest(request);

        if (result.getInt("status_code") != 200) {
            throw new RuntimeException(
                    result.optString("message", "User not found."));
        }

        JSONObject user = result.getJSONObject("user");

        return userFactory.create(
                user.getString("username"),
                user.getString("password"));
    }

    public void save(User user) {

        JSONObject json = new JSONObject();

        json.put("username", user.getName());
        json.put("password", user.getPassword());

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/user")
                .post(body)
                .build();

        JSONObject result = executeRequest(request);

        if (result.getInt("status_code") != 200) {
            throw new RuntimeException(
                    result.optString("message", "Could not save user."));
        }
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    private JSONObject executeRequest(Request request) {

        try (Response response =
                     client.newCall(request).execute()) {

            if (response.body() == null) {
                throw new RuntimeException("Empty API response.");
            }

            return new JSONObject(response.body().string());

        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not connect to the User API.", e);
        }
    }
}
