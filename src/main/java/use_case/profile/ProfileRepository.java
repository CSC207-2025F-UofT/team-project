package use_case.profile;

import entity.QualificationProfile;
import java.util.Optional;
import java.util.UUID;

/** Storage contract for the one local profile. Missing data and read failures are distinct. */
public interface ProfileRepository {
  /** Loads the most recent complete profile. */
  default Optional<QualificationProfile> load() throws ProfileStorageException {
    return loadSnapshot().map(SavedProfile::getProfile);
  }

  /** Loads both the values to edit and their revision in a single read. */
  Optional<SavedProfile> loadSnapshot() throws ProfileStorageException;

  /** Saves a first profile only if no profile already exists. */
  SavedProfile create(QualificationProfile profile) throws ProfileStorageException;

  /** Replaces the same student's expected revision and returns the newly committed snapshot. */
  SavedProfile update(QualificationProfile profile, UUID expectedRevision)
      throws ProfileStorageException;
}
