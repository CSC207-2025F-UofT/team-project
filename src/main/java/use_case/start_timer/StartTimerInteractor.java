package use_case.start_timer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The Start Timer Interactor.
 */
public class StartTimerInteractor implements StartTimerInputBoundary{
    private final StartTimerOutputBoundary presenter;
    private final StartTimerDataAccessInterface dataAccess;
    private Timer timer;

    public StartTimerInteractor(StartTimerOutputBoundary presenter,
                                StartTimerDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    /**
     * Executes the Start Timer Use Case.
     * Starts the timer and update the countdown.
     * @param inputData the input data containing the type of session to start
     */
    @Override
    public void execute(StartTimerInputData inputData) {
        int durationMinutes;

        // Get the duration of the focus session
        if(inputData.getSessionType().equals("focus")) {
            durationMinutes = dataAccess.getFocusDuration();
        } else if(inputData.getSessionType().equals("break")) {
            durationMinutes = dataAccess.getBreakDuration();
        } else {
            durationMinutes = 0;
        }

        // Notify the presenter that the timer has started
        StartTimerOutputData outputData = new StartTimerOutputData(durationMinutes, inputData.getSessionType());
        presenter.prepareSuccessView(outputData);

        // Update the stored timer in memory to show that the timer is running
        dataAccess.setTimerRunning(true);

        // Cancel any ongoing timers
        if(timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int remainingSeconds = durationMinutes * 60;
            @Override
            public void run() {
                // Stop the countdown if the timer has been cancelled
                if(!dataAccess.isTimerRunning()) {
                    timer.cancel();
                    return;
                }

                remainingSeconds--;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;

                String timeString = String.format("%02d:%02d", minutes, seconds);

                // Send the updated timer to the presenter
                presenter.updateTimerCountdown(timeString);

                if (remainingSeconds <= 0) {
                    dataAccess.setTimerRunning(false);
                    timer.cancel();

                    // Prepares the view when the timer is finished
                    presenter.prepareTimerFinishedView();
                }
            }
        }, 1000, 1000);
    }
}
