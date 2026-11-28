package use_case.meal_planning;

public class AddPlannedMealOption {

    private final String id;
    private final String name;

    public AddPlannedMealOption(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}