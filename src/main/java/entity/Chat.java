package entity;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final String id;
    private final List<String> participantUserIds = new ArrayList<>();
    private final List<String> messageIds = new ArrayList<>();
    private String groupName;

    public Chat(String id) {
        this.id = id;
        this.groupName = null;
    }

    public String getId() { return id; }
    public List<String> getParticipantUserIds() { return participantUserIds; }
    public List<String> getMessageIds() { return messageIds; }
    public String getGroupName() { return this.groupName;}

    public void addParticipant(String userId) { participantUserIds.add(userId); }
    public void addMessage(String messageId) { messageIds.add(messageId); }
    public void setGroupName(String groupName) {this.groupName = groupName; }
}

