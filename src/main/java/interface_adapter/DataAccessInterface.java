package interface_adapter;

public interface DataAccessInterface {
    boolean existUserName(String username);
    void save();
    String getPassword(String username);
}
