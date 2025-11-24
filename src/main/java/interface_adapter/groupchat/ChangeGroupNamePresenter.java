package interface_adapter.groupchat;

import use_case.groups.ChangeGroupNameOutputBoundary;
import use_case.groups.ChangeGroupNameOutputData;

/**
 * Presenter for the Change Group Name use case.
 * Converts output data to view model state.
 */
public class ChangeGroupNamePresenter implements ChangeGroupNameOutputBoundary {

    private final GroupChatViewModel viewModel;

    public ChangeGroupNamePresenter(GroupChatViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ChangeGroupNameOutputData outputData) {
        // Update the view model with success
        GroupChatState state = new GroupChatState();
        state.setChatId(outputData.getChatId());
        state.setGroupName(outputData.getNewGroupName());
        state.setSuccess(true);
        state.setError(null);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(ChangeGroupNameOutputData outputData) {
        // Update the view model with error
        GroupChatState state = new GroupChatState();
        state.setChatId(outputData.getChatId());
        state.setSuccess(false);
        state.setError(outputData.getErrorMessage());

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}