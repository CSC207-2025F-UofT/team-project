package use_case.messaging;

import entity.Message;
import use_case.ports.MessageRepository;

import java.time.Instant;
import java.util.UUID;

public class SendMessageInteractor {

    private final MessageRepository messageRepository;

    public SendMessageInteractor(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void execute(String chatId, String senderUserId, String content) {
        String id = UUID.randomUUID().toString();

        Message message = new Message(
                id,
                chatId,
                senderUserId,
                content,
                Instant.now()
        );

        messageRepository.save(message);
    }
}
