package entity;

import java.time.LocalDate;

public class StudentTaskFactory extends TaskFactory {
    private final int uniqueUserId;

    public StudentTaskFactory(int nextTaskUniqueId, int uniqueUserId) {
        super(nextTaskUniqueId);
        this.uniqueUserId = uniqueUserId;
    }

    public StudentTask createStudentTask(String title,
                                         String description,
                                         LocalDate deadline,
                                         double estimatedHoursToCompleteTask,
                                         byte completionProgress,
                                         DifficultyLevel difficultyLevel) {
        int taskId = generateNextTaskUniqueId();

        return new StudentTask(taskId, uniqueUserId, title, description,
                deadline, estimatedHoursToCompleteTask,
                completionProgress, difficultyLevel);
    }
}