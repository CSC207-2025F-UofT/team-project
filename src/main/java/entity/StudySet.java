package entity;

/**
 * The whole set of study materials for one subject.
 */
public class StudySet {
  private final String id;
  private final String name;
  private final CardsList cardsList;

  /**
   * Creates a brand-new study set with an empty cards list.
   *
   * @param id   the unique identifier
   * @param name the study set's name
   */
  public StudySet(String id, String name) {
    this(id, name, new CardsList());
  }

  /**
   * Rebuilds a study set whose cards already exist, for example one loaded from storage.
   *
   * @param id        the unique identifier
   * @param name      the study set's name
   * @param cardsList the existing flashcards section
   */
  public StudySet(String id, String name, CardsList cardsList) {
    this.id = id;
    this.name = name;
    this.cardsList = cardsList;
  }

  /**
   * Returns the identifier.
   *
   * @return the unique identifier of this study set
   */
  public String getId() { return id; }

  /**
   * Returns the name.
   *
   * @return the name of this study set
   */
  public String getName() { return name; }

  /**
   * Returns the flashcards section.
   *
   * @return the cards list of this study set
   */
  public CardsList getCardsList() { return cardsList; }
}