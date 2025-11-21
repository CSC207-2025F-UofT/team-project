package quiz_system.app;

import quiz_system.data_access.InMemoryQuizDataAccessObject;
import quiz_system.data_access.QuizzesReader;
import quiz_system.interface_adapters.QuizController;
import quiz_system.interface_adapters.QuizPresenter;
import quiz_system.interface_adapters.QuizViewModel;
import quiz_system.view.QuizView;
import quiz_system.usecase.SubmitQuizInputBoundary;
import quiz_system.usecase.SubmitQuizInteractor;
import quiz_system.usecase.SubmitQuizOutputBoundary;

import javax.swing.*;

public class QuizMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // load quizzes from quizzes.txt file
            InMemoryQuizDataAccessObject repo = new InMemoryQuizDataAccessObject();
            new QuizzesReader().loadQuizzes(repo);

            // use cases
            QuizViewModel vm = new QuizViewModel();
            SubmitQuizOutputBoundary presenter = new QuizPresenter(vm);
            SubmitQuizInputBoundary interactor = new SubmitQuizInteractor(repo, presenter);
            QuizController controller = new QuizController(interactor);

            // gui
            QuizView view = new QuizView(repo, controller, vm);
            view.loadQuiz(1); // for example, let's start with quiz ID 1
            view.setVisible(true);

        });
    }
}