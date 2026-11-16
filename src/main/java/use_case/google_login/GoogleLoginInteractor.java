package use_case.google_login;

import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import use_case.login.LoginUserDataAccessInterface;

import java.io.IOException;

/** Starts the same application session as password login. */
public final class GoogleLoginInteractor {
    private final GoogleIdentityGateway identityGateway;
    private final LoginUserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary presenter;

    public GoogleLoginInteractor(GoogleIdentityGateway identityGateway,
                                 LoginUserDataAccessInterface userDataAccess,
                                 LoginOutputBoundary presenter) {
        this.identityGateway = identityGateway;
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    public void execute() {
        try {
            String subject = identityGateway.authenticate();
            if (subject == null || subject.isBlank()) {
                presenter.prepareFailView("Google did not return an account ID.");
                return;
            }

            String accountId = "google:" + subject;
            if (userDataAccess.existsByName(accountId)) {
                presenter.prepareFailView("This Google account ID conflicts with an existing local account.");
                return;
            }

            userDataAccess.setCurrentUsername(accountId);
            presenter.prepareSuccessView(new LoginOutputData(accountId));
        }
        catch (IOException exception) {
            presenter.prepareFailView(exception.getMessage());
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            presenter.prepareFailView("Google sign-in was interrupted.");
        }
    }
}
