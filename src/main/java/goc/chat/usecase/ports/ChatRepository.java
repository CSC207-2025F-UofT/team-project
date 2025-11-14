package goc.chat.usecase.ports;

import goc.chat.entity.Chat;
import java.util.Optional;

public interface ChatRepository {
    Optional<Chat> findById(String id);
    Chat save(Chat chat);
}
