/// File: DBStudySetDataAccessObject.java
/// Description: Implement Study Set Data Access Interface
/// Wrote by Daniel

package data_access;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.StudyDeck;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Player;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.StudySet.DataAccessException;
import use_case.StudySet.StudySetDataAccessInterface;

import javax.swing.*;

// Example: https://github.com/CSC207-2025F-UofT/NoteApplication/blob/main/src/main/java/data_access/DBNoteDataAccessObject.java#L15

public class DBStudySetDataAccessObject implements StudySetDataAccessInterface {
    private static final OkHttpClient client = new OkHttpClient();
    private static final int SUCCESS_CODE = 200;
    private static final int API_KEY_ERROR = 401;
    public static final String ERROR_MESSAGE = "error";
    private static final String API_URL = "https://207.dbestserv.com";
    private static final String API_KEY = "?key=abc123";



    @Override
    public String testAPIConnection() throws DataAccessException {
        final String method = "/test-api";
        final Request request = new Request.Builder()
                .url(API_URL + method + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            final JSONObject responseJSON = new JSONObject(response.body().string());
            int statusCode = response.code();
            if (statusCode == SUCCESS_CODE) {
                return "Successfully connected to API Server!";
            } else if (statusCode == API_KEY_ERROR) {
                throw new DataAccessException("Key Error: " + responseJSON.getString(ERROR_MESSAGE));
            } else {
                throw new DataAccessException("API error: " + responseJSON.getString(ERROR_MESSAGE));
            }
        } catch (IOException | JSONException exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    @Override
    public ArrayList<HashMap<String, Integer>> getAllSetNameAndID() throws DataAccessException {
        return null;
    }

    @Override
    public StudyDeck getSetByName(String setName) throws DataAccessException {
        return null;
    }

    public static void main(String[] args) throws DataAccessException {
        DBStudySetDataAccessObject testObject = new DBStudySetDataAccessObject();
        System.out.println(testObject.testAPIConnection());
    }
}

