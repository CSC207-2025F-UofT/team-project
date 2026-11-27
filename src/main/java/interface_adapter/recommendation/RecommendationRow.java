package interface_adapter.recommendation;

/** Plain display values for one ranked internship, including its matching reasons. */
public final class RecommendationRow {
  private final String title;
  private final String company;
  private final String location;
  private final String arrangement;
  private final String skills;
  private final String url;

  /** Retains the original application address alongside its display columns. */
  public RecommendationRow(
      String title,
      String company,
      String location,
      String arrangement,
      String skills,
      String url) {
    this.title = title;
    this.company = company;
    this.location = location;
    this.arrangement = arrangement;
    this.skills = skills;
    this.url = url;
  }

  public String getTitle() {
    return title;
  }

  public String getCompany() {
    return company;
  }

  public String getLocation() {
    return location;
  }

  public String getArrangement() {
    return arrangement;
  }

  public String getSkills() {
    return skills;
  }

  public String getUrl() {
    return url;
  }
}
