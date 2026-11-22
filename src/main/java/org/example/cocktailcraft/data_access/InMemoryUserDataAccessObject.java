package org.example.cocktailcraft.data_access;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.example.cocktailcraft.entity.User;
import org.example.cocktailcraft.use_case.signup.SignupDataAccessInterface;

public class InMemoryUserDataAccessObject implements SignupDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void createUser(String username, String password) {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, username);
        users.put(username, user);
    }
}