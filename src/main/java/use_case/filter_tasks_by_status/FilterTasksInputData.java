package use_case.filter_tasks_by_status;

import entity.Task;
import java.util.List;

public class FilterTasksInputData {
    private final String username;
    private final String boardId;
    private final Task.TaskStatus status;
    private final List<String> members;

    public FilterTasksInputData(String username, String boardId, Task.TaskStatus status, List<String> members) {
        this.username = username;
        this.boardId = boardId;
        this.status = status;
        this.members = members;
    }

    public String getUsername() { return username; }
    public String getBoardId() { return boardId; }
    public Task.TaskStatus getStatus() { return status; }
    public List<String> getMembers() { return members; }
}
