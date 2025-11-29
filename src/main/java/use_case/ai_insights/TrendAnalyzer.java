package use_case.ai_insights;

import entity.SpendingSummary;
import entity.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrendAnalyzer {

    public SpendingSummary analyze(List<Transaction> transactions) {
        Map<String, Double> totals = new HashMap<>();

        double totalSpent = 0;

        for (Transaction t: transactions) {
            totals.put(t.getCategory(),
                    totals.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            totalSpent += t.getAmount();
        }

        String highestCategory = totals.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");


        return new SpendingSummary(totalSpent, totals, highestCategory);
    }
}
