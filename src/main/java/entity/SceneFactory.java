package entity;

import java.util.List;

/**
 * Factory for creating Scene objects.
 */
public class SceneFactory {

    public Scene create(String name, List<ClickableObject> objects, String image) {
        return new Scene(name, objects, image);
    }
}
