package entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private String password;
    private final List<String> friendsUsernames = new ArrayList<>();
    private final List<String> incomingFriendRequests = new ArrayList<>();
    private final List<String> outgoingFriendRequests = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<String> getFriendsUsernames() {
        return friendsUsernames;
    }

    public List<String> getIncomingFriendRequests(){return incomingFriendRequests; }

    public List<String> getOutgoingFriendRequests(){return outgoingFriendRequests; }

    public void addFriend(String friendUsername) {
        if (!friendsUsernames.contains(friendUsername)) {
            friendsUsernames.add(friendUsername);
        }
    }

    public void addIncomingRequest(String fromUsername){
        if (!incomingFriendRequests.contains(fromUsername)){
            incomingFriendRequests.add(fromUsername);
        }
    }

    public void removeIncomingRequest(String fromUsername){incomingFriendRequests.remove(fromUsername); }

    public void addOutgoingRequest(String toUsername){
        if (!outgoingFriendRequests.contains(toUsername)){
            outgoingFriendRequests.add(toUsername);
        }
    }

    public void removeOutgoingRequest(String toUsername){outgoingFriendRequests.remove(toUsername); }
}
