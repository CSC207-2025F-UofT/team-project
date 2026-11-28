package data_access;

import com.sun.net.httpserver.HttpServer;
import entity.Watchlist;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

import java.net.InetAddress;

public class AccountWatchListDataAccessObjectTest {

    private HttpServer server;
    private AccoutWatchListDataAccessObject dao;

    private final Queue<String> replies = new ConcurrentLinkedQueue<>();
    private final List<String> methods = new CopyOnWriteArrayList<>();
    private final List<String> bodies = new CopyOnWriteArrayList<>();

    @BeforeEach
    void startServer() throws IOException{
        server = HttpServer.create(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 0), 0);
        server.createContext("/user", exchange -> {
            methods.add(exchange.getRequestMethod());
            bodies.add(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));

            byte[] reply = replies.remove().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "appliocation/json");
            exchange.sendResponseHeaders(200, reply.length);
            try (OutputStream out = exchange.getResponseBody()){
                out.write(reply);
            }
        });
        server.start();

        dao = new AccoutWatchListDataAccessObject("http://127.0.0.1:" + server.getAddress().getPort());

    }

    @AfterEach
    void stopServer(){
        if (server != null){
            server.stop(0);
        }
    }

    private String userReply(String infoJson){
        String info = infoJson == null ? "" : ",\"info\":" + infoJson;
        return "{\"status_code\":200,\"user\":{\"username\":\"sam\"," + "\"password\":\"pw\"" + info + "}}";
    }

    @Test
    void getReadsStocksFromInfo(){
        replies.add(userReply("{\"watchlist\":[\"IBM\",\"AAPL\"]}"));

        Watchlist watchlist = dao.get("sam");

        assertEquals(List.of("IBM", "AAPL"), watchlist.getStocks());
        assertEquals(List.of("GET"), methods);
    }

    @Test
    void getReturnsEmptyWhenNoInfoInAccount(){
        replies.add(userReply(null));

        assertTrue(dao.get("sam").getStocks().isEmpty());
    }

    @Test
    void getReturnEmptyWhenNoWatchListInInfo(){
        replies.add(userReply("{\"portfolio\":{\"cash\":5}}"));

        assertTrue(dao.get("sam").getStocks().isEmpty());
    }

    @Test
    void getThrowsWhenNoUserFound(){
        replies.add("{\"status_code\":404,\"message\":\"Could not connect to User API\"}");

        RuntimeException error = assertThrows(RuntimeException.class, () -> dao.get("nobody"));

        assertEquals("Could not connect to User API", error.getMessage());
    }

    @Test
    void saveWritesWatchListToInfo(){
        replies.add(userReply(null));
        replies.add("{\"status_code\":200}");
        Watchlist watchList = new Watchlist();
        watchList.addStock("IBM");

        dao.save("sam", watchList);

        assertEquals(List.of("GET", "PUT"), methods);
        JSONObject sent = new JSONObject(bodies.get(1));
        assertEquals("sam", sent.getString("username"));
        assertEquals("pw", sent.get("password"));
        assertEquals("IBM", sent.getJSONObject("info").getJSONArray("watchlist").getString(0));
    }

    @Test
    void saveKeepsOtherInfoKeys(){
        replies.add(userReply("{\"portfolio\":{\"cash\":5}}"));
        replies.add("{\"status_code\":200}");
        Watchlist watchlist = new Watchlist();
        watchlist.addStock("IBM");

        dao.save("sam", watchlist);

        JSONObject info = new JSONObject(bodies.get(1)).getJSONObject("info");
        assertEquals(5, info.getJSONObject("portfolio").getInt("cash"));
        assertEquals("IBM", info.getJSONArray("watchlist").getString(0));
    }

    @Test
    void saveThrowsWhenUpdateRejectedByServer(){
        replies.add(userReply(null));
        replies.add("{\"status_code\":400,\"message\":\"Could not connect to User API\"}");

        RuntimeException error = assertThrows(RuntimeException.class, () -> dao.save("sam", new Watchlist()));
        assertEquals("Could not connect to User API", error.getMessage());
    }

    @Test
    void getThrowsWhenServerCantBeReached(){
        server.stop(0);

        RuntimeException error = assertThrows(RuntimeException.class, () -> dao.get("sam"));

        assertEquals("Could not connect to User API", error.getMessage());
    }
}
