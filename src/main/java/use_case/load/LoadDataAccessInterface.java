package use_case.load;

public interface LoadDataAccessInterface {
    boolean hasSave(String username);

    int getDeepestFloor(String username);
}