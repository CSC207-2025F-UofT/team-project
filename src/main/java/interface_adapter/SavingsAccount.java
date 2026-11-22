//package interface_adapter;
//
//import entity.Account;
//import entity.AccountType;
//import entity.Category;
//import entity.Income;
//
//import java.io.IOException;
//import java.time.LocalDate;
//
//public class SavingsAccount extends Account {
//
//  private double interestRate;
//
//  public SavingsAccount(String accountName, AccountType accountType, double balance, String defaultCurrency, String password, int mainProfile) throws IOException, IllegalArgumentException {
//    super(accountName, accountType, balance, defaultCurrency, password, mainProfile);
//    this.interestRate = 0;
//  }
//
//  public double getInterestRate() {return interestRate;}
//
//  public void setInterestRate(double newInterest) throws IOException {
//    if (newInterest >= 0) {
//      this.interestRate = newInterest / 100;
//      String database = "AccountData.json";
//      AccountCreationControl interestUpdater = new AccountCreationControl(database);
//      interestUpdater.setInterestRate(this.getAccountID(), this.interestRate);
//    } else {
//      throw new IllegalArgumentException("The interest rate cannot be below 0%!");
//    }
//  }
//
//  public void accrueInterest(LocalDate date, String description, Category category, String note) throws IOException {
//    Income interestIncome = new Income(this.getBalance() * interestRate, date, description, this, this.getCurrency(), category, note);
//}
//}
