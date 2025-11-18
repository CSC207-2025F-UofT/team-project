package entity;

import java.util.ArrayList;

public class User {
    private final String username;
    private double balance;
    private int totalBets;
    private int gamesPlayed;
    private final String passwordHash;
    private ArrayList<Sportbet> sbs = new ArrayList<>();

    public User(String username, double balance, int totalBets, int gamesPlayed, String passwordHash) {
        this.username = username;
        this.balance = balance;
        this.totalBets = totalBets;
        this.gamesPlayed = gamesPlayed;
        this.passwordHash = passwordHash;
        this.sbs = new ArrayList<>();
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

    public void addBet(Sportbet sb) {
        sbs.add(sb);
        this.totalBets++;
    }

    public void viewBets(){
        for(Sportbet s:this.sbs){
            System.out.println(s);
        }
    }

    public void addGamePlayed() {
        this.gamesPlayed++;
    }
}
