package interface_adapter.drop_object;


public final class DropObjectViewModel {

    private boolean hasDropResult;
    private boolean lastDropSuccessful;

    public boolean hasDropResult() {
        return hasDropResult;
    }

    public boolean wasLastDropSuccessful() {
        return lastDropSuccessful;
    }

    public String getStatusMessage() {
        if (!hasDropResult) {
            return "";
        }
        return lastDropSuccessful ? "Ball dropped" : "Click inside the terrain to drop a ball";
    }

    void setLastDropSuccessful(boolean successful) {
        this.hasDropResult = true;
        this.lastDropSuccessful = successful;
    }
}
