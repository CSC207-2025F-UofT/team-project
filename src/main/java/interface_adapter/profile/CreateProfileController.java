package interface_adapter.profile;

import interface_adapter.UiTasks;
import use_case.create_profile.CreateProfileInputBoundary;
import use_case.profile.ProfileData;

/** Submits a new profile when the load confirmed that no saved profile exists. */
public final class CreateProfileController implements ProfileFormController {
  private final CreateProfileInputBoundary create;
  private final ProfileViewModel model;
  private final UiTasks tasks;

  /** Connects the form to the create input boundary. */
  public CreateProfileController(
      CreateProfileInputBoundary create, ProfileViewModel model, UiTasks tasks) {
    this.create = create;
    this.model = model;
    this.tasks = tasks;
  }

  /** Captures the draft so validation and write failures preserve it. */
  @Override
  public void execute(ProfileData draft) {
    if (model.getState().getMode() == ProfileViewModel.Mode.CREATE) {
      model.saving(draft);
      tasks.run(
          () -> create.execute(draft),
          () -> model.saveFailed("Could not create the profile. Retry saving."));
    }
  }
}
