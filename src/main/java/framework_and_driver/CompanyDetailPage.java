package framework_and_driver;

import entity.CompanyDetailViewModel;

public class CompanyDetailPage {


    /**
     * 【Presenter call】receive ViewModel and update UI。
     */
    public void updateCompanyDetails(CompanyDetailViewModel viewModel) {
        System.out.println("CompanyDetailPage: update company details UI。");
        System.out.println("company name: " + viewModel.getName());
        System.out.println("company market format: " + viewModel.getMarketCapFormatted());
        System.out.println("latest revenue: " + viewModel.getLatestRevenue());
        System.out.println("recent news quantities: " + viewModel.getRecentNews().size());
    }

    /**
     * 【Presenter call】show error message。
     */
    public void displayError(String message) {
        // TODO: appear error message on the UI
        System.err.println("CompanyDetailPage ERROR: " + message);
    }
}
