package useCases;

import entity.Project;
import entity.ProjectFactory;

public class ProjectInteractor {
    private final ProjectDataAccessInterface projectInterface;
    private final ProjectFactory factory;

    public ProjectInteractor(ProjectDataAccessInterface projectInterface, ProjectFactory factory) {
        this.projectInterface = projectInterface;
        this.factory = factory;
    }

    public Project makeProject(String name) {
        Project project = factory.makeProject(projectInterface.getId(), name);
        projectInterface.writeIdToData(project.getId());
        return project;
    }
}
