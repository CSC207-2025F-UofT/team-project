package controller;

import entity.Insight;
import entity.SpendingSummary;
import use_case.ai_insights.InsightService;
import use_case.ai_insights.presenter.InsightPresenter;

public class InsightsController {
    private final InsightService interactor;
    private final InsightPresenter presenter;

    public InsightsController(InsightService interactor, InsightPresenter presenter) {
        this.interactor = interactor;
        this.presenter = presenter;
    }

    public void generateInsight(SpendingSummary summary, String userId) {
        Insight insight = interactor.generateInsights(summary, userId);

        presenter.present(insight, summary);
    }

}
