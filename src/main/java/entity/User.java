package entity;

import java.util.ArrayList;

public class User {
    private String username;
    private String password:
    private ArrayList<Recipe> publishedRecipes;
    private ArrayList<Recipe> savedRecipes;
    priavte ArrayList<Review> reviews;

    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

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