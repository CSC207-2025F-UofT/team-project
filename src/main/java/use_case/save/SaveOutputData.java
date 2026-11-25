package use_case.save;

public class SaveOutputData {
    private final String username;
    private final int floor;

    public SaveOutputData(String username, int floor) {
        this.username = username;
        this.floor = floor;
    }

    public String getUsername() {
        return username;
    }

    public int getDeepestFloor() {
        return floor;
    }
}
