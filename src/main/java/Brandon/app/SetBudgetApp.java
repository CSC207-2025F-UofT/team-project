package Brandon.app;

import Brandon.data.InMemorySetBudgetDataAccess;
import Brandon.interfaceAdapter.*;
import Brandon.useCase.SetBudgetInteractor;
import Brandon.view.SetBudgetView;

import javax.swing.*;

public class SetBudgetApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SetBudgetViewModel viewModel = new SetBudgetViewModel();
            SetBudgetPresenter presenter = new SetBudgetPresenter(viewModel);
            InMemorySetBudgetDataAccess dataAccess = new InMemorySetBudgetDataAccess();
            SetBudgetInteractor interactor = new SetBudgetInteractor(dataAccess, presenter);
            SetBudgetController controller = new SetBudgetController(interactor);

            SetBudgetView view = new SetBudgetView(controller, viewModel);

            JFrame frame = new JFrame("Set Budget");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
