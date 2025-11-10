package plan4life.presenter;

import plan4life.use_case.generate_schedule.GenerateScheduleOutputBoundary;
import plan4life.use_case.generate_schedule.GenerateScheduleResponseModel;
import plan4life.view.CalendarPanel;

public class CalendarPresenter implements GenerateScheduleOutputBoundary {

    private final CalendarPanel calendarPanel;

    public CalendarPresenter(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;
    }

    @Override
    public void present(GenerateScheduleResponseModel responseModel) {
        // Pass the schedule to the view
        calendarPanel.updateSchedule(responseModel.getSchedule());
    }
}


