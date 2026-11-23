package data_access;

import entity.User;
import use_case.user.UserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDataAccessObject implements UserDataAccessInterface{
    private final Map<String, User> users = new HashMap<>();

    @Override
    public boolean existsByName(String username){
        return users.containsKey(username);
    }

    @Override
    public void save(User user){
        users.put(user.getUsername(), user);
    }

    @Override
    public User get(String username){
        return users.get(username);
    }
}
