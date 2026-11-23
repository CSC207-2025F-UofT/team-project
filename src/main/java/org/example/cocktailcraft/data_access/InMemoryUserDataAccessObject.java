package org.example.cocktailcraft.data_access;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.example.cocktailcraft.entity.User;
import org.example.cocktailcraft.use_case.signup.SignupDataAccessInterface;
import org.example.cocktailcraft.use_case.login.LoginDataAccessInterface;

public class InMemoryUserDataAccessObject
        implements SignupDataAccessInterface, LoginDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();
    private final Map<String, String> passwordHashes = new HashMap<>();

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void createUser(String username, String password) {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, username);

        String hashedPassword = PasswordHasher.hash(password);

        users.put(username, user);
        passwordHashes.put(username, hashedPassword);
    }
    @Override
    public boolean validateCredentials(String username, String password) {
        if (!users.containsKey(username)) {
            return false;
        }

        return PasswordHasher.verify(
                password,
                passwordHashes.get(username)
        );
    }
}