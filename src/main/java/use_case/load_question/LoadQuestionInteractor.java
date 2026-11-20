package use_case.load_question;

import entity.Question;

public class LoadQuestionInteractor implements LoadQuestionInputBoundary {
    private final LoadQuestionDataAccessInterface questionDataAccessObject;
    private final LoadQuestionOutputBoundary loadQuestionPresenter;

    public LoadQuestionInteractor(LoadQuestionDataAccessInterface loadQuestionDataAccessInterface,
                                  LoadQuestionOutputBoundary loadQuestionOutputBoundary){
        this.questionDataAccessObject = loadQuestionDataAccessInterface;
        this.loadQuestionPresenter = loadQuestionOutputBoundary;
    }

    @Override
    public void execute() {
        if (!questionDataAccessObject.existsUnusedQuestion()) {
            loadQuestionPresenter.prepareFailView("No unused questions available");
        }
        else {
            final Question nextQuestion = questionDataAccessObject.getNextQuestion();

            questionDataAccessObject.setCurrentQuestion(nextQuestion);

            final LoadQuestionOutputData outputData = new LoadQuestionOutputData(
                    nextQuestion.getQuestionText(), nextQuestion.getAnswerCount()
            );
            this.loadQuestionPresenter.prepareSuccessView(outputData);
        }
    }
}
