package interface_adapter.create_project;

import use_case.create_project.CreateProjectOutputBoundary;
import use_case.create_project.CreateProjectOutputData;

public class CreateProjectPresenter
        implements CreateProjectOutputBoundary {

    private final CreateProjectViewModel viewModel;

    public CreateProjectPresenter(
            CreateProjectViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(
            CreateProjectOutputData outputData) {
        viewModel.setSuccess(
                outputData.getProjectName(),
                outputData.getWidth(),
                outputData.getHeight()
        );
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setError(errorMessage);
    }
}