package interface_adapter.job_grouping;

import use_case.job_grouping.CreateJobGroupOutputBoundary;
import use_case.job_grouping.CreateJobGroupOutputData;

/**
 * The presenter for the Create Job Group use case.
 */
public class CreateJobGroupPresenter implements CreateJobGroupOutputBoundary {

    private final CreateJobGroupViewModel viewModel;

    /**
     * Creates the Create Job Group Presenter.
     * @param viewModel the view model
     */
    public CreateJobGroupPresenter(CreateJobGroupViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CreateJobGroupOutputData outputData) {
        final CreateJobGroupState state = viewModel.getState();

        state.setGroupName(outputData.getGroupName());
        state.setErrorMessage(null);
        state.setSuccess(true);

        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final CreateJobGroupState state = viewModel.getState();

        state.setErrorMessage(errorMessage);
        state.setSuccess(false);

        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
