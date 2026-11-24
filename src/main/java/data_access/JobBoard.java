package data_access;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** A configured Greenhouse board token and its company display name. */
public final class JobBoard {
  private static final String TOKEN = "[A-Za-z0-9_-]+";
  private static final Pattern HOSTED_PATH = Pattern.compile("/(" + TOKEN + ")(?:/jobs/[0-9]+)?/?");

  private final String token;
  private final String company;

  /** Validates a URL-safe token and a nonblank company label for configured sources. */
  public JobBoard(String token, String company) {
    if (token == null || !token.matches(TOKEN) || company == null || company.isBlank()) {
      throw new IllegalArgumentException("Each board needs a valid token and company name.");
    }
    this.token = token;
    this.company = company.strip();
  }

  /** Extracts a board identifier from a token or a hosted board or job URL. */
  static String tokenFrom(String selection) {
    String text = selection.strip();
    if (text.matches(TOKEN)) {
      return text;
    }
    URI uri = URI.create(text);
    String host = uri.getHost();
    Matcher path = HOSTED_PATH.matcher(uri.getRawPath() == null ? "" : uri.getRawPath());
    if (("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()))
        && ("job-boards.greenhouse.io".equalsIgnoreCase(host)
            || "boards.greenhouse.io".equalsIgnoreCase(host))
        && uri.getUserInfo() == null
        && uri.getPort() == -1
        && path.matches()) {
      return path.group(1);
    }
    throw new IllegalArgumentException("Invalid Greenhouse board.");
  }

  public String getToken() {
    return token;
  }

  public String getCompany() {
    return company;
  }
}
