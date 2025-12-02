package api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple API client for accessing the Petfinder API.
 * This class currently provides a method to retrieve cat information
 * from the Petfinder animals endpoint.
 */
public class petfinder_api {

    /**
     * Sends a GET request to the Petfinder API and retrieves a list of cat data.
     *
     * @param accessToken The OAuth access token required by the Petfinder API.
     * @return A JSON string containing cat data returned by the API.
     * @throws Exception If the API request fails or the response cannot be read.
     */
    public static String getCats(String accessToken) throws Exception {
        final String url = "https://api.petfinder.com/v2/animals?type=cat";

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        // put access token in HTTP Header
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        return response.toString();
    }
}
