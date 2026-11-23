package dataaccess;

import entities.DailyAggregate;
import entities.Stock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import usecase.estimateroi.EstimateROIDataAccessInterface;

/** Supplies closing prices for the Estimate ROI use case. */
public class EstimateROIDataAccessObject implements EstimateROIDataAccessInterface {
    private final ApiStockDataAccessObject api;

    public EstimateROIDataAccessObject(ApiStockDataAccessObject api) {
        this.api = api;
    }

    @Override
    public List<Double> getHistoricalPrices(String ticker, int historyLength) {
        LocalDate to = LocalDate.now().minusDays(1);
        LocalDate from = to.minusYears(historyLength);
        List<DailyAggregate> days = new ArrayList<>(api.getDailyAggregates(new Stock(ticker), from, to));
        days.sort(Comparator.comparing(DailyAggregate::getTime));   // oldest → newest

        // Not enough history: first data point is well after the requested start
        if (days.isEmpty()
                || days.get(0).getTime().isAfter(from.plusDays(14).atStartOfDay().toInstant(java.time.ZoneOffset.UTC))) {
            return List.of();
        }
        List<Double> closes = new ArrayList<>();
        for (DailyAggregate day : days) {
            closes.add(day.getClose());
        }
        return closes;
    }
}