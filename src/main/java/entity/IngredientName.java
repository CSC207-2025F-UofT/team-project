package entity;

import java.util.HashMap;

/*
    The name object of the ingredient, merged ingredient naming alias.
 */
public class IngredientName {
    private final String displayName;
    private final String key;

    private static HashMap<String, String> aliasMapping;

    /*
    TODO
     */
    private IngredientName(String displayName, String key) {
        this.displayName = displayName;
        this.key = key;
    }

    /*
    TODO
     */
    public String getDisplayName() {
        return displayName;
    }

    /*
    TODO
     */
    public String getKey() {
        return key;
    }
}
