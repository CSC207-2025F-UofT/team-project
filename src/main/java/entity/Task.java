package entity;

public abstract class Task {
    private final int uniqueTaskId;
    private final int uniqueUserId;

    Task(int uniqueTaskId, int uniqueUserId) {
        this.uniqueTaskId = uniqueTaskId;
        this.uniqueUserId = uniqueUserId;
    }

    public int getUniqueTaskId() {
        return uniqueTaskId;
    }

    public int getUniqueUserId() {
        return uniqueUserId;
    }

}
