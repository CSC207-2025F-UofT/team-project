package entity;

/**
 * Factory for creating ClickableObject objects.
 */
public class ClickableObjectFactory {

    public ClickableObject create(String name, int coordinateX, int coordinateY, String image, Boolean collectable) {
        return new ClickableObject(name, coordinateX, coordinateY, image, collectable);
    }
}
