package interface_adapter.calendar;

import use_case.calendar.AddEventOutputBoundary;
import use_case.calendar.AddEventOutputData;

public class AddEventPresenter implements AddEventOutputBoundary {
    private final CalendarViewModel viewModel;

    public AddEventPresenter(CalendarViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddEventOutputData output) {
        viewModel.fireEventsChanged();
    }
}
