package interface_adapter.profile;

import use_case.profile.ProfileData;

/** Shared entry point for collecting Swing form values into a use-case request. */
public interface ProfileFormController {
  /** Builds input data from the six fields and sends it to the selected use-case controller. */
  default void submit(
      String program,
      String year,
      String skills,
      String roles,
      String locations,
      String arrangement) {
    execute(new ProfileData(program, year, skills, roles, locations, arrangement));
  }

  /** Submits only when the form is in the appropriate mode for this use case. */
  void execute(ProfileData draft);
}
