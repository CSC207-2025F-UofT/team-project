
public class UserCreation {
    private String username;
    private String password;
    private String email;
    public int id = 0; // Not sure to do with this counter still..

    public UserCreation(String username, String password, String email) { // Constructor
        this.username = username;
        this.password = password;
        this.email = email;

        if (verifiedPass()) {
            // Still need to add to database below
            User newUser = new User(this.username, this.password, this.email);
            id += 1; // Should I even do this?
        }
    }

    // Username MUST be unique, greater than or equal to 4 characters
    private String userVerification() {
        // if username is type Null or length is less than or equal to 3
        if (username == null || username.length() <= 3) {
            return "Username must be at least 4 characters";
        }
        return "Success!";
    }

    // Password needs one uppercase, one lowercase letter,
    // one number, and one special character. Min 6 characters
    private String passwordVerification() {
        if (password == null || password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        return "Success!";
    }

    // Needs to be mail.utoronto.ca, one email per account...
    private String emailVerification() {
        if (email == null || !email.endsWith("@mail.utoronto.ca")) {
            return "Email address must be valid @mail.utoronto.ca";
        }
        return "Success!";
    }

    private boolean verifiedPass() {
        if (userVerification().equals("Success!") &&
                passwordVerification().equals("Success!") &&
                emailVerification().equals("Success!")) {
            return true; // User has passed all username, password, and email verifications
        }
        return false;
    }

}
