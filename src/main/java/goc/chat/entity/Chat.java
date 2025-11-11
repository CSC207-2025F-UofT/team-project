package goc.chat.entity;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final String id;
    private final List<String> participantUserIds = new ArrayList<>();
    private final List<String> messageIds = new ArrayList<>();

    public Chat(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public List<String> getParticipantUserIds() { return participantUserIds; }
    public List<String> getMessageIds() { return messageIds; }

    public void addParticipant(String userId) { participantUserIds.add(userId); }
    public void addMessage(String messageId) { messageIds.add(messageId); }
}

