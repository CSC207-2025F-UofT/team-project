package interface_adapter;

import use_case.CompanyDetailOutputBoundary;
import entity.*;
import framework_and_driver.CompanyDetailPage;
import java.util.List;

public class CompanyDetailPresenter implements CompanyDetailOutputBoundary {

    private final CompanyDetailPage view;

    public CompanyDetailPresenter(CompanyDetailPage view) {
        this.view = view;
    }

    @Override
    public void presentCompanyDetail(Company companyOverview, FinancialStatement financials, List<NewsArticle> news) {

        CompanyDetailViewModel viewModel = new CompanyDetailViewModel(companyOverview, financials, news);

        view.updateCompanyDetails(viewModel);
    }

    @Override
    public void presentError(String message) {
        view.displayError(message);
    }
}
