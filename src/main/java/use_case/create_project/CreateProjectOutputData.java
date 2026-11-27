package use_case.create_project;

public class CreateProjectOutputData {
    private final long projectID;
    private final String projectName;
    private final int width;
    private final int height;

    public CreateProjectOutputData(long projectID, String projectName, int width, int height){
        this.projectID = projectID;
        this.projectName = projectName;
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String getProjectName(){
        return projectName;
    }

    public long getProjectID() {
        return projectID;
    }
}
