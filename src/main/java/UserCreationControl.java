import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDateTime;

public class UserCreationControl extends AccountCreationControl {
  public UserCreationControl(String file) throws IOException {
    super(file);
  }

  public JSONArray returnEmail() {
    return getContents().getJSONArray("emails");
  }

  public JSONArray returnAccounts() {
    return getContents().getJSONArray("accounts");
  }

  public void addData(String country, String IP, LocalDateTime time, String email, JSONArray accounts) throws IOException{
    returnCreatedCountry().put(country);
    returnCreatedIP().put(IP);
    returnCreatedAt().put(time.toString());
    returnAccounts().put(accounts);
    returnEmail().put(email);
    writeContents();
  }

public void addAccount(int userID, JSONArray accounts) throws IOException {
    returnAccounts().put(userID, accounts);
    writeContents();
}}
