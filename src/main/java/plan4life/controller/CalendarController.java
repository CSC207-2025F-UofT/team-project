package plan4life.controller;

import plan4life.use_case.generate_schedule.*;
import plan4life.use_case.lock_activity.*;

import java.util.Set;

public class CalendarController {
    private final GenerateScheduleInputBoundary generateScheduleInteractor;
    private final LockActivityInputBoundary lockActivityInteractor;

    public CalendarController(GenerateScheduleInputBoundary generateScheduleInteractor,
                              LockActivityInputBoundary lockActivityInteractor) {
        this.generateScheduleInteractor = generateScheduleInteractor;
        this.lockActivityInteractor = lockActivityInteractor;
    }

    public void generateSchedule(String routineDescription, String fixedActivities) {
        GenerateScheduleRequestModel request = new GenerateScheduleRequestModel(routineDescription, fixedActivities);
        generateScheduleInteractor.execute(request);
    }

    public void lockAndRegenerate(Set<String> lockedSlots) {
        LockActivityRequestModel request = new LockActivityRequestModel(lockedSlots);
        lockActivityInteractor.execute(request);
    }
}

