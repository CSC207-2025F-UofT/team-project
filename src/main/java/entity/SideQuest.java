package entity;
import java.util.List;

//From Pauline: Please add the following attributes and methods:
//IsStarred, not a final attribute is set by the user by clicking a button (aka I believe this should be a public attribute) 
    //1. Any quest regardless of its progress level can be starred and can be unstarred at any time. 
    //2. The default for any side quest will be unstarred. 
//ProgressLevel, not a final attribute is set by the user however these following business rules follow
    // 1.  There are only 4 statuses: Not Started, In progress, Completed, Cancelled
    // 2.  Once a side quest is completed or cancelled is cannot be undone by the user
    // 3.  A "In progress" side quest will remain in progress until it is cancelled or completed

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
