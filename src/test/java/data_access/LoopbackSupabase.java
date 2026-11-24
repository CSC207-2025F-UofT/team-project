package data_access;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class LoopbackSupabase implements AutoCloseable {
    static final String API_KEY = "sb_publishable_loopback-fixture";

    private final HttpServer server;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    private final BlockingQueue<Reply> replies = new LinkedBlockingQueue<>();
    private final BlockingQueue<Call> calls = new LinkedBlockingQueue<>();

    LoopbackSupabase() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.setExecutor(executor);
        server.createContext("/", exchange -> {
            try (exchange) {
                calls.add(new Call(exchange.getRequestMethod(), exchange.getRequestURI().toString(),
                        exchange.getRequestHeaders().getFirst("apikey"),
                        exchange.getRequestHeaders().getFirst("Authorization"),
                        exchange.getRequestHeaders().getFirst("Content-Type"),
                        new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8)));
                final Reply reply = replies.poll();
                if (reply == null) {
                    exchange.sendResponseHeaders(500, -1);
                    return;
                }
                reply.onRequest().run();
                if (reply.location() != null) {
                    exchange.getResponseHeaders().set("Location", reply.location());
                }
                final byte[] body = reply.body().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(reply.status(), body.length == 0 ? -1 : body.length);
                if (reply.stallBody()) {
                    Thread.sleep(Duration.ofSeconds(10));
                }
                if (body.length != 0) {
                    exchange.getResponseBody().write(body);
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            } catch (IOException ignored) {
                // The deadline tests intentionally cancel requests while the mock writes the body.
            }
        });
        server.start();
    }

    URI origin() {
        return URI.create("http://127.0.0.1:" + server.getAddress().getPort());
    }

    SupabaseClient client(Duration timeout) {
        return new SupabaseClient(origin(), API_KEY, Duration.ofSeconds(2), timeout);
    }

    void enqueue(int status, String body) {
        enqueue(status, body, () -> { });
    }

    void enqueue(int status, String body, Runnable onRequest) {
        replies.add(new Reply(status, body, null, false, onRequest));
    }

    void stallBody(int status, String body) {
        replies.add(new Reply(status, body, null, true, () -> { }));
    }

    void redirectTo(URI target) {
        replies.add(new Reply(307, "", target.toString(), false, () -> { }));
    }

    Call takeCall() throws InterruptedException {
        final Call call = calls.poll(2, TimeUnit.SECONDS);
        assertNotNull(call, "Expected a request to the loopback server");
        return call;
    }

    Call unexpectedCall() throws InterruptedException {
        return calls.poll(150, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }

    record Call(String method, String path, String apiKey, String authorization, String contentType, String body) { }

    private record Reply(int status, String body, String location, boolean stallBody, Runnable onRequest) { }
}
