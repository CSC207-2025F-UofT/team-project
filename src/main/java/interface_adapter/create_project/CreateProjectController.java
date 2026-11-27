package interface_adapter.create_project;
import use_case.create_project.CreateProjectInputBoundary;
import use_case.create_project.CreateProjectInputData;

public class CreateProjectController {
    private final CreateProjectInputBoundary interactor;

    public CreateProjectController(CreateProjectInputBoundary interactor){
        this.interactor = interactor;
    }

    public void execute(String projectName, int width, int height){
        CreateProjectInputData inputData = new CreateProjectInputData(projectName, width, height);
        interactor.execute(inputData);
    }

}
