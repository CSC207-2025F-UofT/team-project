package use_case.update_profile;

import use_case.profile.ProfileData;
import use_case.profile.SavedProfile;

/** UC6 entry points for opening an existing profile and saving its edits. */
public interface UpdateProfileInputBoundary {
  /** Requests the current saved values. */
  void loadProfile();

  /** Applies the draft to the snapshot the student originally edited. */
  void execute(ProfileData input, SavedProfile baseline);
}
