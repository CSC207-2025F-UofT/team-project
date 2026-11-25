package use_case.save;

public class SaveInteractor implements SaveInputBoundary {
    private final SaveDataAccessInterface dataAccess;
    private final SaveOutputBoundary presenter;

    public SaveInteractor(SaveDataAccessInterface dataAccess, SaveOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveInputData inputData) {
        String username = inputData.getUsername();
        int deepestFloor = inputData.getDeepestFloor();

        if (username == null || username.isEmpty()) {
            presenter.prepareFailView("Username is empty");
            return;
        }
        int deepest = deepestFloor;
        if (dataAccess.hasSave(username)) {
            deepest = Math.max(deepest, dataAccess.getDeepestFloor(username));
        }
        dataAccess.saveDeepestFloor(username, deepest);
        SaveOutputData outputData = new SaveOutputData(username, deepest);
        presenter.prepareSuccessView(outputData);
        }

    }
