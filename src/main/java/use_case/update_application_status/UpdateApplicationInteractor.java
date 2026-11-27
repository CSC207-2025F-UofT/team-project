package use_case.update_application_status;

public class UpdateApplicationInteractor {

    private final UpdateApplicationOutputBoundary presenter;

    public UpdateApplicationInteractor(UpdateApplicationOutputBoundary presenter) {
        this.presenter = presenter;
    }
}
