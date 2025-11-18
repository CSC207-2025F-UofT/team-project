package use_case.homescreen;

public class HomescreenInteractor implements HomescreenInputBoundary {
    private final HomescreenOutputBoundary presenter;

    public HomescreenInteractor(HomescreenOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(HomescreenInputData inputData) {
        String action = inputData.getAction();

        //just logs the action - no navigation yet
        System.out.println("Action received: " + action);

        // Don't navigate anywhere yet since views don't exist
        // When you create the views, you'll uncomment this:
        /*
        String targetView = "";
        switch (action) {
            case "browse_landmarks":
                targetView = "landmarks";
                break;
            case "plan_route":
                targetView = "route";
                break;
            case "my_progress":
                targetView = "progress";
                break;
            default:
                presenter.prepareFailView("Unknown action: " + action);
                return;
        }

        HomescreenOutputData outputData = new HomescreenOutputData(targetView, true);
        presenter.prepareSuccessView(outputData);
        */
    }
}