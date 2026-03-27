package use_case.ai_insights;

import entity.Insight;
import entity.SpendingSummary;

public class InsightsController {

    private final InsightService interactor;
    private final InsightPresenter presenter;

    public InsightsController(InsightService interactor, InsightPresenter presenter) {
        this.interactor = interactor;
        this.presenter = presenter;
    }

    public void generateInsight(SpendingSummary summary) {
        Insight insight = interactor.generateInsights(summary);

        presenter.present(insight, summary);
    }

}
