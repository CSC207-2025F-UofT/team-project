package data_access.question;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import use_case.question.QuestionLoadException;
import static org.junit.jupiter.api.Assertions.*;

class JdkQuestionHttpClientTest {
    @Test void usesGetAndRejectsNonSuccessStatusBeforeParsing() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        AtomicInteger status = new AtomicInteger(200);
        server.createContext("/rows", exchange -> {
            assertEquals("GET", exchange.getRequestMethod());
            assertEquals("application/json", exchange.getRequestHeaders().getFirst("Accept"));
            byte[] body = "remote response".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status.get(), body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        try {
            JdkQuestionHttpClient client = new JdkQuestionHttpClient();
            URI uri = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/rows");
            assertEquals("remote response", client.get(uri));
            status.set(503);
            assertTrue(assertThrows(QuestionLoadException.class, () -> client.get(uri))
                    .getMessage().contains("503"));
            status.set(429);
            assertTrue(assertThrows(QuestionLoadException.class, () -> client.get(uri))
                    .getMessage().contains("429"));
        }
        finally { server.stop(0); }
    }
}
