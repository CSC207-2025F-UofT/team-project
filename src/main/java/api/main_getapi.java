package api;

import org.json.JSONArray;
import org.json.JSONObject;

/*
This token is not code as the format of previous lab, I recommend use previous format rewrite this.
 */

public class main_getapi {
    public static void main(String[] args) throws Exception {

        String API_KEY = "OsGIgKJNugsIPURr6oRDaTIe5ViaXvz3cFF6fihxgdCuwHQ4pM";
        String API_SECRET = "o11EweL12UhdW7M11gCXoCKBPQGEphTy8FBwBT9x";

        // get access token
        String accessToken = api_token.getAccessToken(API_KEY, API_SECRET);
        System.out.println("Access token = " + accessToken);

        String token = petfinder_api.getCats(accessToken);
        JSONObject json = new JSONObject(token);

        // get array of animal
        JSONArray animals = json.getJSONArray("animals");

        // print name of 0
        JSONObject first = animals.getJSONObject(0);
        // primary color
        JSONObject colors = first.getJSONObject("colors");
        String primary_color = colors.optString("primary");
        // primary breed
        JSONObject breeds = first.getJSONObject("breeds");
        String primary_breed = breeds.optString("primary");
        // address city
        JSONObject contact = first.getJSONObject("contact");
        JSONObject address = contact.getJSONObject("address");
        String address_city = address.optString("city");
        String address_country = address.optString("country");
        /*
        After getting the first animal, input the info to the class
        We can make a button for admin, add pet: input cat/dog, (input info we need)
        return: show the info we need (of the first animal in response) into the form, click add.
        if pet id already in our file, get a new one. Or check no duplication when we get the pet.
         */
        System.out.println("Species:" + first.optString("species"));
        System.out.println("First cat name: " + first.optString("name"));
        System.out.println("ID: " + first.optInt("id"));
        System.out.println("Gender:" + first.optString("gender"));
        System.out.println("Size:" + first.optString("size"));
        System.out.println("Age: " + first.optString("age"));
        System.out.println("Description:" + first.optString("description"));
        System.out.println("Primary Color:" + primary_color);
        System.out.println("Primary Breed:" + primary_breed);
        System.out.println("Address:" + address_city + ", " + address_country);

    }

}
