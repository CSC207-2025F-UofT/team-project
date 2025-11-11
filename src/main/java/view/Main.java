package view;

public class Main {
    public static void main(String[] args) {
        User user = new User("BillChen", 1525.00, 15, 8);
        new MainMenuFrame(user);
    }
}
