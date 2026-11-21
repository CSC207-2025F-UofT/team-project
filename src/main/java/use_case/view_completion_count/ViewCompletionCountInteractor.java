package use_case.view_completion_count;

import entity.Board;
import entity.Task;

public class ViewCompletionCountInteractor implements ViewCompletionCountInputBoundary{
    private final ViewCompletionCountDataAccessInterface dataAccess;
    private final ViewCompletionCountOutputBoundary presenter;

    public ViewCompletionCountInteractor(ViewCompletionCountDataAccessInterface dataAccess, ViewCompletionCountOutputBoundary presenter){
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewCompletionCountInputData in){
        Board board = dataAccess.getBoard(in.getBoardId());
        if (board == null){
            presenter.prepareFailView("Board not found");
            return;
        }
        int completed = 0;
        for (Task task : board.getTasks()){
            if (task.isCompletedBy(in.getUsername())){
                    completed++;
            }
        }
        int total = board.getTasks().size();
        ViewCompletionCountOutputData out = new ViewCompletionCountOutputData(total, completed);
        presenter.prepareSuccessView(out);
    }
    }


