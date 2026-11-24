package use_case.update_profile;

import use_case.profile.ProfileData;
import use_case.profile.ProfileSaveOutputBoundary;

/** Separates opening a form from reporting a save result. */
public interface UpdateProfileOutputBoundary extends ProfileSaveOutputBoundary {
  /** Loads saved values into the editor. */
  void presentLoaded(ProfileData saved);

  /** Offers creation when no profile exists yet. */
  void presentMissing();

  /** Offers a retry when the saved file cannot be read. */
  void presentLoadError(String message);
}
