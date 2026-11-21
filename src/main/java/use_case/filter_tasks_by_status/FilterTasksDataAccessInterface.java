package use_case.filter_tasks_by_status;
import entity.Board;
public interface FilterTasksDataAccessInterface {
    Board getBoard(String boardId);
}
