package use_case.load;

public class LoadInteractor implements LoadInputBoundary {
    private final LoadDataAccessInterface dataAccess;
    private final LoadOutputBoundary presenter;

    public LoadInteractor(LoadDataAccessInterface dataAccess, LoadOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadInputData inputData) {
        String username = inputData.getUsername();

        if (username == null || username.isEmpty()) {
            presenter.prepareFailView("Username is empty");
            return;
        }
        if (!dataAccess.hasSave(username)) {
            presenter.prepareFailView("No save found for " + username + ".");
            return;
        }

        int floor = dataAccess.getDeepestFloor(username);
        presenter.prepareSuccessView(new LoadOutputData(username, floor));
    }
}