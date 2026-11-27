package use_case.create_project;
import entity.Project;

public interface CreateProjectDataAccessInterface {
    long generateProjectID();
    void setCurrentProject(Project project);
}
