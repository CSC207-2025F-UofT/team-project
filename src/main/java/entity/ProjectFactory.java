package entity;

public class ProjectFactory {
    public ProjectFactory() {}

    public Project makeProject(long id, String name) {
        return new Project(id, name);
    }
}
