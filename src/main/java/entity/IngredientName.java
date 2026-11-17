package entity;

/**
 * The name of an ingredient. Equality is based on a normalized key so names
 * received from the UI and TheMealDB can be compared consistently.
 */
public final class IngredientName {
    private final String displayName;
    private final String key;

    /**
     * Creates an ingredient name from user- or API-provided text.
     *
     * @param rawName the ingredient name
     * @throws IllegalArgumentException if the name is null or blank
     */
    public IngredientName(String rawName) {
        if (rawName == null) {
            throw new IllegalArgumentException("ingredient name cannot be null");
        }

        displayName = rawName.trim();
        key = normalize(rawName);
        if (key.isEmpty()) {
            throw new IllegalArgumentException("ingredient name cannot be blank");
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getKey() {
        return key;
    }

    private static String normalize(String rawName) {
        return rawName
                .trim()
                .toLowerCase()
                .replace('_', ' ')
                .replaceAll("\\s+", " ");
    }

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

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return displayName;
    }
}
