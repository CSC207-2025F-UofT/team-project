package goc.chat.usecase.messaging;

import java.time.Instant;

public class ChatMessageDto {

    private final String messageId;
    private final String senderUserId;
    private final String senderName;
    private final String content;
    private final Instant timestamp;

    public ChatMessageDto(String messageId,
                          String senderUserId,
                          String senderName,
                          String content,
                          Instant timestamp) {

        this.messageId = messageId;
        this.senderUserId = senderUserId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getMessageId() { return messageId; }

    public String getSenderUserId() { return senderUserId; }

    public String getSenderName() { return senderName; }

    public String getContent() { return content; }

    public Instant getTimestamp() { return timestamp; }
}
