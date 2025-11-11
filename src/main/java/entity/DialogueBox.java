package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dialogue box that has dialogue options in it.
 */

public class DialogueBox extends Scene{

    private final String name;
    private final List<ClickableObject> options;
    private final String image;
    private final int id;

    public DialogueBox(String name, List<ClickableObject> options, String image, int id) {
        super(name, options, image);
        if ("".equals(name)) {
            throw new IllegalArgumentException("DialogueBox name cannot be empty");
        }
        this.name = name;
        this.options = new ArrayList<>(options);
        this.image = image;
        this.id = id;
    }
    public String getName() {return name;}
    public List<ClickableObject> getOptions() {return options;}
    public String getImage() {return image;}
    public int getId() {return id;}
}
