//package use_case.openGame;
//
//public interface OpenGameInputBoundary {
//    void execute(OpenGameInputData inputData);
//}

package use_case.openGame;

import use_case.show_results.ShowResultsInputData;

public interface OpenGameInputBoundary {
    void execute(OpenGameInputData inputData);
    void switchToMoveScreen();
}
