package use_case.pet_management;

public class AddPetInputData {
    public final int id;
    public final String name;
    public final String age;
    public final String gender;
    public final String species;
    public final String breed;
    public final String colour;
    public final String size;
    public final String description;
    public final String imageURL;

    public AddPetInputData(int id,
                           String name,
                           String age,
                           String gender,
                           String species,
                           String breed,
                           String colour,
                           String size,
                           String description,
                           String imageURL) {
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
    }
}
