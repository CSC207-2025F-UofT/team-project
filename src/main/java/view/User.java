package view;

/**
 * User entity representing a user in the betting system.
 * This is a domain model in the entity layer.
 */
public class User {
    private String username;
    private double balance;
    private int bets;
    private int gamesPlayed;

    public User(String username, double balance, int bets, int gamesPlayed) {
        this.username = username;
        this.balance = balance;
        this.bets = bets;
        this.gamesPlayed = gamesPlayed;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public int getBets() {
        return bets;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setBets(int bets) {
        this.bets = bets;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}

