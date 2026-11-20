package use_case.question_input;
import entity.Question;
import entity.Team;

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
        final Question currentQuestion = questionInputDataAccess.getCurrentQuestion();
        final Team currentTeam = questionInputDataAccess.getCurrentTeam();
        Boolean correct = false;
        int points_gained = 0;

        if (guess.isBlank()) {
            questionInputPresenter.prepareFailView("No input");
        }

        points_gained = currentQuestion.verifyAnswer(guess);

        if (points_gained > 0) {
            correct = true;
            currentTeam.addPoints(points_gained);
        }

        QuestionInputOutputData questionInputOutputData = new QuestionInputOutputData(correct, points_gained);
        questionInputPresenter.prepareSuccessView(questionInputOutputData);
    }
}
