package interface_adapter.create_plan;

import interface_adapter.plan_editor.PlanEditorState;
import interface_adapter.plan_editor.PlanEditorViewModel;
import use_case.create_plan.CreatePlanOutputBoundary;
import use_case.create_plan.CreatePlanOutputData;

public class CreatePlanPresenter implements CreatePlanOutputBoundary {
    private final PlanEditorViewModel viewModel;

    public CreatePlanPresenter(PlanEditorViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CreatePlanOutputData outputData) {
        viewModel.setState(new PlanEditorState());
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.getState().setError(errorMessage);
        viewModel.firePropertyChanged();
    }
}
