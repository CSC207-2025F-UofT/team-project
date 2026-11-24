package entity;
import java.util.List;

public class SideQuest {
    private final String name;
    private final String description;
    private final String photoUrl;
    private final String isIndoor;
    private final String budget;    // Split into four categories (free, less_than_twenty, less_than_fifty, over_fifty)
    private final String expectedTime; // under_10_minutes, under_30_minutes, under_60_minutes, over_60_minutes
    private final int exp;
    private final Location location;
    private final List<String> interests;

    public SideQuest(String name,
                     String description,
                     String photoUrl,
                     String isIndoor,
                     String budget,
                     String expectedTime,
                     int exp,
                     Location location,
                     List<String> interests) {

        this.name = name;
        this.description = description;
        this.photoUrl = photoUrl;
        this.budget = budget;
        this.isIndoor = isIndoor;
        this.expectedTime = expectedTime;
        this.exp = exp;
        this.location = location;
        this.interests = List.copyOf(interests);;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String isIndoor() {
        return isIndoor;
    }

    public String getBudget() {
        return budget;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public int getExp() {
        return exp;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getInterests() {
        return List.copyOf(interests);
    }
}