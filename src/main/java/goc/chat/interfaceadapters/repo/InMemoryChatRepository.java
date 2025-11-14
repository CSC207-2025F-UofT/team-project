package goc.chat.interfaceadapters.repo;

import goc.chat.entity.Chat;
import goc.chat.usecase.ports.ChatRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryChatRepository implements ChatRepository {

    private final Map<String, Chat> chats = new HashMap<>();

    @Override
    public Optional<Chat> findById(String id) {
        return Optional.ofNullable(chats.get(id));
    }

    @Override
    public Chat save(Chat chat) {
        chats.put(chat.getId(), chat);
        return chat;
    }
}

