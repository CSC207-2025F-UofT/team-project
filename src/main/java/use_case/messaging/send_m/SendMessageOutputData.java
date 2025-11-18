package use_case.messaging.send_m;

import use_case.messaging.ChatMessageDto;

public class SendMessageOutputData {

    private final String chatId;
    private final ChatMessageDto message;

    public SendMessageOutputData(String chatId, ChatMessageDto message) {
        this.chatId = chatId;
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public ChatMessageDto getMessage() {
        return message;
    }
}

