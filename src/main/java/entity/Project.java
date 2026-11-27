package entity;

public class Project {
    private final long id; // unique id
    private String name;
    private boolean isPublished;
    private Canvas canvas;
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

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be empty.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void updateIsPublished() {
        isPublished = !isPublished;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public ColourPalette getPalette() {
        return palette;
    }

    public void setPalette(List<String> newColours) {
        palette.setColours(newColours);
    }

    public void isEmpty() {
        return canvas.isEmpty();
    }
}
