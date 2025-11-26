package plan4life.data_access;

import plan4life.entities.Schedule;

public interface ScheduleDataAccessInterface {
    Schedule getSchedule(int scheduleId);
    void saveSchedule(Schedule schedule);
}