package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import entity.JobPosting;
import interface_adapter.search_jobs.SearchJobsPresenter;
import interface_adapter.search_jobs.SearchJobsState;
import interface_adapter.search_jobs.SearchJobsViewModel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import use_case.port.JobSourceException;
import use_case.search_jobs.SearchJobsInputData;
import use_case.search_jobs.SearchJobsInteractor;

class JoobleJobSearchTest {
  private HttpServer server;
  private JoobleJobSearch source;
  private final AtomicReference<JSONObject> received = new AtomicReference<>();
  private final AtomicInteger calls = new AtomicInteger();

  @BeforeEach
  void start() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.start();
    URI base = URI.create("http://127.0.0.1:" + server.getAddress().getPort());
    source =
        new JoobleJobSearch(
            HttpClient.newHttpClient(),
            base.resolve(JoobleJobSearch.canadianEndpoint("fixture-key").getRawPath()));
  }

  @AfterEach
  void stop() {
    server.stop(0);
  }

  @Test
  void productionEndpointUsesTheCanadianHostAndEncodesOneKeySegment() {
    assertEquals(
        URI.create("https://ca.jooble.org/api/test%2Fkey"),
        JoobleJobSearch.canadianEndpoint(" test/key "));
  }

  @Test
  void sendsOneJsonRequestAndMapsTheOriginalListing() throws Exception {
    String link = "https://ca.jooble.org/jdp/-848334525";
    JSONObject listing =
        new JSONObject()
            .put("id", -848334525L)
            .put("title", "C++ Co-op")
            .put("company", "New Startup")
            .put("location", "Montréal, QC")
            .put("snippet", "<b>Research</b> &amp; development")
            .put("link", link);
    respond(
        200,
        new JSONObject()
            .put("totalCount", 110)
            .put("jobs", new JSONArray().put(listing))
            .toString());
    final JobPosting job = source.search("C++ \"intern\"", "Montréal").getJobs().get(0);
    assertEquals(1, calls.get());
    assertEquals("C++ \"intern\"", received.get().getString("keywords"));
    assertEquals("Montréal", received.get().getString("location"));
    assertEquals(1, received.get().getInt("page"));
    assertEquals("jooble:ca:-848334525", job.getStableId());
    assertEquals("C++ Co-op", job.getTitle());
    assertEquals("New Startup", job.getCompany());
    assertEquals("Montréal, QC", job.getLocation());
    assertEquals("Research & development", job.getDescriptionText());
    assertEquals(link, job.getApplicationUrl());
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "software",
        "software intern",
        "software INTERNSHIP",
        "software co-op",
        "internal tools"
      })
  void sendsUserKeywordsForTitleAndDescriptionSearch(String keywords) throws Exception {
    respond(200, "{\"jobs\":[]}");
    source.search(keywords, "");
    assertEquals(keywords, received.get().getString("keywords"));
    assertFalse(received.get().getBoolean("companysearch"));
    assertEquals("Canada", received.get().getString("location"));
    assertEquals(1, calls.get());
  }

  @ParameterizedTest
  @CsvSource({"Toronto,Toronto", "'',Canada"})
  void companyFilterKeepsTheJobKeywordsInTheRequest(String location, String expectedLocation)
      throws Exception {
    respond(200, "{\"jobs\":[]}");
    SearchJobsInputData criteria = SearchJobsInputData.withFilters("software", "Amazon", location);
    source.fetchPage(criteria, 2);
    assertFalse(received.get().getBoolean("companysearch"));
    assertEquals("software", received.get().getString("keywords"));
    assertEquals(expectedLocation, received.get().getString("location"));
    assertEquals(2, received.get().getInt("page"));
  }

  @Test
  void blankLocationSearchesCanadaAndZeroMatchesIsSuccess() throws Exception {
    respond(200, "{\"jobs\":[],\"totalCount\":0}");
    assertTrue(source.search("intern", "").getJobs().isEmpty());
    assertEquals("Canada", received.get().getString("location"));
  }

  @ParameterizedTest
  @CsvSource({"403,key", "429,limit", "500,500"})
  void serviceFailureProducesActionableErrorAndKeepsKeyPrivate(int code, String reason) {
    respond(code, "provider error with fixture-key");
    JobSourceException error =
        assertThrows(JobSourceException.class, () -> source.search("software intern", "Toronto"));
    assertTrue(error.getMessage().contains(reason));
    assertFalse(error.getMessage().contains("fixture-key"));
    assertEquals(1, calls.get());
  }

  @ParameterizedTest
  @ValueSource(ints = {200, 403})
  void browserChallengeReportsTheSupportReferenceAndDoesNotRetry(int status) {
    String ray = "0123456789abcdef-YYZ";
    respond(
        status,
        "<html>fixture-key</html>",
        Map.of("Content-Type", "text/html", "cf-mitigated", "challenge", "cf-ray", ray));
    JobSourceException error =
        assertThrows(JobSourceException.class, () -> source.search("software intern", "Toronto"));
    assertTrue(error.getMessage().contains("Cloudflare browser check"));
    assertTrue(error.getMessage().contains(ray));
    assertFalse(error.getMessage().contains("fixture-key"));
    assertEquals(1, calls.get());
  }

  @Test
  void htmlDenialWithoutChallengeHeaderReportsTheObservedResponse() {
    String ray = "0123456789abcdef-YYZ";
    respond(
        403,
        "<html>fixture-key</html>",
        Map.of("Content-Type", "text/html; charset=utf-8", "cf-ray", ray));
    JobSourceException error =
        assertThrows(JobSourceException.class, () -> source.search("software intern", "Toronto"));
    assertTrue(error.getMessage().contains("HTML access-denied page (HTTP 403)"));
    assertTrue(error.getMessage().contains(ray));
    assertFalse(error.getMessage().contains("fixture-key"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "{}",
        "not json",
        "{\"jobs\":[{}]}",
        "{\"jobs\":[{\"id\":1,\"title\":\"Intern\",\"link\":\"file:///tmp/local\"}]}"
      })
  void invalidResponsesAreNotPublishedAsValidResults(String json) {
    respond(200, json);
    assertThrows(JobSourceException.class, () -> source.search("intern", "Toronto"));
  }

  @Test
  void collectsTenDistinctInternshipsInOrderAcrossPages() {
    respond(
        page -> 200,
        page -> {
          if (page == 1) {
            return pageOf(1, 20, "Internal Software Engineer", 100);
          } else if (page == 2) {
            return pageOf(21, 7, "Software Intern", 100);
          }
          return pageOf(26, 20, "Software Co-op", 100);
        },
        Map.of());
    SearchJobsState state = searchFinder();
    assertEquals(3, calls.get());
    assertEquals(10, state.getRows().size());
    for (int index = 0; index < 10; index++) {
      assertEquals("jooble:ca:" + (21 + index), state.getRows().get(index).getId());
    }
    assertEquals("software", received.get().getString("keywords"));
    assertEquals("Canada", received.get().getString("location"));
    assertTrue(state.getWarning().isEmpty());
  }

  @Test
  void stopsAtTenEvenWhenTheFirstPageHasMoreMatches() {
    respond(200, pageOf(1, 25, "Software Internship", 45));
    assertEquals(10, searchFinder().getRows().size());
    assertEquals(1, calls.get());
  }

  @Test
  void stopsWhenTheReportedRawResultsAreExhausted() {
    respond(page -> 200, page -> pageOf(page, 1, "Software Intern", 2), Map.of());
    SearchJobsState state = searchFinder();
    assertEquals(2, state.getRows().size());
    assertEquals(2, calls.get());
    assertTrue(state.getWarning().isEmpty());
  }

  @Test
  void emptyPageEndsSearchEvenWhenTheProviderStillReportsMoreResults() {
    respond(page -> 200, page -> pageOf(1, page == 1 ? 3 : 0, "Software Intern", 100), Map.of());
    SearchJobsState state = searchFinder();
    assertEquals(3, state.getRows().size());
    assertEquals(2, calls.get());
  }

  @Test
  void repeatedPageWithoutTotalDoesNotUseTheRemainingQuota() {
    respond(200, pageOf(1, 3, "Software Intern", null));
    SearchJobsState state = searchFinder();
    assertEquals(3, state.getRows().size());
    assertEquals(2, calls.get());
    assertTrue(state.getWarning().contains("repeated"));
  }

  @Test
  void laterRateLimitKeepsEarlierMatchesAndShowsTheProviderWarning() {
    respond(page -> page == 1 ? 200 : 429, page -> pageOf(1, 3, "Software Intern", 100), Map.of());
    SearchJobsState state = searchFinder();
    assertEquals(3, state.getRows().size());
    assertEquals(2, calls.get());
    assertTrue(state.getWarning().contains("request limit"));
    assertFalse(state.isError());
  }

  @Test
  void continuationRetriesTheFailedPageWithoutClearingEarlierMatches() {
    AtomicInteger attempts = new AtomicInteger();
    respond(
        page -> page == 2 && attempts.getAndIncrement() == 0 ? 429 : 200,
        page -> pageOf(page == 1 ? 1 : 11, 10, "Software Intern", 20),
        Map.of());
    SearchJobsViewModel model = new SearchJobsViewModel();
    SearchJobsInteractor finder =
        new SearchJobsInteractor(source, new SearchJobsPresenter(model, Runnable::run));
    finder.execute(new SearchJobsInputData("software"));
    finder.execute(SearchJobsInputData.more());
    assertEquals(10, model.getState().getRows().size());
    assertTrue(model.getState().getWarning().contains("limit"));
    assertTrue(model.getState().hasMore());
    finder.execute(SearchJobsInputData.more());
    assertEquals(20, model.getState().getRows().size());
    assertTrue(model.getState().getWarning().isEmpty());
    assertFalse(model.getState().hasMore());
    assertEquals(3, calls.get());
  }

  private SearchJobsState searchFinder() {
    SearchJobsViewModel model = new SearchJobsViewModel();
    new SearchJobsInteractor(source, new SearchJobsPresenter(model, Runnable::run))
        .execute(new SearchJobsInputData("software", ""));
    return model.getState();
  }

  private static String pageOf(int firstId, int count, String title, Integer total) {
    JSONArray jobs = new JSONArray();
    for (int id = firstId; id < firstId + count; id++) {
      jobs.put(
          new JSONObject()
              .put("id", id)
              .put("title", title)
              .put("link", "https://example.com/" + id));
    }
    return new JSONObject().put("jobs", jobs).put("totalCount", total).toString();
  }

  private void respond(int status, String body) {
    respond(status, body, Map.of());
  }

  private void respond(int status, String body, Map<String, String> headers) {
    respond(page -> status, page -> body, headers);
  }

  private void respond(
      IntUnaryOperator status, IntFunction<String> body, Map<String, String> headers) {
    server.createContext(
        "/api/fixture-key",
        exchange -> {
          try {
            calls.incrementAndGet();
            int page = 1;
            int code = 405;
            if ("POST".equals(exchange.getRequestMethod())
                && "application/json"
                    .equals(exchange.getRequestHeaders().getFirst("Content-Type"))) {
              received.set(
                  new JSONObject(
                      new String(
                          exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8)));
              page = received.get().getInt("page");
              code = status.applyAsInt(page);
            }
            byte[] bytes = body.apply(page).getBytes(StandardCharsets.UTF_8);
            headers.forEach(exchange.getResponseHeaders()::set);
            exchange.sendResponseHeaders(code, bytes.length);
            exchange.getResponseBody().write(bytes);
          } finally {
            exchange.close();
          }
        });
  }
}
