package plan4life.use_case.lock_activity;

import plan4life.entities.Schedule;

public class LockActivityInteractor implements LockActivityInputBoundary {
    private final LockActivityOutputBoundary presenter;
    private final Schedule schedule;

    public LockActivityInteractor(LockActivityOutputBoundary presenter, Schedule schedule) {
        this.presenter = presenter;
        this.schedule = schedule;
    }

    @Override
    public void execute(LockActivityRequestModel requestModel) {
        // Logic: keep locked slots intact, shuffle others
        Schedule newSchedule = new Schedule();
        schedule.getActivities().forEach((time, act) -> {
            if (requestModel.getLockedSlots().contains(time)) {
                newSchedule.addActivity(time, act);
            }
        });
        // For simplicity, random-fill remaining slots
        newSchedule.populateRandomly();

        presenter.present(new LockActivityResponseModel(newSchedule));
    }
}

