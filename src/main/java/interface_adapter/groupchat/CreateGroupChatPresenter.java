package interface_adapter.groupchat;

import interface_adapter.ViewManagerModel;
import use_case.groups.CreateGroupChatOutputBoundary;
import use_case.groups.CreateGroupChatOutputData;

public class CreateGroupChatPresenter implements CreateGroupChatOutputBoundary {

    private final GroupChatViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public CreateGroupChatPresenter(GroupChatViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CreateGroupChatOutputData outputData) {
        GroupChatState state = new GroupChatState();
        state.setChatId(outputData.getChatId());
        state.setGroupName(outputData.getGroupName());
        state.setSuccess(true);
        state.setError(null);

        viewModel.setState(state);
        viewModel.firePropertyChange();

        viewManagerModel.setState("chat");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(CreateGroupChatOutputData outputData) {
        GroupChatState state = new GroupChatState();
        state.setSuccess(false);
        state.setError(outputData.getErrorMessage());

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}