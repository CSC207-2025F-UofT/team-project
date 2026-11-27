package interface_adapter.save_result;

import entity.Library;
import java.util.List;

import use_case.save_result.SaveResultInputBoundary;
import use_case.save_result.SaveResultInputData;

public class SaveResultController {
    private final SaveResultInputBoundary interactor;

    public SaveResultController(SaveResultInputBoundary interactor) {
        this.interactor = interactor;
    }

    // Call the interactor passing in an InputData object
    public void execute(String username, List<Integer> chosenIndices, Library matchedLibrary) {
        SaveResultInputData inputData = new SaveResultInputData(username, chosenIndices, matchedLibrary);
        interactor.execute(inputData);
    }
}