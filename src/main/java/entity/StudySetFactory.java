package entity;

import java.util.UUID;

/**
 * Creates new, empty {@link StudySet} objects.
 */
public class StudySetFactory {

  /**
   * Creates a factory.
   */
  public StudySetFactory() { }

  /**
   * Creates an empty study set with a freshly generated unique id.
   *
   * @param name the name for the new study set
   * @return the new study set
   */
  public StudySet create(String name) {
    return new StudySet(UUID.randomUUID().toString(), name);
  }
}