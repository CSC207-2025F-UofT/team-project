package submitQuiz;

import entity.AnswerOption;
import entity.Question;
import entity.Quiz;
import entity.QuizResult;
import org.junit.Test;
import use_case.submitQuiz.QuizDataAccessInterface;
import use_case.submitQuiz.SubmitQuizInputBoundary;
import use_case.submitQuiz.SubmitQuizInputData;
import use_case.submitQuiz.SubmitQuizInteractor;
import use_case.submitQuiz.SubmitQuizOutputBoundary;
import use_case.submitQuiz.SubmitQuizOutputData;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class SubmitQuizInteractorTest {

    // helper to build quiz
    private Quiz createQuiz(int quizId, int correctOptionId) {
        List<AnswerOption> options = new ArrayList<>();
        options.add(new AnswerOption(1, "Option A"));
        options.add(new AnswerOption(2, "Option B"));
        options.add(new AnswerOption(3, "Option C"));
        options.add(new AnswerOption(4, "Option D"));

        Question question = new Question(10, "Sample question?", options, correctOptionId);
        return new Quiz(quizId, question);
    }

    // selected answer is correct
    @Test
    public void submitCorrectAnswer() {
        final Quiz quiz = createQuiz(1, 2);

        QuizDataAccessInterface repository = new QuizDataAccessInterface() {
            @Override
            public Quiz findById(int quizId) {
                assertEquals(1, quizId);
                return quiz;
            }

            @Override
            public void save(Quiz savedQuiz) {
                // ensure it's the same quiz instance
                assertSame(quiz, savedQuiz);
            }
        };

        SubmitQuizOutputBoundary output = new SubmitQuizOutputBoundary() {
            @Override
            public void present(SubmitQuizOutputData data) {
                assertEquals(1, data.getQuizId());
                assertTrue(data.isCompleted());
                assertEquals(QuizResult.Status.CORRECT.name(), data.getStatus());
                assertEquals("Correct Answer", data.getMessage());
            }

            @Override
            public void switchToBattleView(boolean isCorrect) {
                // not used here
                fail("switchToBattleView should not be called in submitCorrectAnswer test");
            }
        };

        SubmitQuizInputBoundary interactor = new SubmitQuizInteractor(repository, output);
        SubmitQuizInputData input = new SubmitQuizInputData(1, 2); // correct option

        interactor.submit(input);

        assertTrue(quiz.isCompleted());
        assertEquals(Boolean.TRUE, quiz.getAnsweredCorrectly());
    }

    // selected answer is incorrect
    @Test
    public void submitIncorrectAnswer() {
        final Quiz quiz = createQuiz(2, 2); // correct is 2

        QuizDataAccessInterface repository = new QuizDataAccessInterface() {
            @Override
            public Quiz findById(int quizId) {
                assertEquals(2, quizId);
                return quiz;
            }

            @Override
            public void save(Quiz savedQuiz) {
                assertSame(quiz, savedQuiz);
            }
        };

        SubmitQuizOutputBoundary output = new SubmitQuizOutputBoundary() {
            @Override
            public void present(SubmitQuizOutputData data) {
                assertEquals(2, data.getQuizId());
                assertTrue(data.isCompleted());
                assertEquals(QuizResult.Status.INCORRECT.name(), data.getStatus());
                assertEquals("Incorrect Answer", data.getMessage());
            }

            @Override
            public void switchToBattleView(boolean isCorrect) {
                fail("switchToBattleView should not be called in submitIncorrectAnswer test");
            }
        };

        SubmitQuizInputBoundary interactor = new SubmitQuizInteractor(repository, output);
        SubmitQuizInputData input = new SubmitQuizInputData(2, 3); // wrong option

        interactor.submit(input);

        assertTrue(quiz.isCompleted());
        assertEquals(Boolean.FALSE, quiz.getAnsweredCorrectly());
    }

    // no option selected so selectedOptionId == null
    @Test
    public void submitNoAnswerSelected() {
        final Quiz quiz = createQuiz(3, 2);

        QuizDataAccessInterface repository = new QuizDataAccessInterface() {
            @Override
            public Quiz findById(int quizId) {
                assertEquals(3, quizId);
                return quiz;
            }

            @Override
            public void save(Quiz savedQuiz) {
                assertSame(quiz, savedQuiz);
            }
        };

        SubmitQuizOutputBoundary output = new SubmitQuizOutputBoundary() {
            @Override
            public void present(SubmitQuizOutputData data) {
                assertEquals(3, data.getQuizId());
                // entity leaves completed = false when no answer
                assertFalse(data.isCompleted());
                assertEquals(QuizResult.Status.WARNING.name(), data.getStatus());
                assertEquals("Question Not Answered", data.getMessage());
            }

            @Override
            public void switchToBattleView(boolean isCorrect) {
                fail("switchToBattleView should not be called in submitNoAnswerSelected test");
            }
        };

        SubmitQuizInputBoundary interactor = new SubmitQuizInteractor(repository, output);
        SubmitQuizInputData input = new SubmitQuizInputData(3, null); // no selection

        interactor.submit(input);

        assertFalse(quiz.isCompleted());
        assertNull(quiz.getAnsweredCorrectly());
    }

    // test method switchToBattleView on the interactor
    @Test
    public void switchToBattleViewDelegatesToPresenter() {
        QuizDataAccessInterface repository = new QuizDataAccessInterface() {
            @Override
            public Quiz findById(int quizId) {
                fail("findById should not be called in switchToBattleView test");
                return null;
            }

            @Override
            public void save(Quiz quiz) {
                fail("save should not be called in switchToBattleView test");
            }
        };

        final boolean[] called = new boolean[1];
        final boolean[] lastArg = new boolean[1];

        SubmitQuizOutputBoundary output = new SubmitQuizOutputBoundary() {
            @Override
            public void present(SubmitQuizOutputData data) {
                fail("present should not be called in switchToBattleView test");
            }

            @Override
            public void switchToBattleView(boolean isCorrect) {
                called[0] = true;
                lastArg[0] = isCorrect;
            }
        };

        SubmitQuizInputBoundary interactor = new SubmitQuizInteractor(repository, output);

        interactor.switchToBattleView(true);

        assertTrue("Presenter.switchToBattleView should be called", called[0]);
        assertTrue("Argument should be passed through correctly", lastArg[0]);
    }
}
