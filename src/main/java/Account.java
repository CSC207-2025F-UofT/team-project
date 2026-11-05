import java.io.IOException;
import java.time.LocalDateTime;
import java.net.InetAddress;
import java.util.Locale;


public class Account {

    // Attributes
    private final int accountID;
    private String accountName;
    private AccountType accountType;
    private double balance;
    private String currency;
    private final LocalDateTime createdAt;
    private final String createdIPAddress;
    private String password;
    private final String createdCountry;

    // Constructor
    public Account(String accountName,
                   AccountType accountType,
                   double balance, String currency, String password) throws IOException, IllegalArgumentException {

      String userDatabase = "UserData.json";
      UsernameControl allUsers = new UsernameControl(userDatabase);

      if (accountName == null || accountName.isBlank() || password == null || password.isBlank()) {
        throw new IllegalArgumentException(
            "Account name or password cannot be empty!"
        );
      } else if (allUsers.usernameTaken(accountName)) {
        throw new IllegalArgumentException(
            "Account name has already been taken!"
        );
      } else if (!accountName.matches("[\\\\x21-\\\\x7E]+") || !password.matches("[\\\\x21-\\\\x7E]+")) {
        throw new IllegalArgumentException(
            "Account name or password contains illegal characters, please restrict usage to only English letters, numbers, and symbols."
        );
      } else if (accountName.equals(password)) {
        throw new IllegalArgumentException(
            "Account name can not be identical to the password!"
        );
      } else if (balance < 0) {
        throw new IllegalArgumentException(
            "Initial balance cannot be less than 0" + currency + "!"
        );} else {

        this.accountName = accountName;
        allUsers.addUsername(accountName);

        UserIDControl allIDs = new UserIDControl(userDatabase);
        this.accountID = allIDs.nextID();
        allIDs.addUserID();

        this.password = password;
        PasswordControl allPasswords = new PasswordControl(userDatabase);
        allPasswords.addPassword(password);

        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;

        this.createdIPAddress = InetAddress.getLocalHost().getHostAddress();
        this.createdCountry = Locale.getDefault().getDisplayCountry();
        this.createdAt = LocalDateTime.now();
        AccountCreationControl allCreation = new AccountCreationControl(userDatabase);
        allCreation.addData(this.createdCountry, this.createdIPAddress, this.createdAt);

      }
    }

    // Getters
    public int getAccountID() {
        return accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
      return password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getIPAddress() {
      return createdIPAddress;}

    public String getCreatedCountry() {
      return createdCountry;
    }

    // Setters
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Balance operations
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        } else if (amount > balance) {
            throw new IllegalArgumentException(
                    "Cannot withdraw amount greater than existing balance!"
            );
        } else {
        balance -= amount;
        }
    }
}
