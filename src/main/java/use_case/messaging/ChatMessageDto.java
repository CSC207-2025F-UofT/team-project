package use_case.messaging;

import java.time.Instant;

/**
 * A simple DTO representing one message in a chat history,
 * ready for the presenter / view layer.
 */
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

    public String getMessageId() {
        return messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
