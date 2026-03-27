package use_case.ai_insights;

import entity.SpendingSummary;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InsightPromptBuilderTest {

    @Test
    void buildPromptContainsTotalsAndHighestCategory() {
        SpendingSummary summary = new SpendingSummary(
                100.0,
                Map.of(
                        "Food", 60.0,
                        "Shopping", 40.0
                ),
                "Food"
        );

        String prompt = InsightPromptBuilder.buildPrompt(summary);

        assertTrue(prompt.contains("Food: $60.0") || prompt.contains("Food: $60")); // depending on formatting
        assertTrue(prompt.contains("Shopping: $40.0") || prompt.contains("Shopping: $40"));
        assertTrue(prompt.contains("Total spent: $100.0") || prompt.contains("Total spent: $100"));
    }

    @Test
    void buildPromptUsesUnknownWhenHighestCategoryEmpty() {
        SpendingSummary summary = new SpendingSummary(
                50.0,
                Map.of("A", 50.0),
                ""
        );

        String prompt = InsightPromptBuilder.buildPrompt(summary);

        assertTrue(prompt.contains("Unknown"));
    }

    @Test
    void buildPromptHandlesNullHighestCategory() {
        SpendingSummary summary = new SpendingSummary(
                50.0,
                Map.of("Random", 50.0),
                null
        );

        String prompt = InsightPromptBuilder.buildPrompt(summary);

        assertTrue(prompt.contains("Random: $50.0"));
        assertTrue(prompt.contains("Total spent: $50.0"));
    }


}
