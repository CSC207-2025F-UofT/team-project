package entity;

public class Project {
    private final long id; // unique id
    private String name;
    public Canvas canvas;
    public boolean isPublished;
    private ColourPalette palette;

    public Project(long id, String name) {
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
