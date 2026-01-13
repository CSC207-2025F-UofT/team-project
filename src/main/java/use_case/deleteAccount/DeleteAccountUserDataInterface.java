package use_case.deleteAccount;

public interface DeleteAccountUserDataInterface {
    boolean existsByName(String username);
    void deleteByName(String username);
}
