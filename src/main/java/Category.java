
public class Category {

    // Attributes
    private final int categoryID;
    private String name;
    private CategoryType type;
    private String color;
    private String icon;

    // Constructor
    public Category(int categoryID, String name,
                    CategoryType type, String color,
                    String icon) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Category name cannot be empty"
            );
        }

        this.categoryID = categoryID;
        this.name = name;
        this.type = type;
        this.color = color;
        this.icon = icon;
    }

    // Getters
    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getIcon() {
        return icon;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Category name cannot be empty"
            );
        }
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
