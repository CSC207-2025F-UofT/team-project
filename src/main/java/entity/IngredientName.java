package entity;

/**
 * The name of an ingredient. Equality is based on a normalized key so names
 * received from the UI and TheMealDB can be compared consistently.
 */
public final class IngredientName {
    private final String displayName;
    private final String key;

    /*
    TODO
     */
    private IngredientName(String displayName, String key) {
        this.displayName = displayName;
        this.key = key;
    }

    /**
     * Creates an ingredient name from user- or API-provided text.
     *
     * @param rawName the ingredient name
     * @throws IllegalArgumentException if the name is null or blank
     */
    public static IngredientName fromRawName(String rawName) throws IllegalArgumentException {
        if (rawName == null) {
            throw new IllegalArgumentException("ingredient name cannot be null");
        }

        String displayName = rawName.trim();
        String key = normalize(rawName);
        if (key.isEmpty()) {
            throw new IllegalArgumentException("ingredient name cannot be blank");
        }

        return new IngredientName(displayName, key);
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

    /*
    TODO
     */
    private static String normalize(String rawName) {
        return rawName
                .trim()
                .toLowerCase()
                .replace('_', ' ')
                .replaceAll("\\s+", " ");
    }

    /*
    TODO
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IngredientName)) {
            return false;
        }
        final IngredientName that = (IngredientName) other;
        return key.equals(that.key);
    }

    /*
    TODO
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /*
    TODO
     */
    @Override
    public String toString() {
        return displayName;
    }
}
