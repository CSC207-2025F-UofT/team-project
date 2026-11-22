package entity;
import java.util.List;

public class SideQuest {
    private final String name;
    private final String description;
    private final String isIndoor;
    private final double budget;
    private final double expectedTime;
    private final int exp;
    private final Location location;
    private final List<String> interests;

    public SideQuest(String name,
                     String description,
                     String isIndoor,
                     double budget,
                     double expectedTime,
                     int exp,
                     Location location,
                     List<String> interests) {

        this.name = name;
        this.description = description;
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

    public String isIndoor() {
        return isIndoor;
    }

    public double getBudget() {
        return budget;
    }

    public double getExpectedTime() {
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
