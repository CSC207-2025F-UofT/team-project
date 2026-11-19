package entity;

import interface_adapter.*;

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
    private String defaultCurrency;
    private final LocalDateTime createdAt;
    private final String createdIPAddress;
    private String password;
    private final String createdCountry;
    private int mainProfile;

    // Constructor
    public Account(String accountName,
                   AccountType accountType,
                   double balance, String defaultCurrency, String password, int mainProfile) throws IOException, IllegalArgumentException {

      String userDatabase = "AccountData.json";
      String currencyDatabase = "Currencies.json";
      AccountNameControl allUsers = new AccountNameControl(userDatabase);
      CurrencyControl allCurrencies = new CurrencyControl(currencyDatabase);

      if (accountName == null || accountName.isBlank() || password == null || password.isBlank()) {
        throw new IllegalArgumentException(
            "entity.Account name or password cannot be empty!"
        );
      } else if (allUsers.usernameTaken(accountName)) {
        throw new IllegalArgumentException(
            "entity.Account name has already been taken!"
        );
      } else if (!accountName.matches("[\\\\x21-\\\\x7E]+") || !password.matches("[\\\\x21-\\\\x7E]+")) {
        throw new IllegalArgumentException(
            "entity.Account name or password contains illegal characters, please restrict usage to only English letters, numbers, and symbols."
        );
      } else if (accountName.equals(password)) {
        throw new IllegalArgumentException(
            "entity.Account name can not be identical to the password!"
        );
      } else if (balance < 0) {
        throw new IllegalArgumentException(
            "Initial balance cannot be less than 0" + defaultCurrency + "!"
        );
      } else if (!allCurrencies.validCurrency(defaultCurrency)) {
        throw new IllegalArgumentException("The currency type is not supported. Please try again with another currency.");
      } else {
        this.accountName = accountName;
        allUsers.addUsername(accountName);

        AccountIDControl allIDs = new AccountIDControl(userDatabase);
        this.accountID = allIDs.nextID();
        allIDs.addUserID();

        this.password = password;
        PasswordControl allPasswords = new PasswordControl(userDatabase);
        allPasswords.addPassword(password);

        this.accountType = accountType;

        this.balance = balance;
        BalanceControl allBalances = new BalanceControl(userDatabase);
        allBalances.initiateBalance(balance);

        this.defaultCurrency = defaultCurrency;
        CurrencyControl currencySetter = new CurrencyControl(userDatabase);
        currencySetter.addCurrency(defaultCurrency);

        this.createdIPAddress = InetAddress.getLocalHost().getHostAddress();
        this.createdCountry = Locale.getDefault().getDisplayCountry();
        this.createdAt = LocalDateTime.now();
        this.mainProfile = mainProfile;
        AccountCreationControl allCreation = new AccountCreationControl(userDatabase);
        allCreation.addData(this.createdCountry, this.createdIPAddress, this.createdAt, mainProfile);

        String stringID = String.valueOf(this.accountID);
        TransactionControl expense = new TransactionControl("Expenditures.json");

        expense.addAccount(stringID);

        TransactionControl revenue = new TransactionControl("Revenues.json");
        revenue.addAccount(stringID);
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
        return defaultCurrency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getIPAddress() {
      return createdIPAddress;}

    public String getCreatedCountry() {
      return createdCountry;
    }

    public int getMainProfile() {return mainProfile;}

    // Setters
    public void setAccountName(String accountName) throws IOException {
      String userDatabase = "AccountData.json";
      AccountNameControl allUsers = new AccountNameControl(userDatabase);
      if (this.accountName.equals(accountName)) {
        throw new IllegalArgumentException("The new account name must be different from the old.");
      } else if (accountName.equals(password)) {
        throw new IllegalArgumentException("entity.Account name can not be identical to the password!");
      } else if (accountName.isBlank()) {
        throw new IllegalArgumentException("entity.Account name cannot be empty!");
      } else if (allUsers.usernameTaken(accountName)) {
        throw new IllegalArgumentException("entity.Account name has already been taken!");
      } else if (!accountName.matches("[\\\\x21-\\\\x7E]+")) {
        throw new IllegalArgumentException("\"entity.Account name contains illegal characters, please restrict usage to only English letters, numbers, and symbols.");
      } else {
        this.accountName = accountName;
        allUsers.changeUsername(accountID, accountName);
      }
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setPassword(String password) throws IOException {
      String userDatabase = "AccountData.json";
      PasswordControl allPasswords = new PasswordControl(userDatabase);
      if (this.password.equals(password)) {
        throw new IllegalArgumentException("The new password must be different from the old.");
      } else if (accountName.equals(password)) {
        throw new IllegalArgumentException("Password can not be identical to the account name!");
      } else if (password.isBlank()) {
        throw new IllegalArgumentException("password cannot be empty!");
      } else if (!password.matches("[\\\\x21-\\\\x7E]+")) {
        throw new IllegalArgumentException("\"Password contains illegal characters, please restrict usage to only English letters, numbers, and symbols.");
      } else {
        this.password = password;
        allPasswords.changePassword(accountID, password);
      }
    }

    public void setCurrency(String defaultCurrency) throws IOException {
      String currencyDatabase = "Currencies.json";
      String userDatabase = "AccountData.json";
      CurrencyControl allCurrencies = new CurrencyControl(currencyDatabase);
      CurrencyControl currencySetter = new CurrencyControl(userDatabase);

      if (!allCurrencies.validCurrency(defaultCurrency)) {
        throw new IllegalArgumentException("The currency type is not supported. Please try again with another currency.");
      } else {
        balance = allCurrencies.exchange(defaultCurrency, this.defaultCurrency, balance);
        this.defaultCurrency = defaultCurrency;
        currencySetter.changeCurrency(accountID, defaultCurrency);
        BalanceControl allBalances = new BalanceControl(userDatabase);
        allBalances.updateBalance(accountID, balance);
      }
    }

    public void setMainProfile(int newMainProfile) throws IOException {
      this.mainProfile = newMainProfile;
      String userDatabase = "AccountData.json";
      AccountCreationControl allCreation = new AccountCreationControl(userDatabase);
      allCreation.changeMainProfile(this.accountID, newMainProfile);
    }

    // Balance operations
    public void deposit(double amount, String depositCurrency) throws IOException {
      String currencyDatabase = "Currencies.json";
      String userDatabase = "AccountData.json";
      CurrencyControl allCurrencies = new CurrencyControl(currencyDatabase);
      if (amount <= 0) {
          throw new IllegalArgumentException(
              "Amount must be positive");
        } else if (!allCurrencies.validCurrency(depositCurrency)) {
          throw new IllegalArgumentException("The currency type is not supported. Please try again with another currency.");
      } else {
          balance += allCurrencies.exchange(defaultCurrency, depositCurrency, amount);
          BalanceControl allBalances = new BalanceControl(userDatabase);
          allBalances.updateBalance(accountID, balance);
        }
    }

    public void withdraw(double amount, String withdrawCurrency) throws IOException {
      String currencyDatabase = "Currencies.json";
      String userDatabase = "AccountData.json";
      CurrencyControl allCurrencies = new CurrencyControl(currencyDatabase);

      if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );}
      else if (!allCurrencies.validCurrency(withdrawCurrency)) {
        throw new IllegalArgumentException("The currency type is not supported. Please try again with another currency.");
      } else if (balance < allCurrencies.exchange(defaultCurrency, withdrawCurrency, amount)) {
        throw new IllegalArgumentException(
            "Cannot withdraw amount greater than existing balance!"
        );
        } else {
        balance -= allCurrencies.exchange(defaultCurrency, withdrawCurrency, amount);
        BalanceControl allBalances = new BalanceControl(userDatabase);
        allBalances.updateBalance(accountID, balance);
        }
    }
}
