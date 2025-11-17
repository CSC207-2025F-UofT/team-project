package use_case.ports;

import entity.Chat;
import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> findById(String id);
    Chat save(Chat chat);
}
