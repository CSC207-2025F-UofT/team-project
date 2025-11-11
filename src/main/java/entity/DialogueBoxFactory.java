package entity;

import java.util.List;

/**
 * Factory for creating DialogueBox objects.
 */
public class DialogueBoxFactory {
    public DialogueBox create(String name, List<ClickableObject> options, String image, int id) {
        return new DialogueBox(name,  options, image, id);
    }
}
