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

  public void addData(String country, String IP, LocalDateTime time) throws IOException{
    returnCreatedCountry().put(country);
    returnCreatedIP().put(IP);
    returnCreatedAt().put(time.toString());
    writeContents();
  }
}
