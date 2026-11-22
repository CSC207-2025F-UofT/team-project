package app;

import application_business_rules.transactions.TransactionDashboardInteractor;
import application_business_rules.transactions.TransactionDashboardInputBoundary;
import application_business_rules.transactions.TransactionDashboardOutputBoundary;
import application_business_rules.transactions.TransactionDataAccessInterface;
import frameworks_and_drivers.persistence.JsonTransactionDataAccess;
import interface_adapter.transactions.TransactionDashboardController;
import interface_adapter.transactions.TransactionDashboardPresenter;
import interface_adapter.transactions.TransactionDashboardViewModel;
import view.FinanceTrackerView;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

/**
 * Main Component / composition root.
 * This is the class to run to launch Finance Tracker.
 */
public final class FinanceTracker {
    private FinanceTracker() {
        // Application is launched through main.
    }

    private static void launch() {
        TransactionDashboardViewModel viewModel = new TransactionDashboardViewModel();

        TransactionDataAccessInterface transactionDataAccess =
            new JsonTransactionDataAccess(Path.of("data/UserTransactions.json"));

        TransactionDashboardOutputBoundary presenter =
            new TransactionDashboardPresenter(viewModel);

        TransactionDashboardInputBoundary interactor =
            new TransactionDashboardInteractor(transactionDataAccess, presenter);

        TransactionDashboardController controller =
            new TransactionDashboardController(interactor);

        new FinanceTrackerView(controller, viewModel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinanceTracker::launch);
    }
}
