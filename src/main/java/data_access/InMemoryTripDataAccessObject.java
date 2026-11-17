package data_access;

import entity.ActivityIdea;
import entity.Contribution;
import entity.Expense;
import entity.Trip;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps trips and everything logged against them in memory. Use this in unit tests. {@link
 * JsonFileTripDataAccessObject} adds saving to a file on top of it.
 *
 * <p>How to use this from your use case: declare the methods your interactor needs in your own data
 * access interface (in your use_case package), then add {@code implements YourDataAccessInterface}
 * to this class. The methods below already exist, so usually no new code is needed here. If you
 * need a new method, add it here and to {@link JsonFileTripDataAccessObject} only if it changes
 * stored data.
 */
public class InMemoryTripDataAccessObject {

  private final Map<String, Trip> trips = new LinkedHashMap<>();
  private final Map<String, Expense> expenses = new LinkedHashMap<>();
  private final Map<String, Contribution> contributions = new LinkedHashMap<>();
  private final Map<String, ActivityIdea> activityIdeas = new LinkedHashMap<>();

  private String currentTripId;

  // ----- Current trip (same idea as getCurrentUsername() in the user DAO) -----

  /**
   * Records which trip the user has open. Set when a trip is created or opened.
   *
   * @param tripId the open trip's id, or null if no trip is open
   */
  public void setCurrentTripId(String tripId) {
    this.currentTripId = tripId;
  }

  /**
   * Returns the id of the trip the user has open.
   *
   * @return the trip id, or null if no trip is open
   */
  public String getCurrentTripId() {
    return currentTripId;
  }

  // ----- Trips -----

  /**
   * Saves a new trip or replaces the stored trip with the same id.
   *
   * @param trip the trip to save
   */
  public void saveTrip(Trip trip) {
    trips.put(trip.getId(), trip);
    onDataChanged();
  }

  /**
   * Returns the trip with the given id.
   *
   * @param tripId the id to look up
   * @return the trip, or null if there is none
   */
  public Trip getTrip(String tripId) {
    return trips.get(tripId);
  }

  /**
   * Returns whether a trip with the given id exists.
   *
   * @param tripId the id to look up
   * @return true if it exists
   */
  public boolean existsTrip(String tripId) {
    return trips.containsKey(tripId);
  }

  /**
   * Returns every trip the user is a member of, in the order they were created.
   *
   * @param username the user
   * @return the user's trips
   */
  public List<Trip> getTripsForMember(String username) {
    final List<Trip> result = new ArrayList<>();
    for (Trip trip : trips.values()) {
      if (trip.hasMember(username)) {
        result.add(trip);
      }
    }
    return result;
  }

  // ----- Expenses -----

  /**
   * Saves a new expense or replaces the stored expense with the same id.
   *
   * @param expense the expense to save
   */
  public void saveExpense(Expense expense) {
    expenses.put(expense.getId(), expense);
    onDataChanged();
  }

  /**
   * Returns every expense logged for a trip, in the order they were logged.
   *
   * @param tripId the trip
   * @return the trip's expenses
   */
  public List<Expense> getExpenses(String tripId) {
    final List<Expense> result = new ArrayList<>();
    for (Expense expense : expenses.values()) {
      if (expense.getTripId().equals(tripId)) {
        result.add(expense);
      }
    }
    return result;
  }

  // ----- Contributions -----

  /**
   * Saves a new contribution or replaces the stored contribution with the same id.
   *
   * @param contribution the contribution to save
   */
  public void saveContribution(Contribution contribution) {
    contributions.put(contribution.getId(), contribution);
    onDataChanged();
  }

  /**
   * Returns every contribution logged for a trip, in the order they were logged.
   *
   * @param tripId the trip
   * @return the trip's contributions
   */
  public List<Contribution> getContributions(String tripId) {
    final List<Contribution> result = new ArrayList<>();
    for (Contribution contribution : contributions.values()) {
      if (contribution.getTripId().equals(tripId)) {
        result.add(contribution);
      }
    }
    return result;
  }

  // ----- Activity ideas -----

  /**
   * Saves a new activity idea or replaces the stored idea with the same id (for example after its
   * votes change).
   *
   * @param idea the idea to save
   */
  public void saveActivityIdea(ActivityIdea idea) {
    activityIdeas.put(idea.getId(), idea);
    onDataChanged();
  }

  /**
   * Returns the activity idea with the given id.
   *
   * @param ideaId the id to look up
   * @return the idea, or null if there is none
   */
  public ActivityIdea getActivityIdea(String ideaId) {
    return activityIdeas.get(ideaId);
  }

  /**
   * Returns every activity idea for a trip, in the order they were proposed.
   *
   * @param tripId the trip
   * @return the trip's activity ideas
   */
  public List<ActivityIdea> getActivityIdeas(String tripId) {
    final List<ActivityIdea> result = new ArrayList<>();
    for (ActivityIdea idea : activityIdeas.values()) {
      if (idea.getTripId().equals(tripId)) {
        result.add(idea);
      }
    }
    return result;
  }

  // ----- Hooks for subclasses -----

  /**
   * Called after any change to stored data. Does nothing here; {@link JsonFileTripDataAccessObject}
   * overrides it to write the file.
   */
  protected void onDataChanged() {
    // In-memory storage has nothing extra to do.
  }

  /**
   * Returns every stored trip, for subclasses that persist the data.
   *
   * @return all trips
   */
  protected List<Trip> allTrips() {
    return new ArrayList<>(trips.values());
  }

  /**
   * Returns every stored expense, for subclasses that persist the data.
   *
   * @return all expenses
   */
  protected List<Expense> allExpenses() {
    return new ArrayList<>(expenses.values());
  }

  /**
   * Returns every stored contribution, for subclasses that persist the data.
   *
   * @return all contributions
   */
  protected List<Contribution> allContributions() {
    return new ArrayList<>(contributions.values());
  }

  /**
   * Returns every stored activity idea, for subclasses that persist the data.
   *
   * @return all activity ideas
   */
  protected List<ActivityIdea> allActivityIdeas() {
    return new ArrayList<>(activityIdeas.values());
  }

  /**
   * Loads data without triggering a save, for subclasses reading from storage.
   *
   * @param loadedTrips trips to load
   * @param loadedExpenses expenses to load
   * @param loadedContributions contributions to load
   * @param loadedIdeas activity ideas to load
   */
  protected void loadAll(
      List<Trip> loadedTrips,
      List<Expense> loadedExpenses,
      List<Contribution> loadedContributions,
      List<ActivityIdea> loadedIdeas) {
    for (Trip trip : loadedTrips) {
      trips.put(trip.getId(), trip);
    }
    for (Expense expense : loadedExpenses) {
      expenses.put(expense.getId(), expense);
    }
    for (Contribution contribution : loadedContributions) {
      contributions.put(contribution.getId(), contribution);
    }
    for (ActivityIdea idea : loadedIdeas) {
      activityIdeas.put(idea.getId(), idea);
    }
  }
}
