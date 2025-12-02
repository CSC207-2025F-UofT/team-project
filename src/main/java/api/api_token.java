package api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class api_token {

    public static String getAccessToken(String apiKey, String apiSecret) throws Exception {
        String url = "https://api.petfinder.com/v2/oauth2/token";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setDoOutput(true);

        String body = "grant_type=client_credentials"
                + "&client_id=" + apiKey
                + "&client_secret=" + apiSecret;

        con.getOutputStream().write(body.getBytes());

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Extract access_token from JSON manually
        String json = response.toString();
        String token = json.split("\"access_token\":\"")[1].split("\"")[0];

        return token;
    }


/*
    public static JSONArray extractPetsAsJsonArray(String token) {
        List<Map<String, Object>> pets = extractPetsAsMapList(token);
        return new JSONArray(pets); // 自动转成 JList / JMap 格式
    }


    private static List<Map<String, Object>> extractPetsAsMapList(String jsonResponse) {

        JSONObject root = new JSONObject(jsonResponse);
        JSONArray animals = root.getJSONArray("animals");

        List<Map<String, Object>> results = new ArrayList<>();

        for (int i = 0; i < animals.length(); i++) {

            JSONObject a = animals.getJSONObject(i);
            Map<String, Object> pet = new LinkedHashMap<>();

            // 基础字段
            pet.put("id", a.optLong("id"));
            pet.put("name", a.optString("name"));
            pet.put("age", a.optString("age"));
            pet.put("gender", a.optString("gender"));

            // breed
            JSONObject breeds = a.optJSONObject("breeds");
            pet.put("breed", breeds != null ? breeds.optString("primary") : null);

            // color
            JSONObject colors = a.optJSONObject("colors");
            pet.put("color", colors != null ? colors.optString("primary") : null);

            // body size
            pet.put("bodyType", a.optString("size"));

            // weight (API 没有，统一设为 0.0)
            pet.put("weight", 0.0);

            // region（state）
            JSONObject contact = a.optJSONObject("contact");
            if (contact != null) {
                JSONObject addr = contact.optJSONObject("address");
                pet.put("region", addr != null ? addr.optString("state") : null);
            } else {
                pet.put("region", null);
            }

            results.add(pet);
        }

        return results;
    }

 */



}
