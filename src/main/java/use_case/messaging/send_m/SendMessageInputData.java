package use_case.messaging.send_m;

public class SendMessageInputData {

    private final String chatId;
    private final String senderUserId;
    private final String content;

    public SendMessageInputData(String chatId, String senderUserId, String content) {
        this.chatId = chatId;
        this.senderUserId = senderUserId;
        this.content = content;
    }

    public String getChatId() {
        return chatId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public String getContent() {
        return content;
    }
}
