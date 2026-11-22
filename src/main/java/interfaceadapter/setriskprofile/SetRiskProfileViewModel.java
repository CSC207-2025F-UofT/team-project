package interfaceadapter.setriskprofile;

import interfaceadapter.ViewModel;
import java.util.List;

/**
 * The view model for the Set Risk Profile view.
 */
public class SetRiskProfileViewModel extends ViewModel<SetRiskProfileState> {
  public static final String VIEW_NAME = "set risk profile";

  /** Fired when the presets and current settings have been loaded. */
  public static final String SETTINGS_PROPERTY = "settings";

  /** Fired when an estimate has been calculated. */
  public static final String RESULT_PROPERTY = "result";

  /** Fired when an error message should be shown. */
  public static final String ERROR_PROPERTY = "error";

  /** Past price windows (in months) the user can choose from. */
  public static final List<Integer> WINDOW_OPTIONS = List.of(1, 3, 6, 12, 24);

  /**
   * Creates a new SetRiskProfileViewModel with an empty state.
   */
  public SetRiskProfileViewModel() {
    super(VIEW_NAME);
    setState(new SetRiskProfileState());
  }

  /**
   * Formats a number of months for display, e.g. "6 months" or "2 years".
   *
   * @param months Number of months
   * @return Human-readable description of the duration
   */
  public static String formatMonths(int months) {
    if (months % 12 == 0) {
      int years = months / 12;
      return years + (years == 1 ? " year" : " years");
    }
    return months + (months == 1 ? " month" : " months");
  }
}
