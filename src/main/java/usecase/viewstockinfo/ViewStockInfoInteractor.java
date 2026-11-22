package usecase.viewstockinfo;

import entities.DailyAggregate;
import entities.Stock;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import usecase.viewstockinfo.ViewStockInfoStockDataAccessInterface.CachedHistory;

/**
 * The View Stock Info Interactor.
 */
public class ViewStockInfoInteractor implements ViewStockInfoInputBoundary {
  private static final int HISTORY_YEARS = 1;
  private static final ZoneId MARKET_TIME_ZONE = ZoneId.of("America/New_York");
  private static final Logger LOGGER = Logger.getLogger(ViewStockInfoInteractor.class.getName());

  private final ViewStockInfoStockDataAccessInterface stockDataAccessObject;
  private final ViewStockInfoWatchlistDataAccessInterface watchlistDataAccessObject;
  private final ViewStockInfoOutputBoundary presenter;
  private final Clock clock;

  /**
   * Creates a new ViewStockInfoInteractor.
   *
   * @param stockDataAccessInterface Data access object for fresh and cached stock data
   * @param watchlistDataAccessInterface Data access object for the saved watchlist
   * @param outputBoundary The presenter
   */
  public ViewStockInfoInteractor(
          ViewStockInfoStockDataAccessInterface stockDataAccessInterface,
          ViewStockInfoWatchlistDataAccessInterface watchlistDataAccessInterface,
          ViewStockInfoOutputBoundary outputBoundary) {
    this(stockDataAccessInterface, watchlistDataAccessInterface, outputBoundary, Clock.systemUTC());
  }

  ViewStockInfoInteractor(ViewStockInfoStockDataAccessInterface stockDataAccessInterface,
                         ViewStockInfoWatchlistDataAccessInterface watchlistDataAccessInterface,
                         ViewStockInfoOutputBoundary outputBoundary, Clock clock) {
    this.stockDataAccessObject = stockDataAccessInterface;
    this.watchlistDataAccessObject = watchlistDataAccessInterface;
    this.presenter = outputBoundary;
    this.clock = clock.withZone(MARKET_TIME_ZONE);
  }

  @Override
  public void loadWatchlist() {
    try {
      presenter.prepareWatchlistView(getWatchlistTickers());
    } catch (IOException e) {
      presenter.prepareWatchlistFailView("Could not load your watchlist. Please try again.");
    }
  }

  @Override
  public void execute(ViewStockInfoInputData inputData) {
    String ticker = inputData.getTicker();
    if (ticker == null || ticker.isBlank()) {
      presenter.prepareFailView("Select a stock from your watchlist.");
      return;
    }

    Stock stock = new Stock(normalizeTicker(ticker));
    try {
      if (!getWatchlistTickers().contains(stock.getTicker())) {
        presenter.prepareFailView(stock.getTicker() + " is not in your watchlist.");
        return;
      }
    } catch (IOException e) {
      presenter.prepareFailView("Could not load your watchlist. Please try again.");
      return;
    }

    // Use the last fully elapsed market date so today's aggregate cannot be partial.
    LocalDate to = LocalDate.now(clock).minusDays(1);
    List<DailyAggregate> dailyAggregates;
    try {
      dailyAggregates = stockDataAccessObject.getDailyAggregates(stock, startDate(to), to);
    } catch (RuntimeException e) {
      presentCachedHistory(stock);
      return;
    }

    ViewStockInfoOutputData result = buildOutput(stock, to, dailyAggregates, false);
    if (result.getDailyAggregates().isEmpty()) {
      presentCachedHistory(stock);
      return;
    }

    try {
      stockDataAccessObject.saveCachedDailyAggregates(stock,
              new CachedHistory(to, result.getDailyAggregates()));
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not save stock history to the cache", e);
    }
    presenter.prepareSuccessView(result);
  }

  private void presentCachedHistory(Stock stock) {
    try {
      CachedHistory cached = stockDataAccessObject.getCachedDailyAggregates(stock).orElse(null);
      if (cached != null) {
        ViewStockInfoOutputData result = buildOutput(stock, cached.getEndDate(),
                cached.getDailyAggregates(), true);
        if (!result.getDailyAggregates().isEmpty()) {
          presenter.prepareSuccessView(result);
          return;
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Could not read stock history from the cache", e);
    }
    presenter.prepareFailView("Could not load end-of-day data for " + stock.getTicker()
            + ". Please try again.");
  }

  private static ViewStockInfoOutputData buildOutput(Stock stock, LocalDate to,
                                                      List<DailyAggregate> dailyAggregates,
                                                      boolean fromCache) {
    LocalDate from = startDate(to);
    List<DailyAggregate> rows = dailyAggregates.stream()
            .filter(day -> {
              LocalDate date = marketDate(day);
              return !date.isBefore(from) && !date.isAfter(to);
            })
            .sorted(Comparator.comparing(DailyAggregate::getTime))
            .collect(Collectors.toList());
    List<PricePoint> prices = rows.stream()
            .map(day -> new PricePoint(marketDate(day), day.getClose()))
            .collect(Collectors.toList());

    Double dailyChange = null;
    Double dailyChangePercent = null;
    if (prices.size() >= 2) {
      double latestClose = prices.get(prices.size() - 1).getClose();
      double previousClose = prices.get(prices.size() - 2).getClose();
      dailyChange = latestClose - previousClose;
      if (previousClose != 0) {
        dailyChangePercent = dailyChange / previousClose * 100;
      }
    }
    return new ViewStockInfoOutputData(stock.getTicker(), from, to, rows, prices,
            dailyChange, dailyChangePercent, fromCache);
  }

  private List<String> getWatchlistTickers() throws IOException {
    return watchlistDataAccessObject.getWatchlistStocks().stream()
            .map(Stock::getTicker)
            .map(ViewStockInfoInteractor::normalizeTicker)
            .filter(ticker -> !ticker.isEmpty())
            .distinct()
            .collect(Collectors.toList());
  }

  private static String normalizeTicker(String ticker) {
    return ticker.strip().toUpperCase(Locale.ROOT);
  }

  private static LocalDate startDate(LocalDate to) {
    return to.minusYears(HISTORY_YEARS).plusDays(1);
  }

  private static LocalDate marketDate(DailyAggregate day) {
    return day.getTime().atZone(MARKET_TIME_ZONE).toLocalDate();
  }
}
