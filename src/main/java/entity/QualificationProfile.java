package entity;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** A student's qualifications and internship preferences at the time they last saved. */
public final class QualificationProfile {
  private final User owner;
  private final String program;
  private final Integer graduationYear;
  private final List<String> skills;
  private final List<String> preferredRoles;
  private final List<String> preferredLocations;
  private final Optional<WorkArrangement> preferredArrangement;

  /** Creates a validated snapshot and retains the first spelling of each distinct term. */
  public QualificationProfile(
      User owner,
      String program,
      Integer graduationYear,
      Collection<String> skills,
      Collection<String> preferredRoles,
      Collection<String> preferredLocations,
      Optional<WorkArrangement> preferredArrangement) {
    this.owner = Objects.requireNonNull(owner);
    this.program = Objects.requireNonNull(program).strip();
    if (this.program.isEmpty()) {
      throw new IllegalArgumentException("Enter your program of study.");
    }
    if (graduationYear != null && (graduationYear < 1000 || graduationYear > 9999)) {
      throw new IllegalArgumentException("Enter a four-digit graduation year or leave it blank.");
    }
    this.graduationYear = graduationYear;
    this.skills = distinct(skills);
    if (this.skills.isEmpty()) {
      throw new IllegalArgumentException("Enter at least one skill.");
    }
    this.preferredRoles = distinct(preferredRoles);
    this.preferredLocations = distinct(preferredLocations);
    this.preferredArrangement = Objects.requireNonNull(preferredArrangement);
    if (preferredArrangement.orElse(null) == WorkArrangement.UNKNOWN) {
      throw new IllegalArgumentException("Choose Any, Remote, Hybrid or On-site.");
    }
  }

  private static List<String> distinct(Collection<String> values) {
    Map<String, String> terms = new LinkedHashMap<>();
    for (String value : values) {
      String term = value.strip().replaceAll("(?U)\\s+", " ");
      if (!term.isEmpty()) {
        terms.putIfAbsent(term.toLowerCase(Locale.ROOT), term);
      }
    }
    return List.copyOf(terms.values());
  }

  public User getOwner() {
    return owner;
  }

  public String getProgram() {
    return program;
  }

  public Integer getGraduationYear() {
    return graduationYear;
  }

  public List<String> getSkills() {
    return skills;
  }

  public List<String> getPreferredRoles() {
    return preferredRoles;
  }

  public List<String> getPreferredLocations() {
    return preferredLocations;
  }

  public Optional<WorkArrangement> getPreferredArrangement() {
    return preferredArrangement;
  }
}
