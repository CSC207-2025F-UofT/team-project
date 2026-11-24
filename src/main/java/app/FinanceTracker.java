package app;

import frameworks_and_drivers.persistence.JsonTransactionDataAccess;
import frameworks_and_drivers.persistence.JsonUserDataAccess;
import frameworks_and_drivers.security.Pbkdf2PasswordHasher;
import interface_adapter.signup.SignUpController;
import interface_adapter.signup.SignUpPresenter;
import interface_adapter.signup.SignUpViewModel;
import interface_adapter.transactions.TransactionDashboardController;
import interface_adapter.transactions.TransactionDashboardPresenter;
import interface_adapter.transactions.TransactionDashboardViewModel;
import use_case.signup.SignUpInteractor;
import use_case.transactions.TransactionDashboardInteractor;
import use_case.transactions.TransactionDashboardInputBoundary;
import use_case.transactions.TransactionDashboardOutputBoundary;
import use_case.transactions.TransactionDataAccessInterface;
import view.FinanceTrackerView;
import view.SignUpView;
import javax.swing.SwingUtilities;
import java.nio.file.Path;

/** Application entry point and composition root. */
public final class FinanceTracker {
    private FinanceTracker() { }

    private static void launch() {
        TransactionDashboardViewModel dashboardModel=new TransactionDashboardViewModel();
        TransactionDataAccessInterface transactionData=new JsonTransactionDataAccess(Path.of("data/UserTransactions.json"));
        TransactionDashboardOutputBoundary dashboardPresenter=new TransactionDashboardPresenter(dashboardModel);
        TransactionDashboardInputBoundary dashboardInteractor=new TransactionDashboardInteractor(transactionData,dashboardPresenter);
        TransactionDashboardController dashboardController=new TransactionDashboardController(dashboardInteractor);

        Pbkdf2PasswordHasher passwordHasher=new Pbkdf2PasswordHasher();
        JsonUserDataAccess userData=new JsonUserDataAccess(Path.of("data/Profiles.json"),passwordHasher);
        Runnable openSignUp=()->{
            SignUpViewModel signupModel=new SignUpViewModel();
            SignUpPresenter signupPresenter=new SignUpPresenter(signupModel);
            SignUpInteractor signupInteractor=new SignUpInteractor(userData,passwordHasher,signupPresenter);
            SignUpController signupController=new SignUpController(signupInteractor);
            SignUpView signupView=new SignUpView(signupController,signupModel);
            signupView.setVisible(true);
        };
        new FinanceTrackerView(dashboardController,dashboardModel,openSignUp);
    }

    public static void main(String[] args){SwingUtilities.invokeLater(FinanceTracker::launch);}
}
