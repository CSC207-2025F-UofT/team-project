// src/java/interface_adapter/browse_landmarks/BrowseLandmarksController.java
package interface_adapter.browselandmarks;

import use_case.browselandmarks.BrowseLandmarksInputBoundary;

public class BrowseLandmarksController {

    private final BrowseLandmarksInputBoundary interactor;

    public BrowseLandmarksController(BrowseLandmarksInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadLandmarks() {
        interactor.loadLandmarks();
    }
}
