package networking.protocol;
import com.google.gson.JsonElement;

public record Message(
        MessageType type,
        JsonElement payload
) {}