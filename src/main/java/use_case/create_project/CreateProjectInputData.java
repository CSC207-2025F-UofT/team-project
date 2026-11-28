package use_case.create_project;

public class CreateProjectInputData {
    private final String projectName;
    private final int width;
    private final int height;

    public CreateProjectInputData(String projectName, int width, int height){
        this.projectName = projectName;
        this.width = width;
        this.height = height;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
