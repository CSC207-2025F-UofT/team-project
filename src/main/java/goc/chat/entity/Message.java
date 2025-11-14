package goc.chat.entity;

import java.time.Instant;

public class Message {
    private final String id;
    private final String chatId;
    private final String senderUserId;
    private String content;
    private final Instant timestamp;

    public Message(String id, String chatId, String senderUserId, String content, Instant timestamp) {
        this.id = id;
        this.chatId = chatId;
        this.senderUserId = senderUserId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public String getChatId() { return chatId; }
    public String getSenderUserId() { return senderUserId; }
    public String getContent() { return content; }
    public Instant getTimestamp() { return timestamp; }

    public void setContent(String content) { this.content = content; }
}

