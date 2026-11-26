package use_case.question_input;
import entity.*;

/**
 * Question input interactor
 */
public class QuestionInputInteractor implements QuestionInputInputBoundary {
    private final QuestionInputDataAccessInterface questionInputDataAccess;
    private final QuestionInputOutputBoundary  questionInputPresenter;

    public QuestionInputInteractor(QuestionInputDataAccessInterface questionInputDataAccess,
                                   QuestionInputOutputBoundary questionInputPresenter) {
        this.questionInputDataAccess = questionInputDataAccess;
        this.questionInputPresenter = questionInputPresenter;
    }

    @Override
    public void execute(QuestionInputInputData questionInputInputData) {
        final String guess = questionInputInputData.getGuessText();
        final Board currentBoard = questionInputDataAccess.getCurrentBoard();
        final Question currentQuestion = currentBoard.getCurrentQuestion();
        final Team currentTeam = currentBoard.getActiveTeam();
        boolean correct = false;
        int points_gained = 0;

        if (currentBoard.getWrongCount() == 3) {
            questionInputPresenter.prepareFailView("You have reached 3 wrong guesses");
            return;
        }

        // Idk which will actually happen but just to be safe im also putting this cause im pretty sure it could be null
        if (guess == null) {
            questionInputPresenter.prepareFailView("Guess is null");
        }

        if (guess.isBlank()) {
            questionInputPresenter.prepareFailView("No input");
            return;
        }

        points_gained = currentQuestion.verifyAnswer(guess);

        if (points_gained > 0) {
            correct = true;
            currentTeam.addPoints(points_gained);
        }

        if (points_gained == 0) {
            currentBoard.setWrongCount(currentBoard.getWrongCount() + 1);
        }

        QuestionInputOutputData questionInputOutputData = new QuestionInputOutputData(correct, points_gained,
                currentTeam.getName(), currentQuestion.getQuestionText(), guess);
        questionInputPresenter.prepareSuccessView(questionInputOutputData);
    }
}
