package app;

import interface_adapter.AccountIDControl;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    AccountIDControl allIDs = new AccountIDControl("AccountData.json");
    System.out.println(allIDs.nextID());
    allIDs.addUserID();
  }
}
