package networking.client;

import networking.MessageRouter;
import networking.protocol.Message;
import networking.protocol.MessageCodec;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.swing.*;
import java.net.URI;

public class GameClient extends WebSocketClient {
    private final MessageCodec codec = new MessageCodec();
    private final MessageRouter<Void> router;

    public GameClient(URI serverUri, MessageRouter<Void> router) {
        super(serverUri);
        this.router = router;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to server.");
        send("Hello, server (world)!");
    }

    @Override
    public void onMessage(String raw) {
        Message message = codec.decode(raw);

        SwingUtilities.invokeLater(() -> {
            router.dispatch(null, message);
        });
    }

    @Override
    public void onClose(
            int code, String reason, boolean remote) {
        System.out.println("Disconnected (Goodbye World!)");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
