import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    UserIDControl allIDs = new UserIDControl("UserData.json");
    System.out.println(allIDs.nextID());
    allIDs.addUserID();
  }
}
