package use_case.add_side_quest;
import entity.Location;
import java.util.List;
import java.util.ArrayList;

public class AddSideQuestInputData {
    private final String questDescription;
    private final String isIndoor;
    private final double budget;
    private final double expectedTime;
    private final int exp;
    private final Location location;
    private final List<String> interests;

    public AddSideQuestInputData(String questDescription,
                                 String isIndoor,
                                 double budget,         // This is in CAD
                                 double expectedTime,   // This is in minutes
                                 int exp,
                                 Location location,
                                 List<String> interests) {
        this.questDescription = questDescription;
        this.isIndoor = isIndoor;
        this.budget = budget;
        this.expectedTime = expectedTime;
        this.exp = exp;
        this.location = location;
        this.interests = interests == null ? new ArrayList<>() : new ArrayList<>(interests);
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public String getIsIndoor() {
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