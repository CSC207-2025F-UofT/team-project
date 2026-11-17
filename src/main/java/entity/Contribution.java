package entity;

import java.time.LocalDate;

/**
 * Money a member has put aside for a trip, logged before the trip starts. Amounts are in the trip's
 * home currency.
 */
public class Contribution {

  private final String id;
  private final String tripId;
  private final String memberUsername;
  private final double amount;
  private final LocalDate date;

  /**
   * Creates a contribution.
   *
   * @param id unique id of the contribution
   * @param tripId the trip being saved for
   * @param memberUsername the member who saved the money
   * @param amount how much was saved, in the trip's home currency
   * @param date when it was saved
   * @throws IllegalArgumentException if any field is missing or invalid
   */
  public Contribution(
      String id, String tripId, String memberUsername, double amount, LocalDate date) {
    this.id = Validation.requireNonBlank(id, "Contribution id");
    this.tripId = Validation.requireNonBlank(tripId, "Trip id");
    this.memberUsername = Validation.requireNonBlank(memberUsername, "Member");
    this.amount = Validation.requirePositive(amount, "Amount");
    this.date = Validation.requireNonNull(date, "Date");
  }

  public String getId() {
    return id;
  }

  public String getTripId() {
    return tripId;
  }

  public String getMemberUsername() {
    return memberUsername;
  }

  public double getAmount() {
    return amount;
  }

  public LocalDate getDate() {
    return date;
  }
}
