package use_case.complete_quest;

import entity.User;

/** User storage, needed to award EXP (Lucy's data). */
public interface CompleteQuestUserDataAccessInterface {

    /** Returns null if no such user. */
    User getUser(String username);

    void saveUser(User user);
}
