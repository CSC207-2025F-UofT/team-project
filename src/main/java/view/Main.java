package view;

import data_access.SportsAPIDataAccess;

public class Main {
    public static void main(String[] args) {
        SportsAPIDataAccess data = new SportsAPIDataAccess();
        User user = new User("BillChen", 1525.00, 15, 8);
        new MainMenuFrame(user);
    }
}
