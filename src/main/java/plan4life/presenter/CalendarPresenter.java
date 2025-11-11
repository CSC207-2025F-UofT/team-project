package plan4life.presenter;

import plan4life.use_case.generate_schedule.*;
import plan4life.use_case.lock_activity.*;
import plan4life.entities.Schedule;
import plan4life.view.CalendarViewInterface;

public class CalendarPresenter implements
        GenerateScheduleOutputBoundary, LockActivityOutputBoundary {

    private final CalendarViewInterface view;

    public CalendarPresenter(CalendarViewInterface view) {
        this.view = view;
    }

    @Override
    public void present(GenerateScheduleResponseModel response) {
        Schedule schedule = response.getSchedule();
        view.displaySchedule(schedule);
    }

    @Override
    public void present(LockActivityResponseModel response) {
        Schedule updatedSchedule = response.getUpdatedSchedule();
        view.displaySchedule(updatedSchedule);
    }
}




