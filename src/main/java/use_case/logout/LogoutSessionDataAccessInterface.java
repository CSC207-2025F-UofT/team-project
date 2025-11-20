package use_case.logout;

import entity.User;

public interface LogoutSessionDataAccessInterface {
    User getCurrentUser();
    void clearCurrentUser();
}
