package use_case.create_project;

import entity.Project;

public class CreateProjectInteractor implements CreateProjectInputBoundary {
    private final CreateProjectDataAccessInterface dataAccess;
    private final CreateProjectOutputBoundary presenter;

    public CreateProjectInteractor(CreateProjectDataAccessInterface dataAccess, CreateProjectOutputBoundary presenter){
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    public void execute(CreateProjectInputData inputData) {
        int width = inputData.getWidth();
        int height = inputData.getHeight();
        if (width <= 0 || height <= 0) {
            presenter.prepareFailView("Canvas's dimensions must be positive.");
            return;
        }

        String localName = inputData.getProjectName();
        if (localName == null || localName.isBlank()) {
            presenter.prepareFailView("Project's name can not be blank. ");
            return;
        }

        long projectID = dataAccess.generateProjectID();
        Project project = new Project(projectID, localName);
        project.getCanvas().resize(width, height);

        dataAccess.setCurrentProject(project);

        CreateProjectOutputData outputData = new CreateProjectOutputData(
                project.getId(),
                project.getName(),
                project.getCanvas().getWidth(),
                project.getCanvas().getHeight()
        );

        presenter.prepareSuccessView(outputData);
    }
}
