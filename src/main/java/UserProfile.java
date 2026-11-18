import org.json.JSONArray;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserProfile {
    private String name;
    private final int userID;
    private final LocalDateTime createdAt;
    private String email;
    private final String createdCountry;
    private final String createdIPAddress;
    private String password;
    private ArrayList<Integer> accounts;
    private String profileImage;

    public UserProfile(String name,
                       String email, String password) throws IOException {
        String userDatabase = "UserData.json";
        AccountIDControl allIDs = new AccountIDControl(userDatabase);
        this.userID = allIDs.nextID();
        allIDs.addUserID();

        this.name = name;
        AccountNameControl allNames = new AccountNameControl(userDatabase);
        allNames.addUsername(name);

        UserCreationControl allUsers = new UserCreationControl(userDatabase);
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.createdIPAddress = InetAddress.getLocalHost().getHostAddress();
        this.createdCountry = Locale.getDefault().getDisplayCountry();
        this.accounts = new ArrayList<>();
        allUsers.addData(this.createdCountry, this.createdIPAddress, this.createdAt, email, new JSONArray(this.accounts));

        PasswordControl allPasswords = new PasswordControl(userDatabase);
        this.password = password;
        allPasswords.addPassword(password);
        this.profileImage = "src/main/profile_images/default_profile.png";
    }

    public void addAccount (Account newAccount) throws IOException {
      String userDatabase = "UserData.json";
      UserCreationControl allUsers = new UserCreationControl(userDatabase);
      if (accounts.contains(newAccount.getAccountID())) {
        throw new IllegalArgumentException("Account has already been added!");
      } else {
      accounts.add(newAccount.getAccountID());
      newAccount.setMainProfile(this.userID);
      allUsers.addAccount(userID, new JSONArray(this.accounts));
    }}

    public void createAccount (String accountName,
                               AccountType accountType,
                               double balance, String defaultCurrency, String password) throws IOException {
      Account newAccount = new Account(accountName, accountType, balance, defaultCurrency, password, this.userID);
      addAccount(newAccount);
    }

    public double getTotalBalance (String currency) throws IOException {
      String userDatabase = "AccountData.json";
      String currencyDatabase = "Currencies.json";
      double balance = 0;
      BalanceControl balances = new BalanceControl(userDatabase);
      CurrencyControl currencies = new CurrencyControl(currencyDatabase);
      CurrencyControl accountCurrency = new CurrencyControl(userDatabase);
      for (int accountID: this.accounts) {
        if (currency.equals(accountCurrency.returnCurrency(accountID))) {
        balance += balances.returnBalance(accountID);}
        else {
          balance += currencies.exchange(currency,accountCurrency.returnCurrency(accountID), balances.returnBalance(accountID));
        }
      }
      return balance;
    }

    public ImageIcon uploadProfileImage(String imageSource) throws IOException {
      ImageIcon profileImage = new ImageIcon(imageSource);
      Path source = Path.of(imageSource);
      Path destination = Path.of("src/main/profile_images", this.userID + ".png");
      Files.createDirectories(destination.getParent());
      Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
      this.profileImage = String.valueOf(destination);
      UserCreationControl imageUpdator = new UserCreationControl("UserData.json");
      imageUpdator.returnImages().put(this.userID, this.profileImage);
      imageUpdator.writeContents();
      return profileImage;
    }

    public int getNumAccounts() {
      return accounts.size();
    }

    //  Getters
    public int getId() {
        return userID;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {return email; }
    public LocalDateTime getCreatedAt() {return createdAt;}
    public String getCountry() {return createdCountry;}
    public String getCreatedIPAddress() {return createdIPAddress;}
    public List<Integer> getAccounts() {return accounts;}
    public String getPassword() {return password;}

    //  Setters
    public void setName(String name) throws IOException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        } else if (name.equals(this.name)) {
          throw new IllegalArgumentException("The name is the same as the previous!");
        } else {
        this.name = name;
        String userDatabase = "UserData.json";
        AccountNameControl allNames = new AccountNameControl(userDatabase);
        allNames.changeUsername(this.userID, name);
    }}
}