package use_case.save_result;

import entity.Library;

public class SaveResultOutputData {
    private String username;
    private Library matchedLibrary;

    public SaveResultOutputData(String username, Library matchedLibrary) {
        this.username = username;
        this.matchedLibrary = matchedLibrary;
    }

    public String getUserName() {
        return this.username;
    }

    public Library getMatchedLibrary() {
        return this.matchedLibrary;
    }
}
