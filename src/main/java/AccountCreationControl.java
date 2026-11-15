import org.json.JSONArray;
import java.io.IOException;
import java.time.LocalDateTime;

public class AccountCreationControl extends JSONControl {

  public AccountCreationControl(String file) throws IOException {
    super(file);
  }

  public JSONArray returnCreatedCountry() {
    return getContents().getJSONArray("createdCountry");
  }

  public JSONArray returnCreatedIP() {
    return getContents().getJSONArray("createdIP");
  }

  public JSONArray returnCreatedAt() {
    return getContents().getJSONArray("createdAt");
  }

  public JSONArray returnProfile() {
    return getContents().getJSONArray("mainProfile");
  }

  public void addData(String country, String IP, LocalDateTime time, int profile) throws IOException{
    returnCreatedCountry().put(country);
    returnCreatedIP().put(IP);
    returnCreatedAt().put(time.toString());
    returnProfile().put(profile);
    writeContents();
  }

  public void changeMainProfile(int accountID, int newMain) throws IOException {
    JSONArray profiles = returnProfile();
    profiles.put(accountID, newMain);
    writeContents();
  }
}
