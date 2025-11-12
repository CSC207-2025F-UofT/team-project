package entity;

public class LandmarkInfo {
    private final String description;
    private final String openHours;
    private final String imageUrl;

    public LandmarkInfo(String description, String openHours, String imageUrl) {
        this.description = description;
        this.openHours = openHours;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getOpenHours() {
        return openHours;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
