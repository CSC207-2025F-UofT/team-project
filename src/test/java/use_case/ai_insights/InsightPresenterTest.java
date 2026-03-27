package use_case.ai_insights;

import entity.Insight;
import entity.SpendingSummary;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InsightPresenterTest {

    @Test
    void presentSummarySetsViewModelBreakdownCorrectly() {
        InsightViewModel vm = new InsightViewModel();
        InsightPresenter presenter = new InsightPresenter(vm);

        SpendingSummary summary = new SpendingSummary(
                100.0,
                Map.of("Food", 60.0, "Shopping", 40.0),
                "Food"
        );

        presenter.presentSummary(summary);

        String breakdown = vm.getSpendingBreakdown();
        assertNotNull(breakdown);
        assertTrue(breakdown.startsWith("Spending Breakdown:"));

        // We don't care about exact order or decimals, just that entries exist.
        assertTrue(breakdown.contains("Food: $60"));
        assertTrue(breakdown.contains("Shopping: $40"));
    }


    @Test
    void presentHandlesNullRecommendations() {
        InsightViewModel vm = new InsightViewModel();
        InsightPresenter presenter = new InsightPresenter(vm);

        Insight insight = new Insight();
        insight.setSummaryText("Test summary");
        insight.setRecommendations(null);
        insight.setGeneratedAt(java.time.LocalDate.of(2025, 1, 1));

        presenter.present(insight, null);

        assertEquals("Test summary", vm.getSummary());
        assertEquals("No recommendations available.", vm.getRecommendations());
        assertTrue(vm.getDate().contains("Generated: 2025-01-01"));
    }

    @Test
    void presentFormatsRecommendationsCorrectly() {
        InsightViewModel vm = new InsightViewModel();
        InsightPresenter presenter = new InsightPresenter(vm);

        Insight insight = new Insight();
        insight.setSummaryText("Summary");
        insight.setRecommendations(java.util.List.of("Save more", "Track spending"));
        insight.setGeneratedAt(java.time.LocalDate.now());

        presenter.present(insight, null);

        assertEquals("• Save more\n• Track spending", vm.getRecommendations());
    }


}
