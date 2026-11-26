package use_case.load_question;

import data_access.InMemoryDataAccessObject;
import entity.Answer;
import entity.Question;
import org.junit.jupiter.api.Test;
import use_case.add_question.AddQuestionDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadQuestionInteractorTest {

    @Test
    void successTest() {
        InMemoryDataAccessObject dataAccessObject = new InMemoryDataAccessObject();

        // For the success test, we need to add a question to the data access before retrieving one.
        Question testQuestion = new Question("Name something that can be inflated or deflated",
                new ArrayList<>(List.of(
                        new Answer("Balloon", null, 44),
                        new Answer("Tire", null, 25),
                        new Answer("Beach ball", null, 10),
                        new Answer("Ego", null, 7),
                        new Answer("Raft", null, 5)
                ))
        );
        dataAccessObject.saveQuestion(testQuestion);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoadQuestionOutputBoundary successPresenter = new LoadQuestionOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadQuestionOutputData outputData) {
                assertEquals("Name something that can be inflated or deflated", outputData.getQuestionText());
                assertEquals(5, outputData.getAnswerCount());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoadQuestionInputBoundary interactor = new LoadQuestionInteractor(dataAccessObject, successPresenter);
        interactor.execute();
    }

    @Test
    void failureNoQuestionTest() {
        InMemoryDataAccessObject dataAccessObject = new InMemoryDataAccessObject();

        // For the failure test, the data access object should be empty.

        // This creates a failurePresenter that tests whether the test case is as we expect.
        LoadQuestionOutputBoundary failurePresenter = new LoadQuestionOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadQuestionOutputData outputData) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("No unused questions available", error);
            }
        };

        LoadQuestionInputBoundary interactor = new LoadQuestionInteractor(dataAccessObject, failurePresenter);
        interactor.execute();
    }
}
