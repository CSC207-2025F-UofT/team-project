package use_case.create_profile;

import use_case.profile.ProfileData;

/** UC5 entry point for creating the student's first profile. */
@FunctionalInterface
public interface CreateProfileInputBoundary {
  /** Validates and persists the qualifications entered by the student. */
  void execute(ProfileData input);
}
