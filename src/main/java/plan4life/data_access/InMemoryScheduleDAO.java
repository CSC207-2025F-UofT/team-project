package plan4life.data_access;

import plan4life.entities.Schedule;
import java.util.HashMap;
import java.util.Map;

public class InMemoryScheduleDAO implements ScheduleDataAccessInterface {
    private final Map<Integer, Schedule> schedules = new HashMap<>();

    @Override
    public Schedule getSchedule(int scheduleId) {
        return schedules.get(scheduleId);
    }

    @Override
    public void saveSchedule(Schedule schedule) {
        schedules.put(schedule.getScheduleId(), schedule);
    }
}