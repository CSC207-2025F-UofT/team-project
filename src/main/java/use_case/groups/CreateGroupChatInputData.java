package use_case.groups;

import java.util.List;

public class CreateGroupChatInputData {
    private final String creatorUserId;
    private final List<String> participantUsernames;
    private final String groupName;

    public CreateGroupChatInputData(String creatorUserId, List<String> participantUsernames, String groupName) {
        this.creatorUserId = creatorUserId;
        this.participantUsernames = participantUsernames;
        this.groupName = groupName;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public List<String> getParticipantUsernames() {
        return participantUsernames;
    }

    public String getGroupName() {
        return groupName;
    }
}