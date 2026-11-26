package entity;

public abstract class TaskFactory {
    private int nextTaskUniqueId;

    protected TaskFactory(int nextTaskUniqueId) {
        this.nextTaskUniqueId = nextTaskUniqueId;
    }

    protected int generateNextTaskUniqueId() {
        return nextTaskUniqueId++;
    }
}