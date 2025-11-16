package entity;

import java.util.Date;
import java.util.List;

public class Recipe {

    public enum Status {
        DRAFT,
        PUBLISHED,
    }

    private String recipeId;
    private String authorId;

    private String title;
    private String description;
    private List<Ingredient> ingredients;
    private String imagePath;

    private String category;
    private List<String> tags;
    private List<Review> reviews;

    private int views;
    private int saves;

    private boolean shareable;
    private Date creationDate;
    private Date updateDate;
    private Status status;

    public Recipe(String recipeId,
                  String authorId,
                  String title,
                  String description,
                  List<Ingredient> ingredients,
                  String category,
                  List<String> tags,
                  Status status,
                  Date creationDate,
                  Date updateDate,
                  String imagePath
                  ) {
        this.recipeId = recipeId;
        this.authorId = authorId;

        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.imagePath = imagePath;

        this.category = category;
        this.tags = tags;
        this.status = status;
        this.creationDate = creationDate;
        this.updateDate = updateDate;

        this.views = 0;
        this.saves = 0;

    }

    public String getRecipeId(){
        return this.recipeId;
    }

    public String getAuthorId(){
        return this.authorId;
    }

    public String getTitle(){
        return this.title;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public String getDescription(){
        return this.description;
    }

    public List<Ingredient> getIngredients(){
        return this.ingredients;
    }

    public Status getStatus(){
        return this.status;
    }

}
