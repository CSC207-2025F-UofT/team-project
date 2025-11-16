package com.studyarc.use_case.login;

public interface LoginInputBoundary {
    LoginResult login(String username, String password);
}
