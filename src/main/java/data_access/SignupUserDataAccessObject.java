package data_access;

import entity.User;
import org.json.JSONObject;
import use_case.DataAccessException;
import use_case.registration.signup.SignupUserDataAccessInterface;

import java.util.Map;

import static data_access.StaticMethods.makeApiRequest;

public class SignupUserDataAccessObject implements SignupUserDataAccessInterface {

    private final String apiKey;
    public SignupUserDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public boolean existsByName(String username) throws DataAccessException {
        final String method = "/api/exists-user";
        Map<String, String> params = Map.of("username", username);
        JSONObject response = makeApiRequest("GET", method, params, apiKey);
        return response.getBoolean("exists");
    }

    @Override
    public void save(User user) throws DataAccessException {
        final String method = "/api/update-user-data";
        Map<String, String> params = Map.of(
                "username", user.getUserName(),
                "level", String.valueOf(user.getLevel()),
                "points", String.valueOf(user.getExperiencePoints()),
                "answered", String.valueOf(user.getQuestionsAnswered()),
                "correct", String.valueOf(user.getQuestionsCorrect())
        );
        makeApiRequest("POST", method, params, apiKey);
    }
}
