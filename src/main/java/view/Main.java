package view;

import data_access.SportsAPIDataAccess;
import entity.Sportbet;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SportsAPIDataAccess data = new SportsAPIDataAccess();
        //data.fetchOdds();
        data.readdata();
        ArrayList<Sportbet> test = SportsAPIDataAccess.allbets;
        for(Sportbet s:test){
            System.out.println(s);
        }
        User user = new User("BillChen", 1525.00, 15, 8,"a");
        new MainMenuFrame(user);
        //test_push
    }
}
