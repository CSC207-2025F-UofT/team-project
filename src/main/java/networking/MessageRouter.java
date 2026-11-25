package networking;

import networking.protocol.Message;
import networking.protocol.MessageCodec;
import networking.protocol.MessageType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessageRouter<C> {

    private final MessageCodec codec = new MessageCodec();

    private final Map<MessageType, BiConsumer<C, Message>> listeners = new EnumMap<>(MessageType.class);

    /// Registers a new listener
    /// @param type The MessageType of the message being sent (The kind of action being taken)
    /// @param dataClass The Class Type of the Input/Output data
    /// @param listener The execute/present method that needs to be called
    /// @param <T> Data
    public <T> void register(MessageType type, Class<T> dataClass, BiConsumer<C, T> listener) {
        listeners.put(type, (context, message) -> {
            T data = codec.decodePayload(message, dataClass);
            listener.accept(context, data);
        });
    }

    /// Receive a Message
    /// @param context The connection if this is the server, otherwise null
    /// @param message The message being received
    public void dispatch(C context, Message message) {
        listeners.get(message.type()).accept(context, message);
    }

    /// Returns whether this Router has a listener of the given <essageType
    /// @param type The MessageType
    /// @return True iff this Router has a listener of the given MessageType
    public boolean hasListener(MessageType type) {
        return listeners.containsKey(type);
    }
}
