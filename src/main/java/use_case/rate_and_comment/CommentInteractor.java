package use_case.rate_and_comment;

/**
 * The rate and comment Interactor.
 */
public class CommentInteractor implements CommentInputBoundary{
    private final CommentUserDataAccessInterface userDataAccessObject;
    private final CommentOutputBoundary commentPresenter;


    public CommentInteractor(CommentUserDataAccessInterface userDataAccessObject, CommentOutputBoundary commentPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.commentPresenter = commentPresenter;
    }

    @Override
    public void execute(CommentInputData commentInputData) {
        final String username = commentInputData.getUsername();
        final String medianame = commentInputData.getMedianame();
        final String comment = commentInputData.getComment();
        final int rate = commentInputData.getRate();

        //TODO 完成存储数据部分
        //userDataAccessObject.execute();

        final CommentOutputData commentOutputData = new CommentOutputData(medianame);
        commentPresenter.prepareSuccessView(commentOutputData);
    }
}
