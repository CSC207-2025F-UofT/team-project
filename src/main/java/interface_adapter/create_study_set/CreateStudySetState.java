package interface_adapter.create_study_set;

import java.util.ArrayList;
import java.util.List;

/**
 * The data shown by the Create Study Set view.
 */
public class CreateStudySetState {
  private String name = "";
  private String errorMessage = null;
  private String createdStudySetId = null;
  private List<Item> studySets = new ArrayList<>();

  /**
   * Creates a state with an empty name, no error and no study sets.
   */
  public CreateStudySetState() { }

  /**
   * Returns the name.
   *
   * @return the study set name currently shown
   */
  public String getName() { return name; }

  /**
   * Sets the name.
   *
   * @param name the study set name to show
   */
  public void setName(String name) { this.name = name; }

  /**
   * Returns the error message.
   *
   * @return the error to display, or null if there is none
   */
  public String getErrorMessage() { return errorMessage; }

  /**
   * Sets the error message.
   *
   * @param errorMessage the error to display, or null to clear it
   */
  public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

  /**
   * Returns the id of the study set just created.
   *
   * @return the new study set's id, or null if none was created
   */
  public String getCreatedStudySetId() { return createdStudySetId; }

  /**
   * Sets the id of the study set just created.
   *
   * @param id the new study set's id, or null to clear it
   */
  public void setCreatedStudySetId(String id) { this.createdStudySetId = id; }

  /**
   * Returns the user's study sets.
   *
   * @return the entries shown in the study set panel
   */
  public List<Item> getStudySets() { return studySets; }

  /**
   * Sets the user's study sets.
   *
   * @param studySets the entries to show in the study set panel
   */
  public void setStudySets(List<Item> studySets) { this.studySets = studySets; }

  /**
   * One entry in the study set panel: a study set's id, name and card count.
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