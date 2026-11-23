package use_case.update_profile;

import use_case.profile.ProfileData;

/** UC6 entry points for opening an existing profile and saving its edits. */
public interface UpdateProfileInputBoundary {
  /** Requests the current saved values. */
  void loadProfile();

  /** Validates and saves changes for the existing student. */
  void execute(ProfileData input);
}
