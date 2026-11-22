package dataaccess;

import entities.DailyAggregate;
import entities.Stock;
import io.polygon.kotlin.sdk.DefaultJvmHttpClientProvider;
import io.polygon.kotlin.sdk.rest.AggregateDTO;
import io.polygon.kotlin.sdk.rest.AggregatesDTO;
import io.polygon.kotlin.sdk.rest.AggregatesParametersBuilder;
import io.polygon.kotlin.sdk.rest.PolygonRestClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import usecase.setriskprofile.SetRiskProfileStockDataAccessInterface;

/**
 * Implementation of the Data Access Object (DAO) for accessing stock data that uses the Massive.com
 * API.
 */
public class ApiStockDataAccessObject implements SetRiskProfileStockDataAccessInterface {
  // TODO: After we add data access interfaces to our use cases, make this class implement them.
  private final PolygonRestClient client;

  /**
   * Constructs a new data access object.
   *
   * @param apiKey Massive.com API key
   */
  public ApiStockDataAccessObject(String apiKey) {
    if ("".equals(apiKey)) {
      throw new IllegalArgumentException("API key cannot be empty");
    }

    client = new PolygonRestClient(apiKey, new DefaultJvmHttpClientProvider(), "api.massive.com");
  }

  private <T extends Number> T getNullableSafe(T x, T fallback) {
    return Optional.ofNullable(x).orElse(fallback);
  }

  /**
   * Accesses the daily aggregates of a stock across a specified time period. In each aggregate
   * object returned in the list, if a data point is not returned by the API, it is set to zero.
   *
   * @param stock The stock to look up data for
   * @param from Start day of the time period
   * @param to End day of the time period
   * @return A list of aggregate objects for each day in the specified range
   */
  @Override
  public ArrayList<DailyAggregate> getDailyAggregates(Stock stock, LocalDate from, LocalDate to) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    AggregatesDTO aggs = client.getAggregatesBlocking(
            new AggregatesParametersBuilder()
                    .ticker(stock.getTicker())
                    .timespan("day")
                    .fromDate(from.format(formatter))
                    .toDate(to.format(formatter))
                    .build()
    );

    ArrayList<DailyAggregate> result = new ArrayList<>();
    for (AggregateDTO dto : aggs.getResults()) {
      DailyAggregate aggregate = new DailyAggregate(
              getNullableSafe(dto.getOpen(), 0.0),
              getNullableSafe(dto.getClose(), 0.0),
              getNullableSafe(dto.getHigh(), 0.0),
              getNullableSafe(dto.getLow(), 0.0),
              getNullableSafe(dto.getVolume(), 0.0),
              getNullableSafe(dto.getVolumeWeightedAveragePrice(), 0.0),
              Instant.ofEpochMilli(getNullableSafe(dto.getTimestampMillis(), 0L))
      );
      result.add(aggregate);
    }
    return result;
  }
}
