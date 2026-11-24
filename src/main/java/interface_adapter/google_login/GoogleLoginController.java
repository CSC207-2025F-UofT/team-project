package interface_adapter.google_login;

import use_case.google_login.GoogleLoginInteractor;

public final class GoogleLoginController {
    private final GoogleLoginInteractor interactor;

    public GoogleLoginController(GoogleLoginInteractor interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
