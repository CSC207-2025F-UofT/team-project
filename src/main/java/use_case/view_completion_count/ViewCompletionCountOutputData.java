package use_case.view_completion_count;
/**
 * Output data for the ViewCompletionCount use case.
 * Contains the number of tasks completed by the user and the total tasks on the board.
 */
public class ViewCompletionCountOutputData{
    private final int total;
    private final int completed;

    public ViewCompletionCountOutputData(int total, int completed){
        this.total = total;
        this.completed = completed;
    }
    public int getTotal(){
        return this.total;
    }
    public int getCompleted(){
        return this.completed;
    }
}