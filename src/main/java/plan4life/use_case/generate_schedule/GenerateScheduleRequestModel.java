package plan4life.use_case.generate_schedule;

public class GenerateScheduleRequestModel {
    private final String routineDescription;
    private final String fixedActivities;

    public GenerateScheduleRequestModel(String routineDescription, String fixedActivities) {
        this.routineDescription = routineDescription;
        this.fixedActivities = fixedActivities;
    }

    public String getRoutineDescription() {
        return routineDescription;
    }

    public String getFixedActivities() {
        return fixedActivities;
    }
}
