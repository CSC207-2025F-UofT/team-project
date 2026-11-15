package use_case.create_study_set;

import java.util.List;

/**
 * The Output Data for the Create Study Set use case.
 */
public class CreateStudySetOutputData {
  private final String studySetId;
  private final String name;
  private final List<Item> studySets;

  /**
   * Creates output data.
   *
   * @param studySetId the id of the new study set
   * @param name       the name of the new study set
   * @param studySets  an entry for each of the user's study sets, including the new one
   */
  public CreateStudySetOutputData(String studySetId, String name, List<Item> studySets) {
    this.studySetId = studySetId;
    this.name = name;
    this.studySets = studySets;
  }

  /**
   * Returns the new study set's id.
   *
   * @return the id of the created study set
   */
  public String getStudySetId() { return studySetId; }

  /**
   * Returns the new study set's name.
   *
   * @return the name of the created study set
   */
  public String getName() { return name; }

  /**
   * Returns all of the user's study sets.
   *
   * @return an entry for each of the user's study sets, including the new one
   */
  public List<Item> getStudySets() { return studySets; }

  /**
   * A summary of one study set: its id, name and number of flashcards.
   */
  public static class Item {
    /** The study set's id. */
    private final String id;
    /** The study set's name. */
    private final String name;
    /** The number of flashcards in the study set. */
    private final int cardCount;

    /**
     * Creates an entry.
     *
     * @param id        the study set's id
     * @param name      the study set's name
     * @param cardCount the number of flashcards it contains
     */
    public Item(String id, String name, int cardCount) {
      this.id = id;
      this.name = name;
      this.cardCount = cardCount;
    }

    /**
     * Returns the id.
     *
     * @return the study set's id
     */
    public String getId() { return id; }

    /**
     * Returns the name.
     *
     * @return the study set's name
     */
    public String getName() { return name; }

    /**
     * Returns the number of flashcards.
     *
     * @return how many flashcards the study set contains
     */
    public int getCardCount() { return cardCount; }
  }
}