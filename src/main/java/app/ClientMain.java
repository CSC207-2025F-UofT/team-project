package app;

import networking.MessageRouter;
import networking.client.GameClient;
import networking.protocol.MessageType;

import java.net.URI;

public class ClientMain {
    public static void main(String[] args) {
        //Make Routers (sample below idk)
        MessageRouter<Void> router = new MessageRouter<>();
//        router.register(
//                MessageType.CAST_SPELL,
//                CastOutputData.class,
//                castPresenter::presentCastIntent
//        );

        GameClient client = new GameClient(URI.create("ws://localhost:8080"), router);
        client.connect();

        //TODO: Build Client Game View, ViewModel, Controller, and Presenter
    }

}
