package interface_adapter.tasks.dto;

public class TaskDTO {

    private final String taskName;
    private final boolean completed;

    public TaskDTO(String taskName, boolean completed) {
        this.taskName = taskName;
        this.completed = completed;
    }

    public String getTaskName() {
        return taskName;
    }

    public boolean isCompleted() {
        return completed;
    }
}
