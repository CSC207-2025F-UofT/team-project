package use_case.CompleteGame;

public class CompleteGameOutputData {
    //private final boolean saveFileDeleted;
    private final String message;

    //public CompleteGameOutputData(boolean saveFileDeleted) {
        //this.saveFileDeleted = saveFileDeleted;
    //}
    public CompleteGameOutputData(String message) {
        this.message = message;
    }

    //public boolean isSaveFileDeleted() {
        //return saveFileDeleted;
    //}
    public String getMessage() {
        return message;
    }
}
