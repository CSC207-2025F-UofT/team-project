package use_case.save;

public class SaveInputData {
    private final String username;
    private final int floor;

    public SaveInputData(String username, int floor) {
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
