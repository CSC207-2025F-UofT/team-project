package use_case.profile;

import entity.QualificationProfile;
import java.util.Objects;
import java.util.UUID;

/** A profile and the revision read or committed with it. */
public final class SavedProfile {
  private final QualificationProfile profile;
  private final UUID revision;

  /** Pairs the saved values with their storage revision. */
  public SavedProfile(QualificationProfile profile, UUID revision) {
    this.profile = Objects.requireNonNull(profile);
    this.revision = Objects.requireNonNull(revision);
  }

  public QualificationProfile getProfile() {
    return profile;
  }

  public UUID getRevision() {
    return revision;
  }

  /** Applies the fields the student changed since loading the form to this saved revision. */
  public QualificationProfile applyEdits(SavedProfile baseline, QualificationProfile edited)
      throws ProfileStorageException {
    QualificationProfile original = baseline.getProfile();
    String ownerId = profile.getOwner().getUserId();
    if (!ownerId.equals(original.getOwner().getUserId())
        || !ownerId.equals(edited.getOwner().getUserId())) {
      throw new ProfileStorageException(
          "The saved profile belongs to a different student. Reload it.");
    }
    if (revision.equals(baseline.getRevision())) {
      return edited;
    }
    return new QualificationProfile(
        profile.getOwner(),
        mergeField(original.getProgram(), edited.getProgram(), profile.getProgram()),
        mergeField(
            original.getGraduationYear(), edited.getGraduationYear(), profile.getGraduationYear()),
        mergeField(original.getSkills(), edited.getSkills(), profile.getSkills()),
        mergeField(
            original.getPreferredRoles(), edited.getPreferredRoles(), profile.getPreferredRoles()),
        mergeField(
            original.getPreferredLocations(),
            edited.getPreferredLocations(),
            profile.getPreferredLocations()),
        mergeField(
            original.getPreferredArrangement(),
            edited.getPreferredArrangement(),
            profile.getPreferredArrangement()));
  }

  private static <T> T mergeField(T original, T edited, T current) {
    return Objects.equals(original, edited) ? current : edited;
  }
}
