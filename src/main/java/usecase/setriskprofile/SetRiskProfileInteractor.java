package usecase.setriskprofile;

import entities.DailyAggregate;
import entities.GrowthStats;
import entities.RiskProfile;
import entities.RiskProfilePreset;
import entities.RiskProfileSettings;
import entities.SellEstimate;
import entities.Stock;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import usecase.shared.projection.SellStrategy;

/**
 * The Set Risk Profile Interactor.
 */
public class SetRiskProfileInteractor implements SetRiskProfileInputBoundary {
  private static final RiskProfilePreset DEFAULT_PRESET = RiskProfilePreset.MODERATE;
  private static final int DEFAULT_WINDOW = 12;
  private static final ZoneId MARKET_TIME_ZONE = ZoneId.of("America/New_York");

  private final SetRiskProfileStockDataAccessInterface stockDataAccessObject;
  private final SetRiskProfileSettingsDataAccessInterface settingsDataAccessObject;
  private final SetRiskProfileOutputBoundary presenter;
  private final SellStrategy strategy;

  /**
   * Creates a new SetRiskProfileInteractor.
   *
   * @param stockDataAccessInterface Data access object for stock price history
   * @param settingsDataAccessInterface Data access object for persisting the user's settings
   * @param outputBoundary The presenter
   * @param strategy Algorithm used to estimate when to sell
   */
  public SetRiskProfileInteractor(
          SetRiskProfileStockDataAccessInterface stockDataAccessInterface,
          SetRiskProfileSettingsDataAccessInterface settingsDataAccessInterface,
          SetRiskProfileOutputBoundary outputBoundary,
          SellStrategy strategy) {
    this.stockDataAccessObject = stockDataAccessInterface;
    this.settingsDataAccessObject = settingsDataAccessInterface;
    this.presenter = outputBoundary;
    this.strategy = strategy;
  }

  @Override
  public void execute(SetRiskProfileInputData inputData) {
    SetRiskProfileOutputData outputData;
    RiskProfileSettings settings;
    try {
      String ticker = inputData.getTicker();
      Stock stock = new Stock(ticker);
      double amount = inputData.getAmount();
      if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be positive");
      }
      RiskProfile profile = new RiskProfile(inputData.getTargetGain(),
              inputData.getMaxHoldingPeriod());
      settings = new RiskProfileSettings(profile, inputData.getWindow());

      // Fetch stock data according to the price window
      LocalDate to = LocalDate.now(MARKET_TIME_ZONE);
      LocalDate from = to.minusMonths(settings.getPriceWindowMonths());
      List<DailyAggregate> history;
      try {
        history = stockDataAccessObject.getDailyAggregates(stock, from, to);
      } catch (RuntimeException e) {
        // Error occurred while invoking the data access object
        presenter.prepareFailView("Could not retrieve price data for " + ticker + ": "
                + e.getMessage());
        return;
      }
      if (history == null || history.isEmpty()) {
        throw new IllegalArgumentException("No price data found for " + ticker);
      }

      GrowthStats stats = new GrowthStats(history);
      DailyAggregate latest = history.get(history.size() - 1);
      double startPrice = latest.getClose();
      if (amount < startPrice) {
        throw new IllegalArgumentException(String.format(
                "Amount must be at least the price of one share ($%.2f)", startPrice));
      }

      SellEstimate estimate = strategy.estimate(amount, startPrice, stats, profile);
      LocalDate lastTradingDay = latest.getTime().atZone(MARKET_TIME_ZONE).toLocalDate();
      LocalDate sellDate = addTradingDays(lastTradingDay, estimate.getHoldPeriod());

      outputData = new SetRiskProfileOutputData(ticker, startPrice, sellDate,
              estimate.getHoldPeriod(), estimate.getSellPrice(), estimate.getError(),
              estimate.getReturns(), estimate.isTargetReached());
    } catch (IllegalArgumentException e) {
      // Any validation error was encountered
      presenter.prepareFailView(e.getMessage());
      return;
    }

    presenter.prepareSuccessView(outputData);
    try {
      settingsDataAccessObject.saveSettings(settings);
    } catch (IOException e) {
      presenter.prepareFailView("Estimate complete, but your settings could not be saved: "
              + e.getMessage());
    }
  }

  @Override
  public void loadSettings() {
    RiskProfileSettings settings = new RiskProfileSettings(DEFAULT_PRESET.toRiskProfile(),
            DEFAULT_WINDOW);
    String loadError = null;
    try {
      Optional<RiskProfileSettings> saved = settingsDataAccessObject.loadSettings();
      if (saved.isPresent()) {
        settings = saved.get();
      }
    } catch (IOException e) {
      loadError = "Could not load your saved settings, so defaults were used: " + e.getMessage();
    }

    List<SetRiskProfileSettingsOutputData.Preset> presets = new ArrayList<>();
    for (RiskProfilePreset preset : RiskProfilePreset.values()) {
      presets.add(new SetRiskProfileSettingsOutputData.Preset(preset.name(),
              preset.getTargetGain(), preset.getMaxHoldingPeriod()));
    }
    RiskProfile profile = settings.getRiskProfile();
    String selectedPreset = RiskProfilePreset.matching(profile)
            .map(RiskProfilePreset::name)
            .orElse(null);

    presenter.prepareSettingsView(new SetRiskProfileSettingsOutputData(presets, selectedPreset,
            profile.getTargetGain(), profile.getMaxHoldingPeriod(),
            settings.getPriceWindowMonths()));
    if (loadError != null) {
      presenter.prepareFailView(loadError);
    }
  }

  /**
   * Adds a number of trading days (weekdays) to a date. Market holidays are not accounted for.
   *
   * @param start The starting date
   * @param tradingDays Number of trading days to add
   * @return The date the given number of trading days after the start date
   */
  private static LocalDate addTradingDays(LocalDate start, int tradingDays) {
    LocalDate date = start;
    int added = 0;
    while (added < tradingDays) {
      date = date.plusDays(1);
      DayOfWeek day = date.getDayOfWeek();
      if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
        added++;
      }
    }
    return date;
  }
}
