package entity;

import java.util.Objects;

/** An immutable job snapshot shared by Finder, recommendations and saved applications. */
public final class JobPosting {
  private final String source;
  private final String boardToken;
  private final String externalJobId;
  private final String title;
  private final String company;
  private final String location;
  private final String descriptionText;
  private final String applicationUrl;
  private final WorkArrangement workArrangement;

  private JobPosting(Builder builder) {
    source = identityPart(builder.source);
    boardToken = identityPart(builder.boardToken);
    externalJobId = identityPart(builder.externalJobId);
    title = required(builder.title);
    company = required(builder.company);
    location = Objects.requireNonNull(builder.location);
    descriptionText = Objects.requireNonNull(builder.descriptionText);
    applicationUrl = required(builder.applicationUrl);
    workArrangement = Objects.requireNonNull(builder.workArrangement);
  }

  private static String required(String value) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("A required job field is blank.");
    }
    return value;
  }

  private static String identityPart(String value) {
    if (required(value).contains(":")) {
      throw new IllegalArgumentException("Job identity components cannot contain colons.");
    }
    return value;
  }

  public String getSource() {
    return source;
  }

  public String getBoardToken() {
    return boardToken;
  }

  public String getExternalJobId() {
    return externalJobId;
  }

  /** Returns a stable source:board:post key, independent of a result's title or position. */
  public String getStableId() {
    return source + ":" + boardToken + ":" + externalJobId;
  }

  public String getTitle() {
    return title;
  }

  public String getCompany() {
    return company;
  }

  /** Returns the supplied location, or an empty string when the source omits it. */
  public String getLocation() {
    return location;
  }

  public String getDescriptionText() {
    return descriptionText;
  }

  public String getApplicationUrl() {
    return applicationUrl;
  }

  public WorkArrangement getWorkArrangement() {
    return workArrangement;
  }

  /** Builds a snapshot without positional arguments for its optional fields. */
  public static final class Builder {
    private final String source;
    private final String boardToken;
    private final String externalJobId;
    private String title = "";
    private String company = "";
    private String location = "";
    private String descriptionText = "";
    private String applicationUrl = "";
    private WorkArrangement workArrangement = WorkArrangement.UNKNOWN;

    /**
     * Starts a posting identified by its source, company board and external post ID.
     *
     * @param source provider identifier, without colons
     * @param boardToken board identifier, without colons
     * @param externalJobId posting identifier, without colons
     */
    public Builder(String source, String boardToken, String externalJobId) {
      this.source = source;
      this.boardToken = boardToken;
      this.externalJobId = externalJobId;
    }

    /** Sets the employer's original title, used for internship classification and matching. */
    public Builder title(String value) {
      title = value;
      return this;
    }

    /** Sets the company display name associated with this board. */
    public Builder company(String value) {
      company = value;
      return this;
    }

    /** Sets the supplied location; use an empty string for missing information. */
    public Builder location(String value) {
      location = value;
      return this;
    }

    /** Sets searchable plain text; service-specific HTML must already have been removed. */
    public Builder descriptionText(String value) {
      descriptionText = value;
      return this;
    }

    /** Sets the employer's web application URL without recording an application. */
    public Builder applicationUrl(String value) {
      applicationUrl = value;
      return this;
    }

    /** Sets explicitly known work arrangements; the default is UNKNOWN. */
    public Builder workArrangement(WorkArrangement value) {
      workArrangement = value;
      return this;
    }

    /** Returns a validated snapshot; subsequent builder changes cannot modify it. */
    public JobPosting build() {
      return new JobPosting(this);
    }
  }
}
