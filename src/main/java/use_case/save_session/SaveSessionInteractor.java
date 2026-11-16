package use_case.save_session;

import entities.Session;
import entities.TimerSessionFactory;

/**
 * The Save Session Interactor.
 */
public class SaveSessionInteractor implements SaveSessionInputBoundary {

    private SaveSessionDataAccessInterface dataAccessObject;
    private SaveSessionOutputBoundary presenter;
    private TimerSessionFactory sessionFactory;

    public SaveSessionInteractor(SaveSessionOutputBoundary presenter,
                                 SaveSessionDataAccessInterface dataAccessObject,
                                 TimerSessionFactory sessionFactory) {
        this.presenter = presenter;
        this.dataAccessObject = dataAccessObject;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Executes the Save Session use case.
     * Validates the provided focus and break durations.
     * @param inputData the input data containing the length of focus and break durations
     */
    @Override
    public void execute(SaveSessionInputData inputData) {
        if (inputData.getFocusDuration() <= 0 || inputData.getBreakDuration() < 0) {
            presenter.prepareFailView("Focus duration must be greater than 0.");
        } else {
            Session session = sessionFactory.create(inputData.getFocusDuration(), inputData.getBreakDuration());

            dataAccessObject.save(session);

            SaveSessionOutputData outputData = new SaveSessionOutputData(inputData.getFocusDuration(), inputData.getBreakDuration());
            presenter.prepareSuccessView(outputData);
        }
    }
}
