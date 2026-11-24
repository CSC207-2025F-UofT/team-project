package use_case.profile;

import entity.QualificationProfile;
import entity.User;
import entity.WorkArrangement;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/** Plain form values shared by create, update and their output boundaries. */
public final class ProfileData {
  private final String program;
  private final String year;
  private final String skills;
  private final String roles;
  private final String locations;
  private final String arrangement;

  /** Captures the original input, including values the student may need to correct. */
  public ProfileData(
      String program,
      String year,
      String skills,
      String roles,
      String locations,
      String arrangement) {
    this.program = program;
    this.year = year;
    this.skills = skills;
    this.roles = roles;
    this.locations = locations;
    this.arrangement = arrangement;
  }

  /** Empty values for the initial form. */
  public static ProfileData blank() {
    return new ProfileData("", "", "", "", "", "ANY");
  }

  /** Converts normalized domain values back into editable fields. */
  public static ProfileData from(QualificationProfile profile) {
    Integer year = profile.getGraduationYear();
    return new ProfileData(
        profile.getProgram(),
        year == null ? "" : year.toString(),
        String.join(", ", profile.getSkills()),
        String.join(", ", profile.getPreferredRoles()),
        String.join(", ", profile.getPreferredLocations()),
        profile.getPreferredArrangement().map(Enum::name).orElse("ANY"));
  }

  /** Converts and validates a submitted form, preserving the supplied student identity. */
  public QualificationProfile toProfile(User owner) {
    String text = year.strip();
    if (!text.isEmpty() && !text.matches("[1-9][0-9]{3}")) {
      throw new IllegalArgumentException("Enter a four-digit graduation year or leave it blank.");
    }
    return new QualificationProfile(
        owner,
        program,
        text.isEmpty() ? null : Integer.valueOf(text),
        terms(skills),
        terms(roles),
        terms(locations),
        readArrangement());
  }

  private Optional<WorkArrangement> readArrangement() {
    String value = arrangement.strip().toUpperCase(Locale.ROOT);
    if (value.isEmpty() || "ANY".equals(value)) {
      return Optional.empty();
    }
    try {
      return Optional.of(WorkArrangement.valueOf(value));
    } catch (IllegalArgumentException exception) {
      throw new IllegalArgumentException("Choose Any, Remote, Hybrid or On-site.", exception);
    }
  }

  private static List<String> terms(String value) {
    return Arrays.asList(value.split("[,\\n\\r]"));
  }

  public String getProgram() {
    return program;
  }

  public String getYear() {
    return year;
  }

  public String getSkills() {
    return skills;
  }

  public String getRoles() {
    return roles;
  }

  public String getLocations() {
    return locations;
  }

  public String getArrangement() {
    return arrangement;
  }
}
