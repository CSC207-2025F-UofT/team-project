package use_case.update_timer_settings;

import entities.Session;
import entities.TimerSessionFactory;

/**
 * The Update Timer Settings Interactor.
 */
public class UpdateTimerSettingsInteractor implements UpdateTimerSettingsInputBoundary {

    private UpdateTimerSettingsDataAccessInterface dataAccessObject;
    private UpdateTimerSettingsOutputBoundary presenter;
    private TimerSessionFactory sessionFactory;

    public UpdateTimerSettingsInteractor(UpdateTimerSettingsOutputBoundary presenter,
                                         UpdateTimerSettingsDataAccessInterface dataAccessObject,
                                         TimerSessionFactory sessionFactory) {
        this.presenter = presenter;
        this.dataAccessObject = dataAccessObject;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Executes the Update Timer Settings Use Case.
     * Validates the provided focus and break durations.
     * @param inputData the input data containing the length of focus and break durations
     */
    @Override
    public void execute(UpdateTimerSettingsInputData inputData) {
        if (inputData.getFocusDuration() <= 0 && inputData.getBreakDuration() <= 0) {
            presenter.prepareFailView("Focus duration and break duration must be greater than 0.");
        } else if (inputData.getFocusDuration() <= 0) {
            presenter.prepareFailView("Focus duration must be greater than 0.");
        } else if (inputData.getBreakDuration() <= 0) {
            presenter.prepareFailView("Break duration must be greater than 0.");
        } else {
            Session session = sessionFactory.create(inputData.getFocusDuration(), inputData.getBreakDuration());

            dataAccessObject.save(session);

            UpdateTimerSettingsOutputData outputData = new UpdateTimerSettingsOutputData(inputData.getFocusDuration(), inputData.getBreakDuration());
            presenter.prepareSuccessView(outputData);
        }
    }
}
