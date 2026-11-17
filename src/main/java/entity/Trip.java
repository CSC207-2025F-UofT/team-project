package entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A trip that a group of users plans and budgets for together. The user who creates the trip is its
 * owner and its first member.
 */
public class Trip {

  private final String id;
  private final String name;
  private final String destination;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final String homeCurrency;
  private final String ownerUsername;
  private double dailyBudget;
  private final List<String> memberUsernames = new ArrayList<>();
  private final Map<String, Double> savingsGoals = new LinkedHashMap<>();

  /**
   * Creates a trip with its owner as the only member.
   *
   * @param id unique id of the trip
   * @param name the trip name, for example "Tokyo reading week"
   * @param destination where the group is going
   * @param startDate first day of the trip
   * @param endDate last day of the trip (can equal the start date)
   * @param homeCurrency ISO 4217 code all totals are reported in, for example "CAD"
   * @param dailyBudget how much the group plans to spend per day, in the home currency
   * @param ownerUsername the user who created the trip
   * @throws IllegalArgumentException if any field is missing or invalid
   */
  public Trip(
      String id,
      String name,
      String destination,
      LocalDate startDate,
      LocalDate endDate,
      String homeCurrency,
      double dailyBudget,
      String ownerUsername) {
    this.id = Validation.requireNonBlank(id, "Trip id");
    this.name = Validation.requireNonBlank(name, "Trip name");
    this.destination = Validation.requireNonBlank(destination, "Destination");
    this.startDate = Validation.requireNonNull(startDate, "Start date");
    this.endDate = Validation.requireNonNull(endDate, "End date");
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("End date cannot be before start date");
    }
    this.homeCurrency = Validation.requireCurrencyCode(homeCurrency);
    this.dailyBudget = Validation.requireNonNegative(dailyBudget, "Daily budget");
    this.ownerUsername = Validation.requireNonBlank(ownerUsername, "Owner");
    memberUsernames.add(this.ownerUsername);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDestination() {
    return destination;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public String getHomeCurrency() {
    return homeCurrency;
  }

  public String getOwnerUsername() {
    return ownerUsername;
  }

  public double getDailyBudget() {
    return dailyBudget;
  }

  /**
   * Changes the daily budget.
   *
   * @param dailyBudget the new daily budget in the home currency
   * @throws IllegalArgumentException if the budget is negative
   */
  public void setDailyBudget(double dailyBudget) {
    this.dailyBudget = Validation.requireNonNegative(dailyBudget, "Daily budget");
  }

  /**
   * Adds a member to the trip.
   *
   * @param username the user to add
   * @return true if the user was added, false if they were already a member
   */
  public boolean addMember(String username) {
    final String cleaned = Validation.requireNonBlank(username, "Username");
    if (memberUsernames.contains(cleaned)) {
      return false;
    }
    memberUsernames.add(cleaned);
    return true;
  }

  /**
   * Returns whether the user is a member of this trip.
   *
   * @param username the user to check
   * @return true if the user is a member
   */
  public boolean hasMember(String username) {
    return memberUsernames.contains(username);
  }

  /**
   * Returns the members in the order they joined, owner first.
   *
   * @return a read-only list of usernames
   */
  public List<String> getMemberUsernames() {
    return Collections.unmodifiableList(memberUsernames);
  }

  /**
   * Returns whether the date falls within the trip (inclusive).
   *
   * @param date the date to check
   * @return true if the date is between the start and end dates
   */
  public boolean containsDate(LocalDate date) {
    return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
  }

  /**
   * Returns how many days the trip lasts, counting both the first and last day.
   *
   * @return the number of days
   */
  public int getNumberOfDays() {
    return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
  }

  /**
   * Returns every date of the trip in order.
   *
   * @return the list of dates from start to end
   */
  public List<LocalDate> getDates() {
    final List<LocalDate> dates = new ArrayList<>();
    for (LocalDate day = startDate; !day.isAfter(endDate); day = day.plusDays(1)) {
      dates.add(day);
    }
    return dates;
  }

  /**
   * Returns the total budget for the whole trip.
   *
   * @return the daily budget times the number of days
   */
  public double getTotalBudget() {
    return dailyBudget * getNumberOfDays();
  }

  /**
   * Sets a member's personal savings goal for the trip.
   *
   * @param username the member
   * @param amount the goal in the home currency
   * @throws IllegalArgumentException if the user is not a member or the amount is not positive
   */
  public void setSavingsGoal(String username, double amount) {
    if (!hasMember(username)) {
      throw new IllegalArgumentException(username + " is not a member of this trip");
    }
    savingsGoals.put(username, Validation.requirePositive(amount, "Savings goal"));
  }

  /**
   * Returns whether the member has set a savings goal.
   *
   * @param username the member
   * @return true if a goal is set
   */
  public boolean hasSavingsGoal(String username) {
    return savingsGoals.containsKey(username);
  }

  /**
   * Returns a member's savings goal.
   *
   * @param username the member
   * @return the goal, or 0 if the member has not set one
   */
  public double getSavingsGoal(String username) {
    return savingsGoals.getOrDefault(username, 0.0);
  }

  /**
   * Returns every savings goal that has been set.
   *
   * @return a read-only map from username to goal
   */
  public Map<String, Double> getSavingsGoals() {
    return Collections.unmodifiableMap(savingsGoals);
  }

  /**
   * Returns the sum of every member's savings goal.
   *
   * @return the group's total savings goal
   */
  public double getTotalSavingsGoal() {
    double total = 0;
    for (double goal : savingsGoals.values()) {
      total += goal;
    }
    return total;
  }
}
