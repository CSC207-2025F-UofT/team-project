package view;

public class User {
    private String username;
    private double balance;
    private int bets;
    private int gamesPlayed;
    private String password;

    public User(String username, double balance, int bets, int gamesPlayed, String password) {
        this.username = username;
        this.balance = balance;
        this.bets = bets;
        this.gamesPlayed = gamesPlayed;
        this.password = password;
    }

    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public int getBets() { return bets; }
    public int getGamesPlayed() { return gamesPlayed; }
    public String getPassword () { return password; }
}
