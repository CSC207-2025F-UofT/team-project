package data_access;

import entity.JobPosting;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import use_case.port.JobFetchResult;
import use_case.port.JobSourceException;
import use_case.port.PagedJobSearchSource;
import use_case.search_jobs.SearchJobsInputData;

/** Retrieves numbered pages of Canadian vacancies using a Canada-issued Jooble REST API key. */
public final class JoobleJobSearch implements PagedJobSearchSource {
  private static final Duration TIMEOUT = Duration.ofSeconds(8);
  private final HttpClient client;
  private final URI endpoint;

  /** Uses the Canadian REST endpoint for a key issued by Jooble Canada. */
  public JoobleJobSearch(String apiKey) {
    this(HttpClient.newBuilder().connectTimeout(TIMEOUT).build(), canadianEndpoint(apiKey));
  }

  JoobleJobSearch(HttpClient client, URI endpoint) {
    this.client = client;
    this.endpoint = endpoint;
  }

  static URI canadianEndpoint(String apiKey) {
    String key = URLEncoder.encode(apiKey.strip(), StandardCharsets.UTF_8).replace("+", "%20");
    return URI.create("https://ca.jooble.org/api/" + key);
  }

  @Override
  public Page fetchPage(SearchJobsInputData input, int pageNumber) throws JobSourceException {
    JSONObject payload =
        new JSONObject()
            .put("keywords", input.getQuery())
            .put("companysearch", false)
            .put("location", input.getLocation().isBlank() ? "Canada" : input.getLocation())
            .put("page", pageNumber);
    HttpRequest request =
        HttpRequest.newBuilder(endpoint)
            .timeout(TIMEOUT)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
            .build();
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      boolean challenge =
          "challenge".equalsIgnoreCase(response.headers().firstValue("cf-mitigated").orElse(""));
      boolean denied = response.statusCode() == 401 || response.statusCode() == 403;
      boolean html =
          response
              .headers()
              .firstValue("Content-Type")
              .orElse("")
              .toLowerCase(Locale.ROOT)
              .startsWith("text/html");
      if (challenge || (denied && html)) {
        String requestId =
            response
                .headers()
                .firstValue("cf-ray")
                .filter(id -> id.matches("[0-9a-fA-F]{16}(?:-[A-Z0-9]{3})?"))
                .map(id -> " Ray ID: " + id)
                .orElse("");
        String reason = challenge ? "a Cloudflare browser check" : "an HTML access-denied page";
        throw new JobSourceException(
            "Jooble returned "
                + reason
                + " (HTTP "
                + response.statusCode()
                + "). Contact Jooble API support."
                + requestId);
      }
      if (denied) {
        throw new JobSourceException(
            "Jooble denied access (HTTP "
                + response.statusCode()
                + "). Check the Canadian API key and its access with Jooble.");
      }
      if (response.statusCode() == 429) {
        throw new JobSourceException("Jooble's request limit was reached. Try again later.");
      }
      if (response.statusCode() != 200) {
        throw new JobSourceException(
            "Jooble returned HTTP " + response.statusCode() + ". Please try again.");
      }
      JSONObject responseData = new JSONObject(response.body());
      JSONArray results = responseData.getJSONArray("jobs");
      List<JobPosting> jobs = new ArrayList<>();
      for (int index = 0; index < results.length(); index++) {
        jobs.add(toPosting(results.getJSONObject(index)));
      }
      return new Page(
          new JobFetchResult(jobs, List.of("jooble"), List.of()),
          responseData.optLong("totalCount", -1));
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new JobSourceException("Jooble search was interrupted.");
    } catch (HttpTimeoutException exception) {
      throw new JobSourceException("Jooble took too long to respond. Please try again.");
    } catch (IOException exception) {
      throw new JobSourceException("Could not reach Jooble. Please try again.");
    } catch (JSONException | IllegalArgumentException exception) {
      throw new JobSourceException("Jooble returned unreadable listings. Please try again.");
    }
  }

  private static JobPosting toPosting(JSONObject job) {
    String link = job.getString("link");
    URI uri = URI.create(link);
    if (uri.getHost() == null
        || !("https".equalsIgnoreCase(uri.getScheme())
            || "http".equalsIgnoreCase(uri.getScheme()))) {
      throw new IllegalArgumentException("Expected a web link.");
    }
    String company = job.optString("company", "").strip();
    return new JobPosting.Builder("jooble", "ca", job.optString("id", ""))
        .title(Jsoup.parse(job.getString("title")).text())
        .company(company.isEmpty() ? "Company not listed" : company)
        .location(job.optString("location", ""))
        .descriptionText(Jsoup.parse(job.optString("snippet", "")).text())
        .applicationUrl(link)
        .build();
  }
}
