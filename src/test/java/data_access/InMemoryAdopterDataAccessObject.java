package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.Adopter;
import use_case.adopter_login.AdopterLoginDataAccessInterface;
import use_case.adopter_signup.AdopterSignupDataAccessInterface;

/**
 * Keeps adopters in memory only. Used in tests so they don't touch real files.
 */
public class InMemoryAdopterDataAccessObject implements AdopterSignupDataAccessInterface,
        AdopterLoginDataAccessInterface {
    private final Map<String, Adopter> accounts = new HashMap<>();

    @Override
    public boolean existsByUsername(String username) {
        return accounts.containsKey(username);
    }

    @Override
    public Adopter get(String username) {
        return accounts.get(username);
    }

    @Override
    public void save(Adopter adopter) {
        accounts.put(adopter.getUsername(), adopter);
    }
}
