package interface_adapter.profile;

import interface_adapter.ObservableViewModel;
import use_case.profile.ProfileData;

/** Tracks the saved snapshot separately from the student's draft on the shared profile form. */
public final class ProfileViewModel extends ObservableViewModel<ProfileViewModel.State> {
  /** Form modes distinguish a missing profile from an unreadable one. */
  public enum Mode {
    LOADING,
    CREATE,
    EDIT,
    SAVING,
    UNAVAILABLE
  }

  /** An immutable form update; a null saved snapshot means no successful load or save yet. */
  public static final class State {
    private final Mode mode;
    private final ProfileData saved;
    private final ProfileData draft;
    private final String message;

    private State(Mode mode, ProfileData saved, ProfileData draft, String message) {
      this.mode = mode;
      this.saved = saved;
      this.draft = draft;
      this.message = message;
    }

    public Mode getMode() {
      return mode;
    }

    public ProfileData getDraft() {
      return draft;
    }

    public String getMessage() {
      return message;
    }

    public boolean isEditable() {
      return mode == Mode.CREATE || mode == Mode.EDIT;
    }
  }

  /** Starts with editing disabled until persistence reports whether a profile exists. */
  public ProfileViewModel() {
    super(new State(Mode.UNAVAILABLE, null, ProfileData.blank(), ""));
  }

  /** Disables form actions while a saved profile is being read. */
  public void loading() {
    State old = getState();
    publish(new State(Mode.LOADING, old.saved, old.draft, "Loading profile…"));
  }

  /** Enables the new-profile form only after a successful empty load. */
  public void missing() {
    publish(
        new State(
            Mode.CREATE, null, ProfileData.blank(), "Enter your program and at least one skill."));
  }

  /** Installs freshly persisted or loaded values as the new baseline for Cancel. */
  public void saved(ProfileData data, String message) {
    publish(new State(Mode.EDIT, data, data, message));
  }

  /** Captures unsaved text before scheduling a write. */
  public void saving(ProfileData draft) {
    publish(new State(Mode.SAVING, getState().saved, draft, "Saving profile…"));
  }

  /** Restores the editable draft when validation or persistence fails. */
  public void saveFailed(String message) {
    State old = getState();
    publish(new State(old.saved == null ? Mode.CREATE : Mode.EDIT, old.saved, old.draft, message));
  }

  /** Keeps an unreadable profile distinct from an empty profile and offers a reload. */
  public void loadFailed(String message) {
    State old = getState();
    publish(new State(Mode.UNAVAILABLE, old.saved, old.draft, message));
  }

  /** Discards edits in the form without writing to persistent storage. */
  public void cancel() {
    State old = getState();
    if (old.isEditable()) {
      publish(
          new State(
              old.mode,
              old.saved,
              old.saved == null ? ProfileData.blank() : old.saved,
              "Edits discarded."));
    }
  }
}
