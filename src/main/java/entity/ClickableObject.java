package entity;

/**
 * Represents a clickable object in the game that can be interacted with.
 */
public class ClickableObject {

    private final String name;
    private final int coordinateX;
    private final int coordinateY;
    private final String image;
    private Boolean collectable=false ;

    /**
     * Creates a new clickable object.
     * @param name the name of the object
     * @param coordinateX the x coordinate of the object
     * @param coordinateY the y coordinate of the object
     * @param image the path to the image asset
     * @throws IllegalArgumentException if the name or image path is empty
     */
    public ClickableObject(String name, int coordinateX, int coordinateY, String image, Boolean collectable) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Object name cannot be empty");
        }
        if ("".equals(image)) {
            throw new IllegalArgumentException("Image path cannot be empty");
        }
        this.name = name;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.image = image;
        this.collectable = collectable;
    }

    public String getName() {
        return name;
    }

    public int getCoordinateX() {
        return coordinateX;
    }

    public int getCoordinateY() {
        return coordinateY;
    }

    public String getImage() {
        return image;
    }

    public boolean isCollectable() { return collectable; }
}
