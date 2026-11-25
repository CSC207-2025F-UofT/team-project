package use_case.load;

public class LoadOutputData {
    private final String username;
    private final int floor;

    public LoadOutputData(String username, int floor) {
        this.username = username;
        this.floor = floor;
    }

    public String getUsername() {
        return username;
    }

    public int getFloor() {
        return floor;
    }
}