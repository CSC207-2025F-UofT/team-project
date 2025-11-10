package plan4life.use_case.lock_activity;

public interface LockActivityOutputBoundary {
    void execute(LockActivityResponseModel responseModel);

    void present(LockActivityResponseModel lockActivityResponseModel);
}
