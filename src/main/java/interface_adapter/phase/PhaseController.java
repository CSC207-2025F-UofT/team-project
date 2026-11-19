package interface_adapter.phase;

import use_case.phase_portrait.PhasePortraitInputBoundary;
import use_case.phase_portrait.PhasePortraitInputData;

// Phase Portrait button -> use case; the answer comes back through the PhaseViewModel
public class PhaseController {
    private final PhasePortraitInputBoundary interactor;

    public PhaseController(PhasePortraitInputBoundary interactor) {
        this.interactor = interactor;
    }

    // ranges are {min, max} from AxisRangeParser, null x = default, null y = pick one
    public void execute(String latex, double[] xRange, double[] yRange) {
        double xMin = xRange == null ? PhasePortraitInputData.DEFAULT_X_MIN : xRange[0];
        double xMax = xRange == null ? PhasePortraitInputData.DEFAULT_X_MAX : xRange[1];
        double yMin = yRange == null ? PhasePortraitInputData.AUTOMATIC : yRange[0];
        double yMax = yRange == null ? PhasePortraitInputData.AUTOMATIC : yRange[1];
        interactor.execute(new PhasePortraitInputData(latex, xMin, xMax, yMin, yMax));
    }
}
