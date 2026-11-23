package data_access;

import java.util.HashMap;
import java.util.Map;

import use_case.get_data.get_dataDataAccessInterface;

public class InMemoryCognitiveDataDataAccessObject implements get_dataDataAccessInterface {



    /**
     *
     TODO THIS IS FROM THE LOGIN LAB AND NEEDS A LOT OF CHANGES
     * In-memory implementation of the DAO for storing user data. This implementation does
     * NOT persist data between runs of the program.


        private final Map<String, User> users = new HashMap<>();

        private String currentUsername;

        @Override
        public boolean existsByName(String identifier) {
            return users.containsKey(identifier);
        }

        @Override
        public void save(User user) {
            users.put(user.getName(), user);
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }

        @Override
        public void setCurrentUsername(String name) {
            currentUsername = name;
        }

        @Override
        public String getCurrentUsername() {
            return currentUsername;
        }

        @Override
        public void changePassword(User user) {
            // Replace the old entry with the new password
            users.put(user.getName(), user);
        }


    */
}
