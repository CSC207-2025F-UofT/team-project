package plan4life.use_case.lock_activity;

import plan4life.entities.Schedule;

public class LockActivityResponseModel {
    private final Schedule updatedSchedule;

    public LockActivityResponseModel(Schedule updatedSchedule) {
        this.updatedSchedule = updatedSchedule;
    }

    public Schedule getUpdatedSchedule() {
        return updatedSchedule;
    }
}

