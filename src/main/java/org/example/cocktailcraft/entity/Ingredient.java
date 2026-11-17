package org.example.cocktailcraft.entity;

public class Ingredient {
    private final String name;
    private final String category;
    private final String flavour;

    public Ingredient(String name, String category, String flavour) {
        this.name = name;
        this.category = category;
        this.flavour = flavour;
    }

    public String getName() {
            return name;
        }

    public String getCategory() {
            return category;
        }

    public String getFlavour() {
            return flavour;
        }
    }
