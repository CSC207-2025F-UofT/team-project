package quiz_system.usecase;

import quiz_system.entity.Quiz;
import quiz_system.entity.QuizResult;

public final class SubmitQuizInteractor implements SubmitQuizInputBoundary {

    private final QuizDataAccessInterface repository;
    private final SubmitQuizOutputBoundary presenter;

    public SubmitQuizInteractor(QuizDataAccessInterface repository, SubmitQuizOutputBoundary presenter) {
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void submit(SubmitQuizInputData data) {
        // load quiz
        Quiz quiz = repository.findById(data.getQuizId());

        // record selected option if not null
        Integer selected = data.getSelectedOptionId();
        if (selected != null) {
            quiz.recordAnswer(selected);
        }

        // apply quiz logic (correct/incorrect/warning determined by entity)
        QuizResult result = quiz.submit();

        // save quiz state
        repository.save(quiz);

        // create output data for presenter
        SubmitQuizOutputData output = new SubmitQuizOutputData(
                quiz.getQuizId(),
                quiz.isCompleted(),
                result.getStatus().name(),
                result.getMessage()
        );

        // presenter receives result
        presenter.present(output);
    }
}
