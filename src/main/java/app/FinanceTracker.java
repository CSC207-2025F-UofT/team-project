package app;

import frameworks_and_drivers.persistence.JsonTransactionDataAccess;
import interface_adapter.transactions.TransactionDashboardController;
import interface_adapter.transactions.TransactionDashboardPresenter;
import interface_adapter.transactions.TransactionDashboardViewModel;
import use_case.transactions.TransactionDashboardInputBoundary;
import use_case.transactions.TransactionDashboardInteractor;
import use_case.transactions.TransactionDashboardOutputBoundary;
import use_case.transactions.TransactionDataAccessInterface;
import view.FinanceTrackerView;

import javax.swing.SwingUtilities;
import java.nio.file.Path;

/** Application entry point and composition root. */
public final class FinanceTracker {
    private FinanceTracker() { }

    private static void launch() {
        TransactionDashboardViewModel dashboardModel = new TransactionDashboardViewModel();
        // All user, account, and transaction data lives in this one JSON database.
        TransactionDataAccessInterface transactionData =
            new JsonTransactionDataAccess(Path.of("data/Profiles.json"));
        TransactionDashboardOutputBoundary dashboardPresenter =
            new TransactionDashboardPresenter(dashboardModel);
        TransactionDashboardInputBoundary dashboardInteractor =
            new TransactionDashboardInteractor(transactionData, dashboardPresenter);
        TransactionDashboardController dashboardController =
            new TransactionDashboardController(dashboardInteractor);

        new FinanceTrackerView(dashboardController, dashboardModel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinanceTracker::launch);
    }
}
