package use_case.ai_insights;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import entity.Insight;
import entity.SpendingSummary;

class InsightControllerTest {

    static class FakeInsightService extends InsightService {

        public SpendingSummary receivedSummary;

        public FakeInsightService() {
            super(null); // bypass real client
        }

        @Override
        public Insight generateInsights(SpendingSummary summary) {
            this.receivedSummary = summary;

            Insight insight = new Insight();
            insight.setSummaryText("Test Insight");
            return insight;
        }
    }

    static class FakePresenter extends InsightPresenter {

        public Insight receivedInsight;
        public SpendingSummary receivedSummary;

        public FakePresenter(InsightViewModel vm) {
            super(vm);
        }

        @Override
        public void present(Insight insight, SpendingSummary summary) {
            this.receivedInsight = insight;
            this.receivedSummary = summary;
        }
    }

    @Test
    void controllerDelegatesToServiceAndPresenter() {
        SpendingSummary summary = new SpendingSummary(
                100,
                Map.of("Food", 100.0),
                "Food"
        );

        FakeInsightService fakeService = new FakeInsightService();
        FakePresenter fakePresenter = new FakePresenter(new InsightViewModel());

        InsightsController controller = new InsightsController(fakeService, fakePresenter);
        controller.generateInsight(summary);

        // Assertions
        assertEquals(summary, fakeService.receivedSummary);

        assertNotNull(fakePresenter.receivedInsight);
        assertEquals(summary, fakePresenter.receivedSummary);
    }
}
