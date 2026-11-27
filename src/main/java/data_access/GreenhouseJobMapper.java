package data_access;

import entity.JobPosting;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/** Converts service-specific JSON and encoded HTML to canonical plain job snapshots. */
final class GreenhouseJobMapper {
  List<JobPosting> parse(String body, JobBoard board) {
    JSONArray posts = new JSONObject(body).getJSONArray("jobs");
    List<JobPosting> jobs = new ArrayList<>();
    for (int index = 0; index < posts.length(); index++) {
      JSONObject post = posts.getJSONObject(index);
      String id = post.get("id").toString();
      if (!id.matches("[0-9]+")) {
        throw new IllegalArgumentException("Invalid job ID.");
      }
      String url = post.getString("absolute_url");
      URI uri = URI.create(url);
      if (uri.getHost() == null
          || !("https".equalsIgnoreCase(uri.getScheme())
              || "http".equalsIgnoreCase(uri.getScheme()))) {
        throw new IllegalArgumentException("Invalid application URL.");
      }
      String location = "";
      if (post.has("location") && !post.isNull("location")) {
        location = optionalText(post.getJSONObject("location"), "name");
      }
      jobs.add(
          new JobPosting.Builder("greenhouse", board.getToken(), id)
              .title(post.getString("title"))
              .company(board.getCompany())
              .location(location)
              .descriptionText(plainText(optionalText(post, "content")))
              .applicationUrl(url)
              .build());
    }
    return jobs;
  }

  private String optionalText(JSONObject object, String key) {
    return object.has(key) && !object.isNull(key) ? object.getString(key) : "";
  }

  private String plainText(String html) {
    // The API can encode markup as HTML entities, including a second escaped layer.
    String decoded = Parser.unescapeEntities(Parser.unescapeEntities(html, false), false);
    Document document = Jsoup.parse(decoded);
    document.select("script, style").remove();
    return document.text();
  }
}
