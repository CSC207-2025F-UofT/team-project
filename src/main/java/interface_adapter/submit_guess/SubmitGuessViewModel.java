package interface_adapter.submit_guess;

import java.util.ArrayList;
import java.util.List;

public class SubmitGuessViewModel {
  public static final String TITLE_LABEL = "Guess View";
  public static final String GUESS_BUTTON_LABEL = "go!";

  private final List<SubmitGuessViewModelListener> listeners = new ArrayList<>();

  public static final String VIEW_NAME = "guess";

  private SubmitGuessState state = new SubmitGuessState();

  public SubmitGuessState getState() {
    return this.state;
  }

  public void setState(SubmitGuessState state) {
    this.state = state;
  }

  public void addListener(SubmitGuessViewModelListener listener) {
    listeners.add(listener);
  }

  public void notifyStateChanged() {
    for (SubmitGuessViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
