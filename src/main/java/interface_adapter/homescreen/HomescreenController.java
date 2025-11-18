package interface_adapter.homescreen;

import use_case.homescreen.HomescreenInputBoundary;
import use_case.homescreen.HomescreenInputData;

public class HomescreenController {
    private final HomescreenInputBoundary interactor;

    public HomescreenController(HomescreenInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void browseLandmarks() {
        System.out.println("Browse Landmarks button clicked");
        HomescreenInputData inputData = new HomescreenInputData("browse_landmarks");
        interactor.execute(inputData);
    }

    public void planRoute() {
        System.out.println("Plan Route button clicked");
        HomescreenInputData inputData = new HomescreenInputData("plan_route");
        interactor.execute(inputData);
    }

    public void viewProgress() {
        System.out.println("My Progress button clicked");
        HomescreenInputData inputData = new HomescreenInputData("my_progress");
        interactor.execute(inputData);
    }
}