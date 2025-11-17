package interface_adapter.save_event_to_list;

import use_case.save_event_to_list.SaveEventToListOutputBoundary;
import use_case.save_event_to_list.SaveEventToListOutputData;

public class SaveEventToListPresenter implements SaveEventToListOutputBoundary {
    private final SaveEventToListViewModel viewModel;

    public SaveEventToListPresenter(SaveEventToListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SaveEventToListOutputData outputData) {
        viewModel.setEvent(outputData.getEvent());
        viewModel.setEventList(outputData.getEventLists());
        viewModel.setMessage(outputData.getMessage());
    }
}
