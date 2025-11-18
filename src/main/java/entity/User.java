package entity;

public class User {
    private final String username;
    private double balance;
    private int totalBets;
    private int gamesPlayed;
    private final String passwordHash;

    public User(String username, double balance, int totalBets, int gamesPlayed, String passwordHash) {
        this.username = username;
        this.balance = balance;
        this.totalBets = totalBets;
        this.gamesPlayed = gamesPlayed;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public int getTotalBets() { return totalBets; }
    public int getGamesPlayed() { return gamesPlayed; }
    public String getPasswordHash() { return passwordHash; }

    // Optional business logic
    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void addBet() {
        this.totalBets++;
    }

    public void addGamePlayed() {
        this.gamesPlayed++;
    }
}
