package data_access;

import entity.user.User;
import entity.user.UserFactory;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
//import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.createAccount.CreateAccountUserDataAccessInterface;
import use_case.signIn.SignInUserDataAccessInterface;
import use_case.deleteFavouriteLocation.DeleteLocationUserDataAccessInterface;

import java.io.IOException;

/**
 * The DAO for user data.
 */
public class DBUserDataAccessObject implements CreateAccountUserDataAccessInterface, SignInUserDataAccessInterface, DeleteLocationUserDataAccessInterface {
    private static final int SUCCESS_CODE = 200;
    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String STATUS_CODE_LABEL = "status_code";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MESSAGE = "message";
    private final UserFactory userFactory;

    private String currentUsername;

    public DBUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
        // No need to do anything to reinitialize a user list! The data is the cloud that may be miles away.
    }

    @Override
    public String getCurrentUsername() {
        // return the currently logged-in username, or null if none
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    @Override
    public User get(String username) {
        // Make an API call to get the user object.
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/user?username=%s", username))
                .addHeader("Content-Type", CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                final JSONObject userJSONObject = responseBody.getJSONObject("user");
                final String name = userJSONObject.getString(USERNAME);
                final String password = userJSONObject.getString(PASSWORD);

                return userFactory.create(name, password, new java.util.ArrayList<>());
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean existsByName(String username) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        final Request request = new Request.Builder()
                .url(String.format("http://vm003.teach.cs.toronto.edu:20112/checkIfUserExists?username=%s", username))
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            //                throw new RuntimeException(responseBody.getString("message"));
            return responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        // POST METHOD
        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
        final JSONObject requestBody = new JSONObject();
        requestBody.put(USERNAME, user.getName());
        requestBody.put(PASSWORD, user.getPassword());
        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
        final Request request = new Request.Builder()
                .url("http://vm003.teach.cs.toronto.edu:20112/user")
                .method("POST", body)
                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
                .build();
        try {
            final Response response = client.newCall(request).execute();

            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
                // success!
            }
            else {
                throw new RuntimeException(responseBody.getString(MESSAGE));
            }
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

//    @Override
//    public void changePassword(User user) {
//        final OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//
//        // POST METHOD
//        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_JSON);
//        final JSONObject requestBody = new JSONObject();
//        requestBody.put(USERNAME, user.getName());
//        requestBody.put(PASSWORD, user.getPassword());
//        final RequestBody body = RequestBody.create(requestBody.toString(), mediaType);
//        final Request request = new Request.Builder()
//                .url("http://vm003.teach.cs.toronto.edu:20112/user")
//                .method("PUT", body)
//                .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_JSON)
//                .build();
//        try {
//            final Response response = client.newCall(request).execute();
//
//            final JSONObject responseBody = new JSONObject(response.body().string());
//
//            if (responseBody.getInt(STATUS_CODE_LABEL) == SUCCESS_CODE) {
//                // success!
//            }
//            else {
//                throw new RuntimeException(responseBody.getString(MESSAGE));
//            }
//        }
//        catch (IOException | JSONException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
}
