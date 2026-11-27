package data_access;

import entity.Pet;
import org.json.JSONObject;

public class PetJsonMapper {

    public static JSONObject toJson(Pet pet) {
        JSONObject json = new JSONObject();

        json.put("owner", pet.getOwnerUsername());
        json.put("name", pet.getName());
        json.put("species", pet.getSpecies().name());
        json.put("imageUrl", pet.getImageUrl());
        json.put("hunger", pet.getHunger());
        json.put("happiness", pet.getHappiness());
        json.put("health", pet.getHealth());

        return json;
    }

    public static Pet fromJson(JSONObject json) {
        return new Pet(
                json.getString("owner"),
                json.getString("name"),
                Pet.Species.valueOf(json.getString("species")),
                json.getString("imageUrl"),
                json.getInt("hunger"),
                json.getInt("happiness"),
                json.getInt("health")
        );
    }
}