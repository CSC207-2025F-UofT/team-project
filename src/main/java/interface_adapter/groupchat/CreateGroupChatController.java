package interface_adapter.groupchat;

import use_case.groups.CreateGroupChatInputBoundary;
import use_case.groups.CreateGroupChatInputData;

import java.util.List;

public class CreateGroupChatController {

    private final CreateGroupChatInputBoundary interactor;

    public CreateGroupChatController(CreateGroupChatInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String creatorUserId, List<String> participantUsernames, String groupName) {
        CreateGroupChatInputData inputData = new CreateGroupChatInputData(
                creatorUserId,
                participantUsernames,
                groupName
        );
        interactor.execute(inputData);
    }
}