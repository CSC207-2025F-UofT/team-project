package entity;

public class UserFactory {
	public User create(String username, String password, String userType) {
	    return new User(username, password, userType);
	}
}
