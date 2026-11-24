package interface_adapter.profile;

import java.util.concurrent.Executor;
import use_case.profile.ProfileData;
import use_case.update_profile.UpdateProfileOutputBoundary;

/** Presents saved, missing and unreadable profiles as distinct form states. */
public final class UpdateProfilePresenter implements UpdateProfileOutputBoundary {
  private final ProfileViewModel model;
  private final Executor ui;

  /** Injects the same form model used to create the profile. */
  public UpdateProfilePresenter(ProfileViewModel model, Executor ui) {
    this.model = model;
    this.ui = ui;
  }

  @Override
  public void presentSaved(ProfileData saved) {
    ui.execute(() -> model.saved(saved, "Changes saved."));
  }

  @Override
  public void presentLoaded(ProfileData saved) {
    ui.execute(() -> model.saved(saved, "Showing your saved profile."));
  }

  @Override
  public void presentMissing() {
    ui.execute(model::missing);
  }

  @Override
  public void presentError(String message) {
    ui.execute(() -> model.saveFailed(message));
  }

  @Override
  public void presentLoadError(String message) {
    ui.execute(() -> model.loadFailed(message));
  }
}
