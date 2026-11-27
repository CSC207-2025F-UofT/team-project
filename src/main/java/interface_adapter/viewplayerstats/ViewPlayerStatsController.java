package interface_adapter.viewplayerstats;
import java.util.Objects;

import use_case.viewplayerstats.ViewPlayerStatsInputBoundary;
import use_case.viewplayerstats.ViewPlayerStatsInputData;

public class ViewPlayerStatsController {
    private final ViewPlayerStatsInputBoundary inputBoundary;

    public ViewPlayerStatsController(ViewPlayerStatsInputBoundary inputBoundary){
        this.inputBoundary = Objects.requireNonNull(inputBoundary);
    }

    public void execute() {
        ViewPlayerStatsInputData inputData = new ViewPlayerStatsInputData();

        inputBoundary.execute(inputData);
    }
}
