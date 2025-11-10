package entity;

import java.util.Date;
import java.util.List;

public class User {
    private String username;
    private String password:
    private ArrayList<Recipe> publishedRecipes;
    private ArrayList<Recipe> savedRecipes;
    priavte ArrayList<Review> reviews;

    public List<Recipe> getpublishedRecipes() {
        return publishedRecipes;
    }

    public List<Recipe> getsavedRecipes() {
        return savedRecipes;
    }

    public List<Review> getpublishedReviews() {
        return reviews;
    }
}