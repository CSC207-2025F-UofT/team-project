package use_case.create_profile;

import entity.QualificationProfile;
import entity.User;
import use_case.profile.ProfileData;
import use_case.profile.ProfileRepository;
import use_case.profile.ProfileSaveOutputBoundary;
import use_case.profile.ProfileStorageException;

/** Creates a profile and publishes success only after persistence completes. */
public final class CreateProfileInteractor implements CreateProfileInputBoundary {
  private final ProfileRepository profiles;
  private final ProfileSaveOutputBoundary output;

  /** Injects the persistence and presentation boundaries. */
  public CreateProfileInteractor(ProfileRepository profiles, ProfileSaveOutputBoundary output) {
    this.profiles = profiles;
    this.output = output;
  }

  @Override
  public void execute(ProfileData input) {
    QualificationProfile profile;
    try {
      profile = input.toProfile(new User());
      profiles.create(profile);
    } catch (IllegalArgumentException | ProfileStorageException exception) {
      output.presentError(exception.getMessage());
      return;
    }
    output.presentSaved(ProfileData.from(profile));
  }
}
