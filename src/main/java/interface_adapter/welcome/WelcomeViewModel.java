package interface_adapter.welcome;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Welcome View.
 */
public class WelcomeViewModel {

  public static final String TITLE_LABEL = "Welcome View";

  private final List<WelcomeViewModelListener> listeners = new ArrayList<>();

  public static final String VIEW_NAME = "welcome";

  private WelcomeState state = new WelcomeState();

  public WelcomeState getState() {
    return this.state;
  }

  public void setState(WelcomeState state) {
    this.state = state;
  }

  public void addListener(WelcomeViewModelListener listener) {
    listeners.add(listener);
  }

  public void notifyStateChanged() {
    for (WelcomeViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
