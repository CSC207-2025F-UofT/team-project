package use_case.profile;

/** Outcomes common to the create and update forms. */
public interface ProfileSaveOutputBoundary {
  /** Displays the saved, normalized values. */
  void presentSaved(ProfileData saved);

  /** Reports an unsuccessful submission while the draft remains available for correction. */
  void presentError(String message);
}
