package use_case.loadQuiz;

import entity.AnswerOption;
import entity.Quiz;
import use_case.quiz.QuizDataAccessInterface;

import java.util.List;
import java.util.stream.Collectors;

public class LoadQuizInteractor implements LoadQuizInputBoundary {
    private final QuizDataAccessInterface repository;
    private final LoadQuizOutputBoundary presenter;

    public LoadQuizInteractor(QuizDataAccessInterface repository, LoadQuizOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void loadQuiz(LoadQuizInputData data) {
        Quiz quiz = repository.findById(data.getQuizId());
        if (quiz == null) {
            // handle error case - maybe a separate presenter method
            return;
        }

        List<String> optionTexts = quiz.getQuestion().getOptions()
                .stream()
                .map(AnswerOption::getText)
                .collect(Collectors.toList());

        LoadQuizOutputData output = new LoadQuizOutputData(
                quiz.getQuizId(),
                quiz.getQuestion().getText(),
                optionTexts
        );

        presenter.present(output);
    }
}
