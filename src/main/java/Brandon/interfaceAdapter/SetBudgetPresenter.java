package Brandon.interfaceAdapter;

import Brandon.useCase.SetBudgetOutputBoundary;
import Brandon.useCase.SetBudgetOutputData;

public class SetBudgetPresenter implements SetBudgetOutputBoundary {

    private final SetBudgetViewModel viewModel;

    public SetBudgetPresenter(SetBudgetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SetBudgetOutputData outputData) {
        viewModel.setMonth(outputData.getMonth());
        viewModel.setLimit(outputData.getLimit());
        viewModel.setSuccess(outputData.getSuccess());
        viewModel.setMessage(outputData.getMessage());
    }
}
