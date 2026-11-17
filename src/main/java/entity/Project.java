package entity;

public class Project {
    private final long id;
    private String name;
    public Canvas canvas; // temp public see how we implement others
    public boolean isPublished;
    private ColourPalette palette;

    public Project(long id, String name) {
        // generate a new unique project id
        this.id = id;
        canvas = new Canvas();
        isPublished = false;
        palette = new ColourPalette();

        if (name.isEmpty()) {
            this.name = "My Project";
        }
        else {
            this.name = name;
        }
    }

    public long getId() {
        return id;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void publish() {
        isPublished = !isPublished;
    }

    public void emptyCanvas() {
        canvas = new Canvas();
    }

    public void emptyPalette() {
        palette = new ColourPalette();
    }

    public ColourPalette getPalette() {
        return palette;
    }
}
