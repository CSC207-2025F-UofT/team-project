package data_access;

import entity.ActivityIdea;
import entity.Contribution;
import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Converts entities to and from JSON. Kept separate from the file DAO so that the DAO only deals
 * with reading and writing, and this class only deals with format.
 */
public final class TripJsonMapper {

  private static final String ID = "id";
  private static final String TRIP_ID = "tripId";
  private static final String AMOUNT = "amount";
  private static final String DATE = "date";

  private TripJsonMapper() {}

  /**
   * Converts a trip to JSON.
   *
   * @param trip the trip
   * @return its JSON form
   */
  public static JSONObject tripToJson(Trip trip) {
    final JSONObject goals = new JSONObject();
    for (Map.Entry<String, Double> entry : trip.getSavingsGoals().entrySet()) {
      goals.put(entry.getKey(), entry.getValue());
    }
    return new JSONObject()
        .put(ID, trip.getId())
        .put("name", trip.getName())
        .put("destination", trip.getDestination())
        .put("startDate", trip.getStartDate().toString())
        .put("endDate", trip.getEndDate().toString())
        .put("homeCurrency", trip.getHomeCurrency())
        .put("dailyBudget", trip.getDailyBudget())
        .put("owner", trip.getOwnerUsername())
        .put("members", new JSONArray(trip.getMemberUsernames()))
        .put("savingsGoals", goals);
  }

  /**
   * Rebuilds a trip from JSON.
   *
   * @param json the JSON form
   * @return the trip
   */
  public static Trip tripFromJson(JSONObject json) {
    final Trip trip =
        new Trip(
            json.getString(ID),
            json.getString("name"),
            json.getString("destination"),
            LocalDate.parse(json.getString("startDate")),
            LocalDate.parse(json.getString("endDate")),
            json.getString("homeCurrency"),
            json.getDouble("dailyBudget"),
            json.getString("owner"));
    for (String member : stringList(json.getJSONArray("members"))) {
      trip.addMember(member);
    }
    final JSONObject goals = json.getJSONObject("savingsGoals");
    for (String member : goals.keySet()) {
      trip.setSavingsGoal(member, goals.getDouble(member));
    }
    return trip;
  }

  /**
   * Converts an expense to JSON.
   *
   * @param expense the expense
   * @return its JSON form
   */
  public static JSONObject expenseToJson(Expense expense) {
    return new JSONObject()
        .put(ID, expense.getId())
        .put(TRIP_ID, expense.getTripId())
        .put("payer", expense.getPayerUsername())
        .put("description", expense.getDescription())
        .put(AMOUNT, expense.getAmount())
        .put("currency", expense.getCurrency())
        .put("amountInHomeCurrency", expense.getAmountInHomeCurrency())
        .put("category", expense.getCategory().name())
        .put(DATE, expense.getDate().toString());
  }

  /**
   * Rebuilds an expense from JSON.
   *
   * @param json the JSON form
   * @return the expense
   */
  public static Expense expenseFromJson(JSONObject json) {
    return new Expense(
        json.getString(ID),
        json.getString(TRIP_ID),
        json.getString("payer"),
        json.getString("description"),
        json.getDouble(AMOUNT),
        json.getString("currency"),
        json.getDouble("amountInHomeCurrency"),
        ExpenseCategory.valueOf(json.getString("category")),
        LocalDate.parse(json.getString(DATE)));
  }

  /**
   * Converts a contribution to JSON.
   *
   * @param contribution the contribution
   * @return its JSON form
   */
  public static JSONObject contributionToJson(Contribution contribution) {
    return new JSONObject()
        .put(ID, contribution.getId())
        .put(TRIP_ID, contribution.getTripId())
        .put("member", contribution.getMemberUsername())
        .put(AMOUNT, contribution.getAmount())
        .put(DATE, contribution.getDate().toString());
  }

  /**
   * Rebuilds a contribution from JSON.
   *
   * @param json the JSON form
   * @return the contribution
   */
  public static Contribution contributionFromJson(JSONObject json) {
    return new Contribution(
        json.getString(ID),
        json.getString(TRIP_ID),
        json.getString("member"),
        json.getDouble(AMOUNT),
        LocalDate.parse(json.getString(DATE)));
  }

  /**
   * Converts an activity idea to JSON.
   *
   * @param idea the idea
   * @return its JSON form
   */
  public static JSONObject activityIdeaToJson(ActivityIdea idea) {
    return new JSONObject()
        .put(ID, idea.getId())
        .put(TRIP_ID, idea.getTripId())
        .put("day", idea.getDay().toString())
        .put("title", idea.getTitle())
        .put("estimatedCost", idea.getEstimatedCost())
        .put("proposedBy", idea.getProposedBy())
        .put("createdAt", idea.getCreatedAt().toString())
        .put("voters", new JSONArray(idea.getVoterUsernames()));
  }

  /**
   * Rebuilds an activity idea from JSON.
   *
   * @param json the JSON form
   * @return the idea
   */
  public static ActivityIdea activityIdeaFromJson(JSONObject json) {
    return new ActivityIdea(
        json.getString(ID),
        json.getString(TRIP_ID),
        LocalDate.parse(json.getString("day")),
        json.getString("title"),
        json.getDouble("estimatedCost"),
        json.getString("proposedBy"),
        LocalDateTime.parse(json.getString("createdAt")),
        stringList(json.getJSONArray("voters")));
  }

  private static List<String> stringList(JSONArray array) {
    final List<String> result = new ArrayList<>();
    for (int i = 0; i < array.length(); i++) {
      result.add(array.getString(i));
    }
    return result;
  }
}
