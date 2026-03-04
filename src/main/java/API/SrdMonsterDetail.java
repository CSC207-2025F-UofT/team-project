package API;

import entity.Item;
import entity.Spells;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * this class is for Game API, the methods inside of this class will generate the information for Monster class.
 */
public class SrdMonsterDetail implements MonsterDetail {
    /**
     * this method will get the url for races and spells from the api
     * @return a HashMap, the keys are "spells" and "races", the values are the corresponding urls
     * @throws MonsterNotFoundException throws the exception if nothing is founded
     */
    @Override
    public HashMap<String, String> getAllResourcesURL() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url("https://www.dnd5eapi.co/api/2014")
                .addHeader("Accept", "application/json")
                .build();
        HashMap<String, String> map = new HashMap<>();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            for (String str : responseBody.keySet()) {
                map.put(str, responseBody.getString(str));
            }
        } catch (Exception e) {
            throw new MonsterNotFoundException();
        }
        return map;
    }

    /**
     * this method will get the ArrayList for spells
     * @return a ArrayList
     * @throws MonsterNotFoundException throws the exception if nothing is founded
     */
    @Override
    public ArrayList<Spells> generateSpells() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://www.dnd5eapi.co%s", getAllResourcesURL().get("spells")))
                .addHeader("Accept", "application/json")
                .build();
        ArrayList<Spells> theSpell = new ArrayList<Spells>();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            JSONArray results = responseBody.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject spell = results.getJSONObject(i);
                String name = spell.getString("name");
                int dmg = spell.getInt("level");
                theSpell.add(new Spells(name, dmg));
            }
        } catch (Exception e) {
            throw new MonsterNotFoundException();
        }
        return theSpell;
    }

    /**
     * this method will get an Array for races
     * @throws MonsterNotFoundException throws the exception if nothing is founded
     */
    @Override
    public String[] generateRaces() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        Request request = new Request.Builder()
                .url(String.format("https://www.dnd5eapi.co%s", getAllResourcesURL().get("races")))
                .addHeader("Accept", "application/json")
                .build();
        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());
            JSONArray results = responseBody.getJSONArray("results");
            String[] list = new String[results.length()];
            for (int i = 0; i < results.length(); i++) {
                JSONObject races = results.getJSONObject(i);
                String name = races.getString("name");
                list[i] = name;
            }
            return list;
        } catch (Exception e) {
            throw new MonsterNotFoundException();
        }
    }

    /**
     * This method generates an array for items
     *
     * @throws MonsterNotFoundException throws the exception if nothing is founded.
     */
    @Override
    public ArrayList<Item> generateItems() {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(String.format("https://www.dnd5eapi.co%s", getAllResourcesURL().get("magic-items")))
                .addHeader("Accept", "application/json")
                .build();
        ArrayList<Item> items = new ArrayList<>();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responsebody = new JSONObject(response.body().string());
            JSONArray results = responsebody.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject item = results.getJSONObject(i);
                String name = item.getString("name");
                String type = item.getJSONObject("equipment_category").getString("index");

                items.add(new Item(name, type));
            }

        } catch (Exception exception) {
            throw new MonsterNotFoundException();
        }
        return items;
    }
}
