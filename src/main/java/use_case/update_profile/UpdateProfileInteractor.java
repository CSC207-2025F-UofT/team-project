package use_case.update_profile;

import entity.QualificationProfile;
import use_case.profile.ProfileData;
import use_case.profile.ProfileRepository;
import use_case.profile.ProfileStorageException;
import use_case.profile.SavedProfile;

/** Loads and replaces a student's saved qualifications while preserving their identity. */
public final class UpdateProfileInteractor implements UpdateProfileInputBoundary {
  private final ProfileRepository profiles;
  private final UpdateProfileOutputBoundary output;

  /** Injects the same repository used by creation and recommendations. */
  public UpdateProfileInteractor(ProfileRepository profiles, UpdateProfileOutputBoundary output) {
    this.profiles = profiles;
    this.output = output;
  }

  @Override
  public void loadProfile() {
    try {
      profiles.loadSnapshot().ifPresentOrElse(output::presentLoaded, output::presentMissing);
    } catch (ProfileStorageException exception) {
      output.presentLoadError(exception.getMessage());
    }
  }

  @Override
  public void execute(ProfileData input, SavedProfile baseline) {
    SavedProfile saved;
    try {
      QualificationProfile edited = input.toProfile(baseline.getProfile().getOwner());
      saved = profiles.update(edited, baseline);
    } catch (IllegalArgumentException | ProfileStorageException exception) {
      output.presentError(exception.getMessage());
      return;
    }
    output.presentSaved(saved);
  }
}
