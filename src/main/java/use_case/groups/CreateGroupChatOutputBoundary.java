package use_case.groups;

public interface CreateGroupChatOutputBoundary {
    void prepareSuccessView(CreateGroupChatOutputData outputData);
    void prepareFailView(CreateGroupChatOutputData outputData);
}