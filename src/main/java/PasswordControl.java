import org.json.JSONArray;
import java.io.IOException;

public class PasswordControl extends JSONControl {

  public PasswordControl(String file) throws IOException {
    super(file);
  }

  public JSONArray returnPasswords() {
    return getContents().getJSONArray("passwords");
  }

  public void addPassword(String password) throws IOException{
    returnPasswords().put(password);
    writeContents();
  }
}

