package interface_adapter.profile;

import interface_adapter.UiTasks;
import java.util.function.Consumer;
import java.util.function.Predicate;
import use_case.profile.ProfileData;
import use_case.profile.SavedProfile;
import use_case.update_profile.UpdateProfileInputBoundary;

/** Handles opening, retrying and editing an existing qualification profile. */
public final class UpdateProfileController implements ProfileFormController {
  private final UpdateProfileInputBoundary update;
  private final ProfileViewModel model;
  private final UiTasks tasks;
  private boolean checkingForChanges;

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

  /** Refreshes an untouched form or tells the student about the change while preserving a draft. */
  public void checkForChanges(Predicate<ProfileData> formMatches, Consumer<String> showNotice) {
    ProfileViewModel.State before = model.getState();
    if (checkingForChanges || !before.isEditable()) {
      return;
    }
    checkingForChanges = true;
    tasks.read(
        update::readSnapshot,
        latest -> {
          checkingForChanges = false;
          // A manual reload or save may have finished while the file was being read.
          if (model.getState() == before) {
            latest.ifPresent(
                saved -> showExternalSave(before.getSaved(), saved, formMatches, showNotice));
          }
        },
        () -> checkingForChanges = false);
  }

  private void showExternalSave(
      SavedProfile baseline,
      SavedProfile latest,
      Predicate<ProfileData> formMatches,
      Consumer<String> showNotice) {
    if (baseline != null && baseline.getRevision().equals(latest.getRevision())) {
      return;
    }
    ProfileData original =
        baseline == null ? ProfileData.blank() : ProfileData.from(baseline.getProfile());
    if (formMatches.test(original)) {
      model.saved(latest, "Profile refreshed with changes from another window.");
    } else if (baseline == null) {
      showNotice.accept("Another window created a profile. Reload profile to open it.");
    } else {
      showNotice.accept("Profile changed in another window. Your unsaved edits are still here.");
    }
  }

  /** Submits edits only for an existing profile. */
  @Override
  public void execute(ProfileData draft) {
    if (model.getState().getMode() == ProfileViewModel.Mode.EDIT) {
      SavedProfile baseline = model.getState().getSaved();
      model.saving(draft);
      tasks.run(
          () -> update.execute(draft, baseline),
          () -> model.saveFailed("Could not update the profile. Retry saving."));
    }
  }
}
