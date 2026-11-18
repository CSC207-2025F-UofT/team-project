package use_case.add_clothing;

/**
 * What the user typed and picked on the Add Clothing form.
 */
public class AddClothingInputData {
    private final String name;
    private final String category;
    private final String subCategory;
    private final String color;
    private final String style;
    private final boolean layer;
    private final int weatherRating;
    private final String imageSourcePath;

    public AddClothingInputData(String name, String category, String subCategory, String color,
                                String style, boolean layer, int weatherRating,
                                String imageSourcePath) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.color = color;
        this.style = style;
        this.layer = layer;
        this.weatherRating = weatherRating;
        this.imageSourcePath = imageSourcePath;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
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

    public String getImageSourcePath() {
        return imageSourcePath;
    }
}
