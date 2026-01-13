package use_case.deleteAccount;

public class DeleteAccountInteractor implements DeleteAccountInputBoundary {
    private final DeleteAccountUserDataInterface userDataAccess;
    private final DeleteAccountOutputBoundary presenter;

    public DeleteAccountInteractor(DeleteAccountUserDataInterface userDataAccess,
                                   DeleteAccountOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteAccountInputData inputData) {
        String username = inputData.getUsername();

        userDataAccess.deleteByName(username);
        presenter.prepareSuccessView(new DeleteAccountOutputData(username));
    }
}
