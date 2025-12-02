package entity;

import org.json.JSONObject;

public class Pet {
    private final int id;
    private final String name;
    public enum Age { BABY, YOUNG, ADULT, SENIOR }
    private final Age age;
    public enum Gender { MALE, FEMALE }
    private final Gender gender;
    private final String species;
    private final String breed;
    private final String colour;
    public enum Size {
        SMALL, MEDIUM, LARGE, EXTRA_LARGE;
    }
    private final Size size;
    private final String description;
    private final String imageURL;
    private boolean isAdopted;


    public Pet(int id, String name, Age age, Gender gender, String species, String breed, String colour, Size size, String description, String imageURL) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.species = species;
        this.breed = breed;
        this.colour = colour;
        this.size = size;
        this.description = description;
        this.imageURL = imageURL;
        this.isAdopted = false;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public Age getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBreed() {
        return breed;
    }

    public String getColor() {
        return colour;
    }

    public String getDescription() {
        return description;
    }

    public Size getSize() {
        return size;
    }

    public String getImageURL() {
        return imageURL;
    }

    public boolean isAdopted() {
        return isAdopted;
    }

    public void setAdopted() {
        isAdopted = true;
    }

    public Pet(JSONObject petJson) {
        this.id = petJson.getInt("id");
        this.name = petJson.getString("name");
        this.species = petJson.getString("species");
        this.age = petJson.getEnum(Age.class,"age");
        this.gender = petJson.getEnum(Gender.class, "gender");
        this.breed = petJson.getString("breed");
        this.colour = petJson.getString("colour");
        this.size = petJson.getEnum(Size.class, "size");
        this.description = petJson.getString("description");
        this.imageURL = petJson.getString("imageURL");
        this.isAdopted = petJson.getBoolean("isAdopted");
    }

    public JSONObject toJson() {
        JSONObject petJson = new JSONObject();

        petJson.put("id", id);
        petJson.put("name", name);
        petJson.put("species", species);
        petJson.put("age", age);
        petJson.put("gender", gender);
        petJson.put("breed", breed);
        petJson.put("colour", colour);
        petJson.put("size", size.toString());
        petJson.put("description", description);
        petJson.put("imageURL", imageURL);
        petJson.put("isAdopted", isAdopted);

        return petJson;
    }
}
