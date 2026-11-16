package data_access;

/** A configured Greenhouse board token and its company display name. */
public final class JobBoard {
  private final String token;
  private final String company;

  /** Validates a URL-safe token and a nonblank company label for configured sources. */
  public JobBoard(String token, String company) {
    if (token == null || !token.matches("[A-Za-z0-9_-]+") || company == null || company.isBlank()) {
      throw new IllegalArgumentException("Each board needs a valid token and company name.");
    }
    this.token = token;
    this.company = company.strip();
  }

  public String getToken() {
    return token;
  }

  public String getCompany() {
    return company;
  }
}
