/// File: DBStudySetDataAccessObject.java
/// Description: Implement Study Set Data Access Interface
/// Wrote by Daniel

package data_access;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import entity.StudyDeck;
import org.json.JSONArray;
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

    /**
     * Helper method for making GET requests to the API.
     * Automatically handles HTTP errors, JSON parsing, and exceptions.
     *
     * @param method The endpoint path (e.g. "/test-api")
     * @return JSONObject parsed from the response body
     * @throws DataAccessException if the response code indicates an error or the request fails
     */
    private JSONObject makeApiRequest(String method) throws DataAccessException {
        final Request request = new Request.Builder()
                .url(API_URL + method + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            final JSONObject responseJSON = new JSONObject(responseBody);
            int statusCode = response.code();

            if (statusCode == SUCCESS_CODE) {
                return responseJSON;
            } else if (statusCode == API_KEY_ERROR) {
                throw new DataAccessException("Key Error: " + responseJSON.optString(ERROR_MESSAGE, "Unknown key error"));
            } else {
                throw new DataAccessException("API error: " + responseJSON.optString(ERROR_MESSAGE, "Unknown API error"));
            }
        } catch (IOException | JSONException E) {
            throw new DataAccessException("Request failed: " + E.getMessage());
        }
    }


    @Override
    public String testAPIConnection() throws DataAccessException {
        JSONObject responseJSON = makeApiRequest("/test-api");
        return "Successfully connected to API Server! Message: " + responseJSON.getString("message");
    }

    @Override
    public HashMap<String, Integer> getAllSetNameAndID() throws DataAccessException {
        final String method = "/api/get-all-study-set-name-and-id";
        JSONObject responseJSON = makeApiRequest(method);
        HashMap<String, Integer> deck = new HashMap<>();
        JSONArray titles = responseJSON.getJSONArray("titles");
        for (int i = 0; i < titles.length(); i++) {
            JSONObject pair = titles.getJSONObject(i);
            String title = pair.keys().next();
            int id = pair.getInt(title);
            deck.put(title, id);
        }
        return deck;
    }
    // TODO: Update convention: StudySet -> Deck, Question -> card, name -> title
    @Override
    public StudyDeck getSetByName(String setName) throws DataAccessException {
        return null;
    }

    public static void main(String[] args) throws DataAccessException {
        DBStudySetDataAccessObject testObject = new DBStudySetDataAccessObject();
        System.out.println(testObject.testAPIConnection());
    }
}

