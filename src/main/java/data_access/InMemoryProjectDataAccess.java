package data_access;

import entity.Project;
import use_case.create_project.CreateProjectDataAccessInterface;
public class InMemoryProjectDataAccess implements CreateProjectDataAccessInterface {
    private long nextProjectID = 1;
    private Project currentProject;

    @Override
    public long generateProjectID() {
        return nextProjectID++;
    }

    @Override
    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public Project getCurrentProject(){
        return currentProject;
    }
}

