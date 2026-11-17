package use_case.manage_tasks;

import java.util.List;

public class ManageTasksOutputData {
    private final List<TaskData> tasksList;
    private final int totalMinutes;

    public ManageTasksOutputData(List<TaskData> tasksList, int totalMinutes) {
        this.tasksList = List.copyOf(tasksList);
        this.totalMinutes = totalMinutes;
    }

    // getters
    public List<TaskData> getTasksList() {
        return tasksList;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }
}
