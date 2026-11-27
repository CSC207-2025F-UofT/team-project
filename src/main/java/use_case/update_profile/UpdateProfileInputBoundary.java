package use_case.update_profile;

import java.util.Optional;
import use_case.profile.ProfileData;
import use_case.profile.ProfileStorageException;
import use_case.profile.SavedProfile;

/** UC6 entry points for opening an existing profile and saving its edits. */
public interface UpdateProfileInputBoundary {
  /** Requests the current saved values. */
  void loadProfile();

  /** Reads the latest saved revision without changing the open form. */
  Optional<SavedProfile> readSnapshot() throws ProfileStorageException;

  /** Applies the draft to the snapshot the student originally edited. */
  void execute(ProfileData input, SavedProfile baseline);
}
