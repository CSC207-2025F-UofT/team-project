package use_case.create_study_set;

import entity.StudySet;

import java.util.List;

/**
 * The storage operations the Create Study Set use case needs.
 */
public interface CreateStudySetDataAccessInterface {
  /**
   * Checks whether the user already has a study set with the given name.
   *
   * @param userId the id of the user
   * @param name   the name to look for, compared ignoring case
   * @return true if the user already has a study set with that name
   */
  boolean existsByName(String userId, String name);

  /**
   * Saves a study set and links it to the given user.
   *
   * @param userId   the id of the user who owns the study set
   * @param studySet the study set to save
   */
  void save(String userId, StudySet studySet);

  /**
   * Returns all study sets belonging to a user.
   *
   * @param userId the id of the user
   * @return the user's study sets, in the order they were created (empty if none)
   */
  List<StudySet> getStudySets(String userId);
}