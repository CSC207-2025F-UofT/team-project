package use_case.save;

public interface SaveDataAccessInterface {
    boolean hasSave(String username);

    int getDeepestFloor(String username);

    void saveDeepestFloor(String username, int floor);
}
