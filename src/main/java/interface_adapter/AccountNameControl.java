package interface_adapter;

import org.json.JSONArray;
import java.io.IOException;

public class AccountNameControl extends JSONControl {

  public AccountNameControl(String file) throws IOException {
    super(file);
  }

  public JSONArray returnUsernames() {
    return getContents().getJSONArray("usernames");
  }

  public boolean usernameTaken(String newUser) {
    return returnUsernames().toList().contains(newUser);
  }

  public void addUsername(String username) throws IOException{
    returnUsernames().put(username);
    writeContents();
  }

  public void changeUsername(int UserID, String newUsername)  throws IOException {
    returnUsernames().put(UserID, newUsername);
    writeContents();
  }
}
