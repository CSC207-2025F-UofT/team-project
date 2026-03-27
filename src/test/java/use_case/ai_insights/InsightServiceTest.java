package use_case.ai_insights;

import entity.Insight;
import entity.SpendingSummary;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InsightServiceTest {

    static class StubInsightClient extends InsightClient {
        private final String response;

        StubInsightClient(String response) {
            this.response = response;
        }

        @Override
        public String generateInsight(String prompt) {
            // we ignore the prompt, just return stubbed Gemini-like response
            JSONObject inner = new JSONObject()
                    .put("summary", "You spent a lot on food.")
                    .put("tips", List.of("Cook at home more", "Track grocery deals"));

            JSONObject wrapped = new JSONObject()
                    .put("candidates", List.of(
                            new JSONObject()
                                    .put("content", new JSONObject()
                                            .put("parts", List.of(
                                                    new JSONObject().put("text", inner.toString())
                                            ))
                                    )
                    ));

            return wrapped.toString();
        }
    }

    @Test
    void generateInsightsParsesSummaryAndTips() {
        SpendingSummary summary = new SpendingSummary(
                200.0,
                Map.of("Food", 150.0, "Other", 50.0),
                "Food"
        );

        InsightService service = new InsightService(new StubInsightClient("{}"));
        Insight insight = service.generateInsights(summary);

        assertNotNull(insight.getGeneratedAt());
        assertTrue(insight.getSummaryText().contains("food"));
        assertEquals(2, insight.getRecommendations().size());
    }

    @Test
    void generateInsightsHandlesZeroTotalSpend() {
        SpendingSummary summary = new SpendingSummary(
                0.0,
                Map.of(),
                "Unknown"
        );

        InsightService service = new InsightService(new StubInsightClient("{}"));
        Insight insight = service.generateInsights(summary);

        assertEquals("Not enough transaction data to generate insights.", insight.getSummaryText());
        assertEquals(1, insight.getRecommendations().size());
    }

    static class FakeFailingClient extends InsightClient {
        @Override
        public String generateInsight(String prompt) {
            return "{not valid json}";
        }
    }

    @Test
    void generateInsightsHandlesMalformedGeminiResponse() {
        InsightService service = new InsightService(new FakeFailingClient());

        SpendingSummary summary = new SpendingSummary(
                150.0,
                Map.of("Food", 150.0),
                "Food"
        );

        Insight insight = service.generateInsights(summary);

        assertEquals("Insight generation failed.", insight.getSummaryText());
        assertTrue(insight.getRecommendations().isEmpty());
    }

    static class FakeSuccessClient extends InsightClient {
        @Override
        public String generateInsight(String prompt) {
            return "{}";
        }
    }

    @Test
    void generateInsightsSetsTimestamp() {
        InsightService service = new InsightService(new FakeSuccessClient());

        SpendingSummary summary = new SpendingSummary(
                50.0,
                Map.of("Food", 50.0),
                "Food"
        );

        Insight insight = service.generateInsights(summary);

        assertNotNull(insight.getGeneratedAt());
    }


}
