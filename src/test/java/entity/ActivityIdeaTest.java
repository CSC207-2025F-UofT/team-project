package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActivityIdeaTest {

  private static final LocalDate DAY = LocalDate.of(2027, 2, 14);
  private static final LocalDateTime NOON = LocalDateTime.of(2026, 11, 16, 12, 0);

  private ActivityIdea idea(String id, LocalDateTime createdAt) {
    return new ActivityIdea(id, "t1", DAY, "teamLab " + id, 40, "emily", createdAt);
  }

  @Test
  void storesFields() {
    final ActivityIdea idea = idea("a1", NOON);
    assertEquals("a1", idea.getId());
    assertEquals("t1", idea.getTripId());
    assertEquals(DAY, idea.getDay());
    assertEquals("teamLab a1", idea.getTitle());
    assertEquals(40, idea.getEstimatedCost());
    assertEquals("emily", idea.getProposedBy());
    assertEquals(NOON, idea.getCreatedAt());
    assertEquals(0, idea.getVoteCount());
  }

  @Test
  void rejectsInvalidValues() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ActivityIdea("a1", "t1", DAY, "", 0, "emily", NOON));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ActivityIdea("a1", "t1", DAY, "Museum", -1, "emily", NOON));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ActivityIdea("a1", "t1", null, "Museum", 0, "emily", NOON));
  }

  @Test
  void eachMemberVotesOnce() {
    final ActivityIdea idea = idea("a1", NOON);
    assertTrue(idea.addVote("emily"));
    assertFalse(idea.addVote("emily"));
    assertTrue(idea.hasVoted("emily"));
    assertEquals(1, idea.getVoteCount());
    assertTrue(idea.removeVote("emily"));
    assertFalse(idea.removeVote("emily"));
    assertEquals(0, idea.getVoteCount());
  }

  @Test
  void toggleVoteAddsThenRemoves() {
    final ActivityIdea idea = idea("a1", NOON);
    assertTrue(idea.toggleVote("jadyn"));
    assertTrue(idea.hasVoted("jadyn"));
    assertFalse(idea.toggleVote("jadyn"));
    assertFalse(idea.hasVoted("jadyn"));
  }

  @Test
  void constructorWithVotersLoadsVotes() {
    final ActivityIdea idea =
        new ActivityIdea(
            "a1", "t1", DAY, "Museum", 0, "emily", NOON, List.of("emily", "tamima", "emily"));
    assertEquals(Set.of("emily", "tamima"), idea.getVoterUsernames());
    assertThrows(UnsupportedOperationException.class, () -> idea.getVoterUsernames().add("x"));
  }

  @Test
  void rankingIsByVotesThenEarliestProposed() {
    final ActivityIdea early = idea("early", NOON);
    final ActivityIdea late = idea("late", NOON.plusHours(1));
    final ActivityIdea popular = idea("popular", NOON.plusHours(2));
    popular.addVote("a");
    popular.addVote("b");
    early.addVote("a");
    late.addVote("b");

    final List<ActivityIdea> ideas = new ArrayList<>(List.of(late, early, popular));
    ideas.sort(ActivityIdea.BY_RANK);

    assertEquals(List.of(popular, early, late), ideas);
  }
}
