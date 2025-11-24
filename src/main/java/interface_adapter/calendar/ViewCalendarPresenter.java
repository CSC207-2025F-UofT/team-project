package interface_adapter.calendar;

import use_case.calendar.ViewCalendarOutputBoundary;
import use_case.calendar.ViewCalendarOutputData;

public class ViewCalendarPresenter implements ViewCalendarOutputBoundary {

    private final CalendarViewModel viewModel;

    public ViewCalendarPresenter(CalendarViewModel vm) {
        this.viewModel = vm;
    }

    @Override
    public void prepareSuccessView(ViewCalendarOutputData data) {
        viewModel.fireViewCalendar();
    }
}
