package use_case.ai_insights;

import entity.SpendingSummary;
import entity.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrendAnalyzerTest {

    @Test
    void analyzeComputesTotalsAndHighestCategory() {
        TrendAnalyzer analyzer = new TrendAnalyzer();

        List<Transaction> txs = List.of(
                new Transaction(LocalDate.of(2025, 11, 1), "FOOD", 10.0, "A"),
                new Transaction(LocalDate.of(2025, 11, 2), "FOOD", 20.0, "B"),
                new Transaction(LocalDate.of(2025, 11, 3), "SHOPPING", 50.0, "C")
        );

        SpendingSummary summary = analyzer.analyze(txs);

        assertEquals(80.0, summary.totalSpent(), 0.0001);
        assertEquals(2, summary.totals().size());
        assertEquals(30.0, summary.totals().get("FOOD"), 0.0001);
        assertEquals(50.0, summary.totals().get("SHOPPING"), 0.0001);
        assertEquals("SHOPPING", summary.highestCategory());
    }

    @Test
    void analyzeHandlesEmptyList() {
        TrendAnalyzer analyzer = new TrendAnalyzer();
        SpendingSummary summary = analyzer.analyze(List.of());

        assertEquals(0.0, summary.totalSpent(), 0.0001);
        assertTrue(summary.totals().isEmpty());
        assertEquals("Unknown", summary.highestCategory());
    }

    @Test
    void buildPromptHandlesEmptyTotals() {
        SpendingSummary summary = new SpendingSummary(
                0.0,
                Map.of(),
                "Unknown"
        );

        String prompt = InsightPromptBuilder.buildPrompt(summary);

        assertTrue(prompt.contains("Total spent: $0.00") || prompt.contains("Total spent: $0"));
        assertTrue(prompt.contains("Unknown") || prompt.contains("No category"));
    }

}
