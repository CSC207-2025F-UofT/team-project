package use_case.support;

import entity.User;
import use_case.complete_quest.CompleteQuestUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/** Test stand-in for Lucy's user storage. */
public class FakeUserDataAccess implements CompleteQuestUserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }
}
