package entity;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Task {
    private final int uniqueTaskId;
    private final int uniqueUserId;

    public Task(int uniqueTaskId, int uniqueUserId) {
        this.uniqueTaskId = uniqueTaskId;
        this.uniqueUserId = uniqueUserId;
    }

}
