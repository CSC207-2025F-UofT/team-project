package com.studyarc.use_case.login;

import com.studyarc.entity.User;

public class LoginResult {
    private boolean success;
    private User user;

    LoginResult(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }
}
