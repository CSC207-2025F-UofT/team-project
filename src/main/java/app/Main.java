package app;

import data_access.GeminiLectureNotesDataAccess;
import data_access.LectureNotesDataAccessInterface;
import interface_adapters.lecturenotes.GenerateLectureNotesController;
import interface_adapters.lecturenotes.GenerateLectureNotesPresenter;
import interface_adapters.lecturenotes.LectureNotesViewModel;
import usecases.lecturenotes.GenerateLectureNotesInteractor;
import views.LectureNotesView;

import javax.swing.*;
import javax.swing.WindowConstants;


public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // 1. ViewModel
            LectureNotesViewModel viewModel = new LectureNotesViewModel();

            // 2. DataAccess:connect to Gemini
            LectureNotesDataAccessInterface dataAccess =
                    new GeminiLectureNotesDataAccess();

            // 3. Presenter
            GenerateLectureNotesPresenter presenter =
                    new GenerateLectureNotesPresenter(viewModel);

            // 4. Interactor
            GenerateLectureNotesInteractor interactor =
                    new GenerateLectureNotesInteractor(dataAccess, presenter);

            // 5. Controller
            GenerateLectureNotesController controller =
                    new GenerateLectureNotesController(interactor);

            // 6. View
            LectureNotesView view = new LectureNotesView(controller, viewModel);

            // 7. Window
            JFrame frame = new JFrame("Lecture Notes (Gemini)");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
