package loadQuiz;

import entity.AnswerOption;
import entity.Question;
import entity.Quiz;
import org.junit.Test;
import use_case.loadQuiz.LoadQuizInputBoundary;
import use_case.loadQuiz.LoadQuizInputData;
import use_case.loadQuiz.LoadQuizInteractor;
import use_case.loadQuiz.LoadQuizOutputBoundary;
import use_case.loadQuiz.LoadQuizOutputData;
import use_case.submitQuiz.QuizDataAccessInterface;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class LoadQuizInteractorTest {
    @Test
    public void loadQuizStandardTest() {
        // build question and quiz
        List<AnswerOption> options = new ArrayList<>();
        options.add(new AnswerOption(1, "Option A"));
        options.add(new AnswerOption(2, "Option B"));
        options.add(new AnswerOption(3, "Option C"));
        options.add(new AnswerOption(4, "Option D"));

        Question question = new Question(10, "What is 2 + 2?", options, 2);
        final Quiz quiz = new Quiz(1, question);

        QuizDataAccessInterface repository = new QuizDataAccessInterface() {
            @Override
            public Quiz findById(int quizId) {
                assertEquals(1, quizId);
                return quiz;
            }

            @Override
            public void save(Quiz quiz) {
                // not used by LoadQuizInteractor so if called, that's a bug
                fail("save should not be called in LoadQuizInteractor");
            }
        };

        LoadQuizOutputBoundary output = new LoadQuizOutputBoundary() {
            @Override
            public void present(LoadQuizOutputData data) {
                assertEquals(1, data.getQuizId());
                assertEquals("What is 2 + 2?", data.getQuestionText());
                assertEquals(4, data.getOptionTexts().size());
                assertEquals("Option A", data.getOptionTexts().get(0));
                assertEquals("Option B", data.getOptionTexts().get(1));
                assertEquals("Option C", data.getOptionTexts().get(2));
                assertEquals("Option D", data.getOptionTexts().get(3));
            }
        };

        LoadQuizInputBoundary interactor = new LoadQuizInteractor(repository, output);
        LoadQuizInputData input = new LoadQuizInputData(1);

        interactor.loadQuiz(input);
    }

    // if quiz is not found, should early-return and never call presenter
    @Test
    public void loadQuizQuizNotFound() {
        QuizDataAccessInterface repository = new QuizDataAccessInterface() {
            @Override
            public Quiz findById(int quizId) {
                assertEquals(999, quizId);
                return null; // triggers the early return branch
            }

            @Override
            public void save(Quiz quiz) {
                fail("save should not be called when quiz is not found");
            }
        };

        LoadQuizOutputBoundary output = new LoadQuizOutputBoundary() {
            @Override
            public void present(LoadQuizOutputData data) {
                fail("Output should not be called when quiz is null");
            }
        };

        LoadQuizInputBoundary interactor = new LoadQuizInteractor(repository, output);
        LoadQuizInputData input = new LoadQuizInputData(999);

        interactor.loadQuiz(input);
    }
}
