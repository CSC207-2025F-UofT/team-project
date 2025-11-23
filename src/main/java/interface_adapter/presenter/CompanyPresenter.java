package interface_adapter.presenter;

import use_case.company.CompanyOutputBoundary;
import entity.Company;
import interface_adapter.view_model.CompanyViewModel;

public class CompanyPresenter implements CompanyOutputBoundary {

    private final CompanyViewModel vm;
    private final framework_and_driver.CompanyPage ui;

    public CompanyPresenter(CompanyViewModel vm,
                                   framework_and_driver.CompanyPage ui) {
        this.vm = vm;
        this.ui = ui;
    }

    @Override
    public void presentCompany(Company overview) {
        vm.error = null;
        vm.name = overview.getName();
        vm.symbol = overview.getSymbol();
        vm.sector = overview.getSector();
        vm.industry = overview.getIndustry();
        vm.description = overview.getDescription();

        ui.refresh();
    }

    @Override
    public void presentError(String message) {
        vm.error = message;
        ui.refresh();
    }
}

