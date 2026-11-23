package interfaceadapter.setriskprofile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import usecase.setriskprofile.SetRiskProfileOutputBoundary;
import usecase.setriskprofile.SetRiskProfileOutputData;
import usecase.setriskprofile.SetRiskProfileSettingsOutputData;

/**
 * The presenter for the Set Risk Profile use case.
 */
public class SetRiskProfilePresenter implements SetRiskProfileOutputBoundary {
  private static final int TRADING_DAYS_PER_MONTH = 21;
  private static final DateTimeFormatter DATE_FORMAT =
          DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US);

  private final SetRiskProfileViewModel viewModel;

  /**
   * Creates a new Set Risk Profile presenter.
   *
   * @param viewModel View model for the use case
   */
  public SetRiskProfilePresenter(SetRiskProfileViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void prepareSuccessView(SetRiskProfileOutputData outputData) {
    SetRiskProfileState state = viewModel.getState();
    double sellPrice = outputData.getSellPrice();
    double error = outputData.getError();

    state.setResultTicker(outputData.getTicker());
    state.setBuyPrice(formatDollars(outputData.getStartPrice()) + " (latest close)");
    state.setSellDate(outputData.getSellDate().format(DATE_FORMAT) + " ("
            + outputData.getHoldPeriod() + " trading days)");
    state.setSellPrice(formatDollars(sellPrice));

    // The error is the standard deviation of the log return, so the +/- 1 standard deviation
    // price range is sellPrice * e^(-error) to sellPrice * e^(error).
    state.setPriceRange(formatDollars(sellPrice * Math.exp(-error)) + " – "
            + formatDollars(sellPrice * Math.exp(error)));
    double returns = outputData.getReturns();
    state.setReturns((returns < 0 ? "-" : "+") + formatDollars(Math.abs(returns)));
    state.setTargetReached(outputData.isTargetReached());
    state.setTargetMessage(outputData.isTargetReached()
            ? "Target gain is reached within your maximum holding period."
            : "Target gain is not reached within your maximum holding period; "
              + "this estimate sells at the end of it.");
    state.setErrorMessage(null);

    viewModel.setState(state);
    viewModel.firePropertyChange(SetRiskProfileViewModel.RESULT_PROPERTY);
  }

  @Override
  public void prepareFailView(String errorMessage) {
    SetRiskProfileState state = viewModel.getState();
    state.setErrorMessage(errorMessage);
    viewModel.setState(state);
    viewModel.firePropertyChange(SetRiskProfileViewModel.ERROR_PROPERTY);
  }

  @Override
  public void prepareSettingsView(SetRiskProfileSettingsOutputData outputData) {
    SetRiskProfileState state = viewModel.getState();

    List<SetRiskProfileState.PresetOption> options = new ArrayList<>();
    int selectedIndex = -1;
    List<SetRiskProfileSettingsOutputData.Preset> presets = outputData.getPresets();
    for (int i = 0; i < presets.size(); i++) {
      SetRiskProfileSettingsOutputData.Preset preset = presets.get(i);
      double gainPercent = preset.getTargetGain() * 100;
      String label = String.format("%s (+%s%%, %s)", capitalize(preset.getName()),
              formatPercent(gainPercent), formatTradingDays(preset.getMaxHoldingPeriod()));
      options.add(new SetRiskProfileState.PresetOption(label, gainPercent,
              preset.getMaxHoldingPeriod()));
      if (preset.getName().equals(outputData.getSelectedPreset())) {
        selectedIndex = i;
      }
    }

    state.setPresets(options);
    state.setSelectedPresetIndex(selectedIndex);
    state.setTargetGainPercent(outputData.getTargetGain() * 100);
    state.setMaxHoldingPeriod(outputData.getMaxHoldingPeriod());
    state.setWindowMonths(outputData.getWindow());

    viewModel.setState(state);
    viewModel.firePropertyChange(SetRiskProfileViewModel.SETTINGS_PROPERTY);
  }

  private static String formatDollars(double amount) {
    return String.format(Locale.US, "$%,.2f", amount);
  }

  private static String formatPercent(double percent) {
    if (percent == Math.rint(percent)) {
      return String.valueOf((long) percent);
    }
    return String.format(Locale.US, "%.2f", percent);
  }

  private static String capitalize(String name) {
    String lower = name.toLowerCase(Locale.ROOT);
    return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
  }

  private static String formatTradingDays(int tradingDays) {
    if (tradingDays % TRADING_DAYS_PER_MONTH == 0) {
      return SetRiskProfileViewModel.formatMonths(tradingDays / TRADING_DAYS_PER_MONTH);
    }
    return tradingDays + " trading days";
  }
}
