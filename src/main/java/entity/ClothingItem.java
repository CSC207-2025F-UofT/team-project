package entity;

/**
 * One piece of clothing in the user's closet (name, category, color,
 * material, vibe, warmth, layer level, see-through, image path).
 */
public class ClothingItem {
    public static final int MIN_WEATHER_RATING = -10;
    public static final int MAX_WEATHER_RATING = 10;
    private static final String DRESS = "Dress";

    private final String id;
    private final String name;
    private final Category category;
    private final String subCategory;
    private final String color;
    private final String style;
    private final boolean layer;
    private final int weatherRating;
    private final String imagePath;

    public ClothingItem(String id, String name, Category category, String subCategory,
                        String color, String style, boolean layer, int weatherRating,
                        String imagePath) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.color = color;
        this.style = style;
        this.layer = layer;
        this.weatherRating = weatherRating;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getColor() {
        return color;
    }

    public String getStyle() {
        return style;
    }

    public boolean isLayer() {
        return layer;
    }

    public int getWeatherRating() {
        return weatherRating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isDress() {
        return category == Category.TOP && DRESS.equalsIgnoreCase(subCategory);
    }
}