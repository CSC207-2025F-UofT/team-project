package use_case.add_side_quest;
import entity.Location;
import java.util.List;
import java.util.ArrayList;

public class AddSideQuestInputData {
    private final String questDescription;
    private final String photoUrl;
    private final String isIndoor;
    private final double budget;
    private final double expectedTime;
    private final int exp;
    private final Location location;
    private final List<String> interests;

    public AddSideQuestInputData(String id, String name, String questDescription, String photoUrl, String isIndoor,
                                 double budget, double expectedTime, int exp, Location location,
                                 List<String> interests) {
        this.id = id;
        this.name = name;
        this.questDescription = questDescription;
        this.photoUrl = photoUrl;
        this.isIndoor = isIndoor;
        this.budget = budget;
        this.expectedTime = expectedTime;
        this.exp = exp;
        this.location = location;
        this.interests = new ArrayList<>(interests);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public String getPhotoUrl() {
        return photoUrl;
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
        return new ArrayList<>(interests);
    }
}