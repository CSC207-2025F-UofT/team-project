package use_case.log_input;

import entity.GuessRecord;
import entity.RoundRecord;

/**
 * Log Input Interactor: records a guess into the current round's history.
 */
public class LogInputInteractor implements LogInputInputBoundary {
    private final LogInputDataAccessInterface logInputDataAccess;
    private final LogInputOutputBoundary logInputPresenter;

    public LogInputInteractor(LogInputDataAccessInterface logInputDataAccess, LogInputOutputBoundary logInputPresenter) {
        this.logInputDataAccess = logInputDataAccess;
        this.logInputPresenter = logInputPresenter;
    }

    @Override
    public void execute(LogInputInputData logInputInputData) {
        final RoundRecord roundRecord = logInputDataAccess.getCurrentRoundRecord();

        if (roundRecord == null) {
            throw new IllegalStateException("No active round to log the guess to");
        }
        final String teamName = logInputInputData.getTeamName();
        final String question = logInputInputData.getQuestionText();
        final String guess = logInputInputData.getGuess();
        final Boolean correct = logInputInputData.isCorrect();
        final int points = logInputInputData.getPointsAwarded();
        int roundNumber = roundRecord.getRoundNumber();

        GuessRecord guessRecord = new GuessRecord(teamName, question, guess, correct, points);
        roundRecord.addGuess(guessRecord);
        //add the roundRecord to the game

        LogInputOutputData logInputOutputData = new LogInputOutputData(roundNumber, question, teamName, guess, correct, points);
        logInputPresenter.prepareSuccessView(logInputOutputData);
    }
}
