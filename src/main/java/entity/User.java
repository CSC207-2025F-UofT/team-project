package entity;

import java.util.ArrayList;

public class User {
    private String username;
    private String password:
    private ArrayList<Recipe> publishedRecipes;
    private ArrayList<Recipe> savedRecipes;
    priavte ArrayList<Review> reviews;

    public ArrayList<Recipe> getpublishedRecipes() {
        return publishedRecipes;
    }

    public ArrayList<Recipe> getsavedRecipes() {
        return savedRecipes;
    }

    public ArrayList<Review> getpublishedReviews() {
        return reviews;
    }
}