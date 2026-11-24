package networking.protocol;
import com.google.gson.Gson;

public class MessageCodec {
    private final Gson gson = new Gson();

    public String encode(MessageType type, Object data) {
        Message message = new Message(
                type,
                gson.toJsonTree(data)
        );

        return gson.toJson(message);
    }

    public Message decode(String raw) {
        return gson.fromJson(raw, Message.class);
    }

    public <T> T decodePayload(Message message, Class<T> type) {
        return gson.fromJson(message.payload(), type);
    }
}