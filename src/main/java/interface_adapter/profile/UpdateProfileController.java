package interface_adapter.profile;

import interface_adapter.UiTasks;
import use_case.profile.ProfileData;
import use_case.update_profile.UpdateProfileInputBoundary;

/** Handles opening, retrying and editing an existing qualification profile. */
public final class UpdateProfileController implements ProfileFormController {
  private final UpdateProfileInputBoundary update;
  private final ProfileViewModel model;
  private final UiTasks tasks;

  /** Connects the shared form to the update input boundary. */
  public UpdateProfileController(
      UpdateProfileInputBoundary update, ProfileViewModel model, UiTasks tasks) {
    this.update = update;
    this.model = model;
    this.tasks = tasks;
  }

  /** Loads the currently saved profile unless another form operation is in flight. */
  public void loadProfile() {
    ProfileViewModel.Mode mode = model.getState().getMode();
    if (mode != ProfileViewModel.Mode.LOADING && mode != ProfileViewModel.Mode.SAVING) {
      model.loading();
      tasks.run(
          update::loadProfile,
          () -> model.loadFailed("Could not load the profile. Retry loading."));
    }
  }

  /** Submits edits only for an existing profile. */
  @Override
  public void execute(ProfileData draft) {
    if (model.getState().getMode() == ProfileViewModel.Mode.EDIT) {
      model.saving(draft);
      tasks.run(
          () -> update.execute(draft),
          () -> model.saveFailed("Could not update the profile. Retry saving."));
    }
  }
}
