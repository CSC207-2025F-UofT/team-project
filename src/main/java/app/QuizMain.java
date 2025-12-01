package app;

import data_access.InMemoryQuizDataAccessObject;
import data_access.QuizzesReader;
import interface_adapter.Battle.Battle_ViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.quiz.QuizController;
import interface_adapter.quiz.QuizPresenter;
import interface_adapter.quiz.LoadQuizPresenter;
import interface_adapter.quiz.Quiz_ViewModel;
import use_case.loadQuiz.LoadQuizInputBoundary;
import use_case.loadQuiz.LoadQuizInteractor;
import use_case.loadQuiz.LoadQuizOutputBoundary;
import use_case.quiz.SubmitQuizInputBoundary;
import use_case.quiz.SubmitQuizInteractor;
import use_case.quiz.SubmitQuizOutputBoundary;
import view.QuizView;

import javax.swing.*;
import java.awt.*;


public class QuizMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Load quizzes from quizzes.txt file
            InMemoryQuizDataAccessObject repo = new InMemoryQuizDataAccessObject();
            new QuizzesReader().loadQuizzes(repo);

            // Create ViewModels
            Quiz_ViewModel quizViewModel = new Quiz_ViewModel();
            Battle_ViewModel battleViewModel = new Battle_ViewModel();
            ViewManagerModel viewManagerModel = new ViewManagerModel(quizViewModel.getViewName());

            // Create Presenters
            LoadQuizOutputBoundary loadQuizPresenter = new LoadQuizPresenter(quizViewModel);
            SubmitQuizOutputBoundary submitQuizPresenter = new QuizPresenter(quizViewModel, battleViewModel, viewManagerModel);

            // Create Interactors (Use Cases)
            LoadQuizInputBoundary loadQuizInteractor = new LoadQuizInteractor(repo, loadQuizPresenter);
            SubmitQuizInputBoundary submitQuizInteractor = new SubmitQuizInteractor(repo, submitQuizPresenter);

            // Create Controller (inject BOTH interactors)
            QuizController controller = new QuizController(submitQuizInteractor, loadQuizInteractor);

            // Create main frame
            JFrame mainFrame = new JFrame("Quiz Battle System");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(600, 400);
            mainFrame.setLocationRelativeTo(null);

            // Create card layout for view switching
            CardLayout cardLayout = new CardLayout();
            JPanel cardPanel = new JPanel(cardLayout);

            // Create Quiz View (now a JPanel, not JFrame)
            QuizView quizView = new QuizView(controller, quizViewModel);

            // Create placeholder Battle View (you'll replace this with your actual BattleView)
            JPanel battleView = new JPanel();
            battleView.add(new JLabel("Battle View - To be implemented"));

            // Add views to card panel
            cardPanel.add(quizView, quizViewModel.getViewName());
            cardPanel.add(battleView, battleViewModel.getViewName());

            // Add card panel to main frame
            mainFrame.add(cardPanel);

            // Add ViewManagerModel listener to switch views
            viewManagerModel.addPropertyChangeListener(evt -> {
                if ("state".equals(evt.getPropertyName())) {
                    String viewName = (String) evt.getNewValue();
                    cardLayout.show(cardPanel, viewName);
                }
            });

            // Show main frame and load first quiz
            mainFrame.setVisible(true);
            quizView.loadQuiz(1); // Start with quiz ID 1
        });
    }
}