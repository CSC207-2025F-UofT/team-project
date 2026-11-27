package entity;

/**
 * Factory for creating player accounts.
 */
public class PlayerDataFactory {

    /**
     * Creates a new player account with no games played.
     *
     * @param id the username
     * @param password the password
     * @return the new account
     */
    public playerData create(String id, String password) {
        return new playerData(id, password);
    }
}