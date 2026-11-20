package use_case.add_question;

import entity.Answer;
import entity.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the Add Question Use Case.
 */
public class AddQuestionInteractor implements AddQuestionInputBoundary {
    private final AddQuestionDataAccessInterface dataAccess;
    private final AddQuestionOutputBoundary presenter;

    public AddQuestionInteractor(AddQuestionDataAccessInterface dataAccess,
                                 AddQuestionOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddQuestionInputData inputData) {
        final String questionText = inputData.getQuestionText();

        if (questionText == null || questionText.trim().isEmpty()) {
            presenter.prepareFailView("Question text cannot be empty.");
            return;
        }

        final List<String> primaryAnswers = inputData.getPrimaryAnswers();
        final List<Integer> scores = inputData.getScores();
        final List<List<String>> variants = inputData.getAcceptedVariants();

        if (primaryAnswers == null || primaryAnswers.isEmpty()) {
            presenter.prepareFailView("At least one answer must be provided.");
            return;
        }

        if (primaryAnswers.size() != scores.size() || primaryAnswers.size() != variants.size()) {
            presenter.prepareFailView("Answer lists, scores, and variants must have matching sizes.");
            return;
        }

        if (dataAccess.questionExists(questionText.trim())) {
            presenter.prepareFailView("This question already exists in the question bank.");
            return;
        }

        final List<Answer> answersList = new ArrayList<>();

        for (int i = 0; i < primaryAnswers.size(); i++) {
            String ansText = primaryAnswers.get(i);
            int score = scores.get(i);
            List<String> varList = variants.get(i);

            if (ansText == null || ansText.trim().isEmpty()) {
                presenter.prepareFailView("Answer text cannot be empty.");
                return;
            }
            if (score <= 0) {
                presenter.prepareFailView("Each answer must have a positive score.");
                return;
            }
            answersList.add(new Answer(ansText.trim(), varList, score));
        }

        final Question newQuestion = new Question(questionText.trim(), answersList);
        dataAccess.saveQuestion(newQuestion);

        final AddQuestionOutputData outputData = new AddQuestionOutputData(
                newQuestion.getQuestionText(),
                answersList.size(),
                "Question successfully added and saved!"
        );
        presenter.prepareSuccessView(outputData);
    }
}
