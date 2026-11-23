package use_case.update_profile;

import entity.QualificationProfile;
import java.util.Optional;
import use_case.profile.ProfileData;
import use_case.profile.ProfileRepository;
import use_case.profile.ProfileStorageException;

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
      profiles
          .load()
          .ifPresentOrElse(
              profile -> output.presentLoaded(ProfileData.from(profile)), output::presentMissing);
    } catch (ProfileStorageException exception) {
      output.presentLoadError(exception.getMessage());
    }
  }

  @Override
  public void execute(ProfileData input) {
    QualificationProfile edited;
    try {
      Optional<QualificationProfile> saved = profiles.load();
      if (saved.isEmpty()) {
        output.presentError("The saved profile is missing. Reload the profile to create one.");
        return;
      }
      edited = input.toProfile(saved.get().getOwner());
      profiles.update(edited);
    } catch (IllegalArgumentException | ProfileStorageException exception) {
      output.presentError(exception.getMessage());
      return;
    }
    output.presentSaved(ProfileData.from(edited));
  }
}
