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
        CreatePlanState state = new CreatePlanState();
        state.setPlanId(outputData.getPlanId());
        state.setDate(outputData.getDate());

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        CreatePlanState state = new CreatePlanState();
        state.setErrorMessage(errorMessage);

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}