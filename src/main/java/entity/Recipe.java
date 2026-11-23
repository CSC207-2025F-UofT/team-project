package entity;
import java.util.*;

public class Recipe {
    private final String id;
    private final String name;
    private final String category;
    private final String area;
    private final String instructions;
    private final String imageUrl;
    private final List<Ingredient> ingredients;

    public Recipe(String id, String name, String category, String area, String instructions
    , String imageUrl, List<Ingredient> ingredients){
        this.id = id;
        this.name = name;
        this.category = category;
        this.area = area;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
    }
    public String getID(){return id;}
    public String getName(){return name;}
    public String getCategory(){return category;}
    public String getArea(){return area;}
    public String getInstructions(){return instructions;}
    public String getImageUrl(){return imageUrl;}
    public List<Ingredient> getIngredients(){return ingredients;}
}
