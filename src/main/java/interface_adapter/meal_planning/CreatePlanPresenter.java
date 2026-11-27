package interface_adapter.meal_planning;

import use_case.meal_planning.CreatePlanOutputBoundary;
import use_case.meal_planning.CreatePlanOutputData;

import java.util.Objects;

public class CreatePlanPresenter implements CreatePlanOutputBoundary {

    private final CreatePlanViewModel viewModel;

    public CreatePlanPresenter(CreatePlanViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(viewModel);
    }

    @Override
    public void prepareSuccessView(CreatePlanOutputData outputData) {
        viewModel.update(
                outputData.getPlanId(),
                outputData.getDate(),
                "");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.update(null, null, errorMessage);
    }
}