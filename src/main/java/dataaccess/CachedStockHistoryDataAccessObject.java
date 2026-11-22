package dataaccess;

import entities.DailyAggregate;
import entities.Stock;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import usecase.viewstockinfo.ViewStockInfoStockDataAccessInterface;

/** Uses the shared market data API and stores the last successful history for each ticker. */
public class CachedStockHistoryDataAccessObject implements ViewStockInfoStockDataAccessInterface {
  private final ApiStockDataAccessObject api;
  private final Path cacheDirectory;

  public CachedStockHistoryDataAccessObject(ApiStockDataAccessObject api) {
    this(api, AppConfigDirectory.getPath().resolve("stock-history"));
  }

  CachedStockHistoryDataAccessObject(ApiStockDataAccessObject api, Path cacheDirectory) {
    this.api = api;
    this.cacheDirectory = cacheDirectory;
  }

  @Override
  public List<DailyAggregate> getDailyAggregates(Stock stock, LocalDate from, LocalDate to) {
    return api.getDailyAggregates(stock, from, to);
  }

  @Override
  public Optional<CachedHistory> getCachedDailyAggregates(Stock stock) throws IOException {
    try {
      JSONObject snapshot = new JSONObject(Files.readString(cacheFile(stock)));
      if (!stock.getTicker().equals(snapshot.getString("ticker"))) {
        throw new IOException("Cached ticker does not match the request");
      }

      LocalDate endDate = LocalDate.parse(snapshot.getString("endDate"));
      JSONArray days = snapshot.getJSONArray("days");
      List<DailyAggregate> history = new ArrayList<>();
      for (int i = 0; i < days.length(); i++) {
        JSONObject day = days.getJSONObject(i);
        history.add(new DailyAggregate(day.getDouble("open"), day.getDouble("close"),
                day.getDouble("high"), day.getDouble("low"), day.getDouble("volume"),
                day.getDouble("vwap"), Instant.parse(day.getString("time"))));
      }
      return Optional.of(new CachedHistory(endDate, history));
    } catch (NoSuchFileException e) {
      return Optional.empty();
    } catch (JSONException | DateTimeException e) {
      throw new IOException("Invalid stock history cache", e);
    }
  }

  @Override
  public void saveCachedDailyAggregates(Stock stock, CachedHistory history) throws IOException {
    JSONArray days = new JSONArray();
    for (DailyAggregate day : history.getDailyAggregates()) {
      days.put(new JSONObject()
              .put("open", day.getOpen())
              .put("close", day.getClose())
              .put("high", day.getHigh())
              .put("low", day.getLow())
              .put("volume", day.getVolume())
              .put("vwap", day.getVolumeWeightedAveragePrice())
              .put("time", day.getTime().toString()));
    }
    JSONObject snapshot = new JSONObject()
            .put("ticker", stock.getTicker())
            .put("endDate", history.getEndDate().toString())
            .put("days", days);

    Files.createDirectories(cacheDirectory);
    Path temporary = Files.createTempFile(cacheDirectory, "stock-", ".tmp");
    Path destination = cacheFile(stock);
    try {
      Files.writeString(temporary, snapshot.toString());
      try {
        Files.move(temporary, destination,
                StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (AtomicMoveNotSupportedException e) {
        Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
      }
    } finally {
      Files.deleteIfExists(temporary);
    }
  }

  private Path cacheFile(Stock stock) {
    String encodedTicker = URLEncoder.encode(stock.getTicker(), StandardCharsets.UTF_8);
    return cacheDirectory.resolve("stock-" + encodedTicker + ".json");
  }
}
