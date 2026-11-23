package interface_adapter.profile;

import java.util.concurrent.Executor;
import use_case.profile.ProfileData;
import use_case.profile.ProfileSaveOutputBoundary;

/** Presents create outcomes on the UI thread. */
public final class CreateProfilePresenter implements ProfileSaveOutputBoundary {
  private final ProfileViewModel model;
  private final Executor ui;

  /** Injects the screen model and its scheduler. */
  public CreateProfilePresenter(ProfileViewModel model, Executor ui) {
    this.model = model;
    this.ui = ui;
  }

  @Override
  public void presentSaved(ProfileData saved) {
    ui.execute(
        () -> model.saved(saved, "Profile created. Your recommendations are ready to view."));
  }

  @Override
  public void presentError(String message) {
    ui.execute(() -> model.saveFailed(message));
  }
}
