package use_case.view_completion_count;

/**
 * Input data for the ViewCompletionCount use case.
 */
public class ViewCompletionCountInputData {
    private final String username;
    private final String boardId;
    public ViewCompletionCountInputData(String username, String boardId){
        this.username = username;
        this.boardId = boardId;
    }
    public String getUsername(){
        return this.username;
    }
    public String getBoardId(){
        return this.boardId;
    }
}
