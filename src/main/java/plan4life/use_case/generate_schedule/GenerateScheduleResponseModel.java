package plan4life.use_case.generate_schedule;

import plan4life.entities.Schedule;

public class GenerateScheduleResponseModel {
    private final Schedule schedule;

    public GenerateScheduleResponseModel(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}

