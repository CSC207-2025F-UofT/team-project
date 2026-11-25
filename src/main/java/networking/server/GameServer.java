package networking.server;
import java.net.InetSocketAddress;

import networking.MessageRouter;
import networking.protocol.Message;
import networking.protocol.MessageCodec;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class GameServer extends WebSocketServer {
    private final MessageCodec codec = new MessageCodec();
    private final MessageRouter<WebSocket> lobbyRouter;

    public GameServer(int port,  MessageRouter<WebSocket> lobbyRouter) {
        super(new InetSocketAddress(port));
        this.lobbyRouter = lobbyRouter;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Player connected");
    }

    @Override
    public synchronized void onMessage(WebSocket conn, String raw) {
        Message message = codec.decode(raw);

        if (lobbyRouter.hasListener(message.type())) {
            lobbyRouter.dispatch(conn, message);
        } else {
            //TODO: Implement GameRoom Manager
//            GameRoom room = roomManager.getRoom(conn);
//            room.getRouter().dispatch(conn, message);
        }
    }

    @Override
    public void onClose(
            WebSocket conn, int code,
            String reason, boolean remote) {
        System.out.println("Player disconnected");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started");
    }
}