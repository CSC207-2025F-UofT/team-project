package data_access;

import entity.JobPosting;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import use_case.port.CompanyBoardSource;
import use_case.port.JobFetchResult;
import use_case.port.JobSourceException;
import use_case.port.SourceFailure;

/** Retrieves public Greenhouse boards without credentials; isolates failures per board. */
public final class GreenhouseJobSource implements CompanyBoardSource {
  private final HttpClient client;
  private final URI baseUri;
  private final List<JobBoard> boards;
  private final Duration timeout;
  private final GreenhouseJobMapper mapper = new GreenhouseJobMapper();

  /**
   * Configures the transport; the base URI is injectable for deterministic local-server tests.
   *
   * @param client reusable HTTP client with a connection timeout
   * @param baseUri boards endpoint, ending in a slash
   * @param boards configured company boards
   * @param timeout maximum duration of one request
   */
  public GreenhouseJobSource(
      HttpClient client, URI baseUri, List<JobBoard> boards, Duration timeout) {
    if (!baseUri.toString().endsWith("/") || timeout.isZero() || timeout.isNegative()) {
      throw new IllegalArgumentException("Use a trailing-slash boards URI and a positive timeout.");
    }
    this.client = client;
    this.baseUri = baseUri;
    this.boards = List.copyOf(boards);
    this.timeout = timeout;
  }

  @Override
  public Map<String, String> getCompanies() {
    Map<String, String> names = new LinkedHashMap<>();
    boards.forEach(board -> names.put(board.getToken(), board.getCompany()));
    return Collections.unmodifiableMap(names);
  }

  @Override
  public JobFetchResult fetchCompany(String selection) throws JobSourceException {
    final String token;
    try {
      token = JobBoard.tokenFrom(selection);
    } catch (IllegalArgumentException exception) {
      throw new JobSourceException("Enter a Greenhouse board token or a hosted board URL.");
    }
    String name = getCompanies().getOrDefault(token, token);
    return fetchBoards(List.of(new JobBoard(token, name)));
  }

  @Override
  public JobFetchResult fetch() throws JobSourceException {
    return fetchBoards(boards);
  }

  private JobFetchResult fetchBoards(List<JobBoard> selected) throws JobSourceException {
    List<JobPosting> jobs = new ArrayList<>();
    List<String> successes = new ArrayList<>();
    List<SourceFailure> failures = new ArrayList<>();
    for (JobBoard board : selected) {
      HttpRequest request =
          HttpRequest.newBuilder(baseUri.resolve(board.getToken() + "/jobs?content=true"))
              .timeout(timeout)
              .header("Accept", "application/json")
              .GET()
              .build();
      try {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
          failures.add(new SourceFailure(board.getToken(), SourceFailure.Reason.HTTP_ERROR));
          continue;
        }
        // Parse a whole board before publishing it: malformed data cannot look complete.
        List<JobPosting> boardJobs = mapper.parse(response.body(), board);
        jobs.addAll(boardJobs);
        successes.add(board.getToken());
      } catch (HttpTimeoutException exception) {
        failures.add(new SourceFailure(board.getToken(), SourceFailure.Reason.TIMEOUT));
      } catch (IOException exception) {
        failures.add(new SourceFailure(board.getToken(), SourceFailure.Reason.NETWORK));
      } catch (JSONException | IllegalArgumentException exception) {
        failures.add(new SourceFailure(board.getToken(), SourceFailure.Reason.INVALID_RESPONSE));
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        throw new JobSourceException("Job retrieval was interrupted.");
      }
    }
    if (successes.isEmpty()) {
      if (selected.size() == 1) {
        throw new JobSourceException(
            "Could not retrieve the Greenhouse board "
                + selected.get(0).getToken()
                + ". Check the board token or try again later.");
      }
      throw new JobSourceException("No configured job board could be retrieved.");
    }
    return new JobFetchResult(jobs, successes, failures);
  }
}
