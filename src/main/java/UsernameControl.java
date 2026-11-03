import org.json.JSONArray;
import java.io.IOException;

public class UsernameControl extends JSONControl {

  public UsernameControl(String file) throws IOException {
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
}
