import org.json.JSONArray;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UserIDControl extends JSONControl {

  public UserIDControl(String file) throws IOException {
    super(file);
  }

  public JSONArray returnUserID() {
    return getContents().getJSONArray("userID");
  }

  public int nextID() throws IOException {
    if (returnUserID().toList().isEmpty()) {
      return 0;
    } else {
      return Collections.max((List<Integer>) (List<?>) returnUserID().toList()) + 1;
    }
  }

  public void addUserID () throws IOException {
    returnUserID().put(nextID());
    writeContents();
  }
}