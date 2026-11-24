package use_case.profile;

import entity.QualificationProfile;
import java.util.Optional;

/** Storage contract for the one local profile. Missing data and read failures are distinct. */
public interface ProfileRepository {
  /** Loads the most recent complete profile. */
  Optional<QualificationProfile> load() throws ProfileStorageException;

  /** Saves a first profile only if no profile already exists. */
  void create(QualificationProfile profile) throws ProfileStorageException;

  /** Replaces a saved profile belonging to the same student. */
  void update(QualificationProfile profile) throws ProfileStorageException;
}
