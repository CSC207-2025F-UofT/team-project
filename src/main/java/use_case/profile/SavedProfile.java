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
}
