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
    private final String id; // A unique ID for quests
    private final String isIndoor;
    private final String budget;    // Split into four categories (free, less_than_twenty, less_than_fifty, over_fifty)
    private final String expectedTime; // under_10_minutes, under_30_minutes, under_60_minutes, over_60_minutes
    private final int exp;
    private final List<double> coordinates;
    private final List<String> interests;

    public SideQuest(String id,
                     String name,
                     String isIndoor,
                     String budget,
                     String expectedTime,
                     int exp,
                     List<double> coordinates,
                     List<String> interests) {

        this.id = id;
        this.name = name;
        this.budget = budget;
        this.isIndoor = isIndoor;
        this.expectedTime = expectedTime;
        this.exp = exp;
        this.coordinates = List.copyOf(coordinates);
        this.interests = List.copyOf(interests);;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
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

    public List<double> getCoordinates() {
        return List.copyOf(coordinates);
    }

    public List<String> getInterests() {
        return List.copyOf(interests);
    }
}
