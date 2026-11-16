package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import entity.JobPosting;
import entity.WorkArrangement;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import use_case.port.JobFetchResult;
import use_case.port.JobSourceException;
import use_case.port.SourceFailure;

class GreenhouseJobSourceTest {
  private HttpServer server;
  private ExecutorService serverExecutor;
  private URI base;
  private final AtomicReference<String> query = new AtomicReference<>();
  private final CountDownLatch releaseSlowResponse = new CountDownLatch(1);

  @BeforeEach
  void startServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    serverExecutor = Executors.newCachedThreadPool();
    server.setExecutor(serverExecutor);
    server.start();
    base = URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/boards/");
  }

  @AfterEach
  void stopServer() {
    releaseSlowResponse.countDown();
    server.stop(0);
    serverExecutor.shutdownNow();
  }

  @Test
  void mapsEncodedHtmlStableIdentityAndMissingFields() throws Exception {
    respond(
        "good",
        200,
        "{\"jobs\":[{\"id\":7,\"title\":\"Software Intern\","
            + "\"content\":\"&amp;lt;p&amp;gt;Java &amp;amp; SQL&amp;lt;/p&amp;gt;\","
            + "\"absolute_url\":\"https://example.com/jobs/7\"}]}");
    JobFetchResult result = source(Duration.ofSeconds(2), "good").fetch();
    JobPosting job = result.getJobs().get(0);
    assertEquals("content=true", query.get());
    assertEquals("greenhouse:good:7", job.getStableId());
    assertEquals("Java & SQL", job.getDescriptionText());
    assertEquals("", job.getLocation());
    assertEquals(WorkArrangement.UNKNOWN, job.getWorkArrangement());
    assertEquals(1, result.getSuccessCount());
  }

  @Test
  void partialHttpFailureKeepsSuccessfulEmptyBoard() throws Exception {
    respond("empty", 200, "{\"jobs\":[]}");
    respond("down", 503, "unavailable");
    JobFetchResult result = source(Duration.ofSeconds(2), "down", "empty").fetch();
    assertTrue(result.getJobs().isEmpty());
    assertEquals(List.of("empty"), result.getSuccessfulSources());
    assertEquals(List.of("down"), result.getFailedBoards());
    assertEquals(SourceFailure.Reason.HTTP_ERROR, result.getFailures().get(0).getReason());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "not json",
        "{}",
        "{\"jobs\":{}}",
        "{\"jobs\":[{\"title\":\"Intern\"}]}",
        "{\"jobs\":[{\"id\":7,\"title\":\"Intern\",\"absolute_url\":\"javascript:alert(1)\"}]}",
        "{\"jobs\":[{\"id\":7,\"title\":\"Intern\",\"absolute_url\":\"https://example.com\","
            + "\"location\":\"invalid\"}]}"
      })
  void malformedBoardIsReportedAsUnavailableRatherThanEmpty(String body) throws Exception {
    respond("bad", 200, body);
    respond("good", 200, "{\"jobs\":[]}");
    JobFetchResult result = source(Duration.ofSeconds(2), "bad", "good").fetch();
    assertEquals(List.of("bad"), result.getFailedBoards());
    assertEquals(SourceFailure.Reason.INVALID_RESPONSE, result.getFailures().get(0).getReason());
  }

  @Test
  void partialMalformedBoardNeverPublishesAnIncompleteUnlabelledList() throws Exception {
    respond(
        "bad",
        200,
        "{\"jobs\":[{\"id\":1,\"title\":\"Intern\","
            + "\"absolute_url\":\"https://example.com/1\"},{\"id\":2}]}");
    respond("good", 200, "{\"jobs\":[]}");
    JobFetchResult result = source(Duration.ofSeconds(2), "bad", "good").fetch();
    assertTrue(result.getJobs().isEmpty());
    assertEquals(List.of("bad"), result.getFailedBoards());
  }

  @Test
  void allBoardsFailAndNoBoardsConfiguredBothThrow() {
    respond("down", 429, "rate limited");
    assertThrows(JobSourceException.class, () -> source(Duration.ofSeconds(2), "down").fetch());
    assertThrows(JobSourceException.class, () -> source(Duration.ofSeconds(2)).fetch());
  }

  @Test
  void timeoutDoesNotPreventAnotherBoardFromSucceeding() throws Exception {
    server.createContext(
        "/boards/slow/jobs",
        exchange -> {
          try {
            releaseSlowResponse.await(3, TimeUnit.SECONDS);
          } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
          } finally {
            exchange.close();
          }
        });
    respond("good", 200, "{\"jobs\":[]}");
    JobFetchResult result = source(Duration.ofMillis(400), "slow", "good").fetch();
    assertEquals(List.of("good"), result.getSuccessfulSources());
    assertEquals(SourceFailure.Reason.TIMEOUT, result.getFailures().get(0).getReason());
  }

  @Test
  void interruptionPreservesInterruptFlagAndDoesNotPublishResults() {
    respond("good", 200, "{\"jobs\":[]}");
    GreenhouseJobSource source = source(Duration.ofSeconds(2), "good");
    Thread.currentThread().interrupt();
    try {
      assertThrows(JobSourceException.class, source::fetch);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted();
    }
  }

  private GreenhouseJobSource source(Duration timeout, String... tokens) {
    List<JobBoard> boards =
        java.util.Arrays.stream(tokens)
            .map(token -> new JobBoard(token, token + " company"))
            .collect(java.util.stream.Collectors.toList());
    return new GreenhouseJobSource(
        HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build(),
        base,
        boards,
        timeout);
  }

  private void respond(String token, int status, String body) {
    server.createContext(
        "/boards/" + token + "/jobs",
        exchange -> {
          query.set(exchange.getRequestURI().getRawQuery());
          byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
          exchange.sendResponseHeaders(status, bytes.length);
          try (java.io.OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
          } finally {
            exchange.close();
          }
        });
  }
}
