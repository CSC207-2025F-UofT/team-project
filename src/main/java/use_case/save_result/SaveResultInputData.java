package use_case.save_result;

import entity.Library;

import java.util.List;

public class SaveResultInputData {
    private String username;
    private List<Integer> chosenIndices;
    private Library matchedLibrary;

    public SaveResultInputData(String username, List<Integer> chosenIndices, Library matchedLibrary) {
        this.username = username;
        this.chosenIndices = chosenIndices;
        this.matchedLibrary = matchedLibrary;
    }

    public String getUsername() {
        return this.username;
    }

    public List<Integer> getChosenIndices() {
        return this.chosenIndices;
    }

    public Library getMatchedLibrary() {
        return this.matchedLibrary;
    }
}
