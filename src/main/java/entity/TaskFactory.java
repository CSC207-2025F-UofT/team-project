package entity;

import java.time.LocalDate;

public abstract class TaskFactory {
    private int nextUniqueId;

    protected TaskFactory(int nextUniqueId){
        this.nextUniqueId = nextUniqueId;
    }

    protected int generateTaskId(){
        return nextUniqueId ++;
    }

    public abstract Task createTask(int uniqueUserID, String title, String description, LocalDate deadline, double estimatedHoursToCompleteTask, int completionProgress, byte difficultyLevel);
}
