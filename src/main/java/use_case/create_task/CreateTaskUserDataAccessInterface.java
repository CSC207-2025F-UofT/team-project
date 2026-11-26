package use_case.create_task;

import entity.StudentTask;

public interface CreateTaskUserDataAccessInterface {
    boolean existsByUserId(int userId);
    int generateNextTaskId();
    void saveTask(int userId, StudentTask task);
}