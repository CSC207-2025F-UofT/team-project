package plan4life.use_case.lock_activity;

import plan4life.entities.Schedule;
import plan4life.data_access.ScheduleDataAccessInterface;

public class LockActivityInteractor implements LockActivityInputBoundary {
    private final LockActivityOutputBoundary presenter;
    private final ScheduleDataAccessInterface scheduleDAO;

    public LockActivityInteractor(LockActivityOutputBoundary presenter, ScheduleDataAccessInterface scheduleDAO) {
        this.presenter = presenter;
        this.scheduleDAO = scheduleDAO;
    }

    @Override
    public void execute(LockActivityRequestModel requestModel) {
        int scheduleId = requestModel.getScheduleId();
        Schedule existing = scheduleDAO.getSchedule(scheduleId);
        if (existing == null) {
            // If not found, create a fresh schedule (or handle error via presenter)
            Schedule created = new Schedule(scheduleId, "week");
            created.populateRandomly();
            scheduleDAO.saveSchedule(created);
            presenter.present(new LockActivityResponseModel(created));
            return;
        }

        // Build new schedule preserving locked slots
        Schedule newSchedule = new Schedule(scheduleId, existing.getType());
        // Copy locked activities (if present) from existing schedule
        existing.getLockedSlotKeys().forEach(newSchedule::lockSlotKey);
        newSchedule.copyLockedActivitiesFrom(existing);

        // Lock any slots that the user selected in case they selected new ones
        if (requestModel.getLockedSlots() != null) {
            requestModel.getLockedSlots().forEach(newSchedule::lockSlotKey);
            // copy activities for keys (if existing)
            requestModel.getLockedSlots().forEach(key -> {
                String act = existing.getActivities().get(key);
                if (act != null) newSchedule.addActivity(key, act);
            });
        }

        // Populate remaining slots (the generator / randomizer should avoid locked keys)
        newSchedule.populateRandomly(newSchedule.getLockedSlotKeys());

        // Save and present
        scheduleDAO.saveSchedule(newSchedule);
        presenter.present(new LockActivityResponseModel(newSchedule));
    }
}


