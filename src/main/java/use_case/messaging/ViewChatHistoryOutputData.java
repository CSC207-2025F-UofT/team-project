package use_case.messaging;

import java.util.List;

/**
 * Output data for the view chat history use case.
 */
public class ViewChatHistoryOutputData {

    private final String chatId;
    private final List<ChatMessageDto> messages;

    public ViewChatHistoryOutputData(String chatId, List<ChatMessageDto> messages) {
        this.chatId = chatId;
        this.messages = messages;
    }

    public String getChatId() {
        return chatId;
    }

    public List<ChatMessageDto> getMessages() {
        return messages;
    }
}

