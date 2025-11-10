package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a scene in the game containing clickable objects.
 */
public class Scene {

    private final String name;
    private final List<ClickableObject> objects;
    private final String image;

    /**
     * Creates a new scene.
     * @param name the name of the scene
     * @param objects the list of clickable objects in this scene
     * @param image the path to the background image
     * @throws IllegalArgumentException if the name or image path is empty
     */
    public Scene(String name, List<ClickableObject> objects, String image) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Scene name cannot be empty");
        }
        if ("".equals(image)) {
            throw new IllegalArgumentException("Image path cannot be empty");
        }
        this.name = name;
        this.objects = new ArrayList<>(objects);
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public List<ClickableObject> getObjects() {
        return new ArrayList<>(objects);
    }

    public String getImage() {
        return image;
    }
}
