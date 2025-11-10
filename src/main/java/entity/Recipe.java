package entity;

import java.util.Date;
import java.util.List;

public class Recipe {

    private String recipeName;
    private int recipeId;
    private String description;
    private List<Ingredient> ingredients;
    private String category;
    private boolean shareable;
    private Date creationDate;
    private Date updateDate;
    private int views;
    private int saves;
    private List<Review> reviews;

    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

}
