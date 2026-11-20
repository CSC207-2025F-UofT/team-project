package use_case.cancel_timer;

/**
 * The Cancel Timer Interactor.
 */
public class CancelTimerInteractor implements CancelTimerInputBoundary {
    private final CancelTimerOutputBoundary presenter;
    private final CancelTimerDataAccessInterface dataAccess;

    public CancelTimerInteractor(CancelTimerOutputBoundary presenter,
                                 CancelTimerDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    /**
     * Executes the Cancel Timer Use Case.
     */
    @Override
    public void execute() {
        dataAccess.setTimerRunning(false);

        presenter.prepareSuccessView();
    }
}
