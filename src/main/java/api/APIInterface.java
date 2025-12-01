package api;

import data_access.PetDataAccessInterface;
import entity.Pet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIInterface implements PetDataAccessInterface {
    final String API_KEY = "OsGIgKJNugsIPURr6oRDaTIe5ViaXvz3cFF6fihxgdCuwHQ4pM";
    final String API_SECRET = "o11EweL12UhdW7M11gCXoCKBPQGEphTy8FBwBT9x";

    private String getAccessToken() throws Exception {
        final String url = "https://api.petfinder.com/v2/oauth2/token";

        final URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setDoOutput(true);

        String body = "grant_type=client_credentials"
                + "&client_id=" + API_KEY
                + "&client_secret=" + API_SECRET;

        con.getOutputStream().write(body.getBytes());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        final JSONObject json = new JSONObject(response.toString());

        return json.getString("access_token");
    }

    private JSONArray getPetsFromApi() throws Exception {
        final String url = "https://api.petfinder.com/v2/animals";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        // put access token in HTTP Header
        con.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        final JSONObject json = new JSONObject(response.toString());
        final JSONArray pets = json.getJSONArray("animals");

        return pets;
    }

    @Override
    public List<Pet> getPets() throws Exception {
        JSONArray petsFromApi = getPetsFromApi();
        List<Pet> pets = new ArrayList<>();

        for (int i = 0; i < petsFromApi.length(); i++) {
            JSONObject petJson = petsFromApi.getJSONObject(i);

            int id = petJson.getInt("id");
            String name = petJson.getString("name");
            Pet.Age age = Pet.Age.valueOf(petJson.getString("age").toUpperCase());
            Pet.Gender gender = Pet.Gender.valueOf(petJson.getString("gender").toUpperCase());
            String species = petJson.getString("species");
            String breed = petJson.getJSONObject("breeds").optString("primary");
            String colour = petJson.getJSONObject("colors").optString("primary");
            Pet.Size size = Pet.Size.valueOf(petJson.getString("size").toUpperCase().replace(" ", "_"));
            String description = petJson.optString("description");
            String imageURL = "";
            JSONArray imageArray = petJson.getJSONArray("photos");
            if (!imageArray.isEmpty()) {
                imageURL = imageArray.getJSONObject(0).optString("medium");
            }

            Pet pet = new Pet(id, name, age, gender, species, breed, colour, size, description, imageURL);

            pets.add(pet);
        }

        return pets;
    }
}
