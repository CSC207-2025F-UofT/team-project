package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

/** An activity a member suggests for one day of a trip. Each member can vote for it once. */
public class ActivityIdea {

  /** Orders ideas from most to fewest votes. Ties go to the idea proposed first. */
  public static final Comparator<ActivityIdea> BY_RANK =
      Comparator.comparingInt(ActivityIdea::getVoteCount)
          .reversed()
          .thenComparing(ActivityIdea::getCreatedAt);

  private final String id;
  private final String tripId;
  private final LocalDate day;
  private final String title;
  private final double estimatedCost;
  private final String proposedBy;
  private final LocalDateTime createdAt;
  private final Set<String> voterUsernames = new LinkedHashSet<>();

  /**
   * Creates an activity idea with no votes.
   *
   * @param id unique id of the idea
   * @param tripId the trip this idea belongs to
   * @param day the day of the trip it is proposed for
   * @param title short name of the activity
   * @param estimatedCost expected cost in the trip's home currency (0 if unknown or free)
   * @param proposedBy the member who suggested it
   * @param createdAt when it was suggested, used to break ties
   * @throws IllegalArgumentException if any field is missing or invalid
   */
  public ActivityIdea(
      String id,
      String tripId,
      LocalDate day,
      String title,
      double estimatedCost,
      String proposedBy,
      LocalDateTime createdAt) {
    this.id = Validation.requireNonBlank(id, "Activity id");
    this.tripId = Validation.requireNonBlank(tripId, "Trip id");
    this.day = Validation.requireNonNull(day, "Day");
    this.title = Validation.requireNonBlank(title, "Title");
    this.estimatedCost = Validation.requireNonNegative(estimatedCost, "Estimated cost");
    this.proposedBy = Validation.requireNonBlank(proposedBy, "Proposed by");
    this.createdAt = Validation.requireNonNull(createdAt, "Created at");
  }

  /**
   * Creates an activity idea with existing votes, for example when loading saved data.
   *
   * @param id unique id of the idea
   * @param tripId the trip this idea belongs to
   * @param day the day of the trip it is proposed for
   * @param title short name of the activity
   * @param estimatedCost expected cost in the trip's home currency
   * @param proposedBy the member who suggested it
   * @param createdAt when it was suggested
   * @param voters members who have voted for it
   */
  public ActivityIdea(
      String id,
      String tripId,
      LocalDate day,
      String title,
      double estimatedCost,
      String proposedBy,
      LocalDateTime createdAt,
      Collection<String> voters) {
    this(id, tripId, day, title, estimatedCost, proposedBy, createdAt);
    for (String voter : voters) {
      addVote(voter);
    }
  }

  /**
   * Records a vote.
   *
   * @param username the member voting
   * @return true if the vote was added, false if they had already voted
   */
  public boolean addVote(String username) {
    return voterUsernames.add(Validation.requireNonBlank(username, "Username"));
  }

  /**
   * Removes a vote.
   *
   * @param username the member whose vote to remove
   * @return true if a vote was removed, false if they had not voted
   */
  public boolean removeVote(String username) {
    return voterUsernames.remove(username);
  }

  /**
   * Adds the member's vote if they have not voted, otherwise removes it.
   *
   * @param username the member clicking vote
   * @return true if the member now has a vote on this idea
   */
  public boolean toggleVote(String username) {
    if (hasVoted(username)) {
      removeVote(username);
      return false;
    }
    addVote(username);
    return true;
  }

  /**
   * Returns whether the member has voted for this idea.
   *
   * @param username the member
   * @return true if they have voted
   */
  public boolean hasVoted(String username) {
    return voterUsernames.contains(username);
  }

  public int getVoteCount() {
    return voterUsernames.size();
  }

  /**
   * Returns who has voted, in voting order.
   *
   * @return a read-only set of usernames
   */
  public Set<String> getVoterUsernames() {
    return Collections.unmodifiableSet(voterUsernames);
  }

  public String getId() {
    return id;
  }

  public String getTripId() {
    return tripId;
  }

  public LocalDate getDay() {
    return day;
  }

  public String getTitle() {
    return title;
  }

  public double getEstimatedCost() {
    return estimatedCost;
  }

  public String getProposedBy() {
    return proposedBy;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}
