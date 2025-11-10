package use_case.game;

import entity.ClickableObject;

import java.util.List;

/**
 * Output data for the Click Button use case.
 */
public class GameOutputData {

    private String backgroundImage;
    private List<ClickableObject> clickableObjects;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public List<ClickableObject> getClickableObjects() {
        return clickableObjects;
    }

    public void setClickableObjects(List<ClickableObject> clickableObjects) {
        this.clickableObjects = clickableObjects;
    }
}
