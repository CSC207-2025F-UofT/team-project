package use_case.account;

import entity.User;

public interface SessionStarter {
    /** Makes this user the signed-in user, replacing any previous session. */
    void start(AuthSession session, User user);
}
