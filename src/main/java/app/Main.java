package app;

import data_access.FirestoreQuizResultDAO;
import entity.Library;
import interface_adapter.save_result.SaveResultController;
import interface_adapter.save_result.SaveResultPresenter;
import interface_adapter.save_result.SaveResultViewModel;
import use_case.save_result.SaveResultInteractor;
import view.SaveResultView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.util.List;

/**
 * Composition root: builds every object, connects them, and shows the window.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // Blue (data access): Firestore, using the key file in the project root
        FirestoreQuizResultDAO resultDAO = new FirestoreQuizResultDAO("serviceAccountKey.json");

        SwingUtilities.invokeLater(() -> {
            // Save result use case
            SaveResultViewModel saveResultViewModel = new SaveResultViewModel();
            SaveResultPresenter saveResultPresenter = new SaveResultPresenter(saveResultViewModel);
            SaveResultInteractor saveResultInteractor = new SaveResultInteractor(saveResultPresenter, resultDAO);
            SaveResultController saveResultController = new SaveResultController(saveResultInteractor);
            SaveResultView saveResultView = new SaveResultView(saveResultViewModel, saveResultController);

            // Temporary placeholder until take quiz is wired in
            saveResultView.setResult(List.of(1, 0, 1, 2, 0, 1, 3, 0, 2), Library.ROBARTS);

            JFrame frame = new JFrame("Our Quiz");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(saveResultView);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
