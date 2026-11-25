package app;

import networking.MessageRouter;
import networking.server.GameServer;
import org.java_websocket.WebSocket;


public class ServerMain {
    public static void main(String[] args) {
        //Make Routers
        MessageRouter<WebSocket> lobbyRouter = new MessageRouter<>();

        GameServer server = new GameServer(8080, lobbyRouter);
        server.start();

        //TODO: Build Server Lobby Managers, Use Case Interactors, Entities, etc.
    }
}
