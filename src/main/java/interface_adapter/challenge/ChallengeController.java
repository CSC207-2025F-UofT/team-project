package interface_adapter.challenge;

import use_case.challenge.ChallengeInputBoundary;
import use_case.challenge.ChallengeInputData;
import use_case.challenge.ChallengeOutputBoundary;
import use_case.challenge.ChallengeStateException;
import use_case.question.QuestionLoadException;
import static use_case.challenge.ChallengeInputData.Action;

/** Converts view actions to input data; threading belongs to the Swing view. */
public final class ChallengeController {
    private final ChallengeInputBoundary input;
    private final ChallengeOutputBoundary output;

    public ChallengeController(ChallengeInputBoundary input, ChallengeOutputBoundary output) {
        this.input = input;
        this.output = output;
    }

    public void open() { execute(Action.OPEN, "", "", 0, -1, false); }
    public void select(String courseId) { execute(Action.SELECT, courseId, "", 0, -1, false); }
    public void start(String courseId) { execute(Action.START, courseId, "", 0, -1, false); }
    public void answer(String attemptId, int questionIndex, int answerIndex) {
        execute(Action.ANSWER, "", attemptId, questionIndex, answerIndex, false);
    }
    public void next(String attemptId, int questionIndex) {
        execute(Action.NEXT, "", attemptId, questionIndex, -1, false);
    }
    public void leave(boolean confirmed) { execute(Action.LEAVE, "", "", 0, -1, confirmed); }

    private void execute(Action action, String courseId, String attemptId,
                         int questionIndex, int answerIndex, boolean confirmed) {
        try {
            input.execute(new ChallengeInputData(action, courseId, attemptId,
                    questionIndex, answerIndex, confirmed));
        }
        catch (QuestionLoadException | ChallengeStateException exception) {
            output.presentError(exception.getMessage());
        }
    }
}
