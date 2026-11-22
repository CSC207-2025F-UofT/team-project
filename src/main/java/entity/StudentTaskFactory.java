package entity;

import java.time.LocalDate;

public class StudentTaskFactory extends TaskFactory{
    public StudentTaskFactory(int nextUniqueId){
        super(nextUniqueId);
    }

    @Override
    public StudentTask createTask(int uniqueUserId, String title, String description, LocalDate deadline, double estimatedHoursToCompleteTask, int completionProgress, byte difficultyLevel) {
        if (!StudentTask.inputValidation(
                uniqueUserId, deadline,
                estimatedHoursToCompleteTask, completionProgress)) {
            throw new IllegalArgumentException("Invalid task input.");
        }

        int taskId = generateTaskId();

        return new StudentTask(taskId, uniqueUserId, title, description, deadline, estimatedHoursToCompleteTask, (byte) completionProgress, difficultyLevel);
    }
}
