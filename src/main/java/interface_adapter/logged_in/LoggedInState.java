package interface_adapter.logged_in;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";

    private String password = "";
    private String from = "";
    private String to = "";
    private String day = "";
    private String passwordError;
    private String month = "January";
    private String year = "2025";

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
        from = copy.from;
        to = copy.to;
        day = copy.day;
        month = copy.month;
        year = copy.year;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setFrom(String from) {this.from = from;}

    public String getFrom() {return from;}

    public void setTo(String to) {this.to = to;}

    public String getTo() {return to;}

    public void setDay(String day) {this.day = day;}

    public String getDay() {return day;}

    public void setMonth(String month) {this.month = month;}

    public String getMonth() {return month;}

    public void setYear(String year) {this.year = year;}

    public String getYear() {return year;}
}
