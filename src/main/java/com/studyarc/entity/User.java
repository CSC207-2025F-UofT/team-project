package com.studyarc.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private byte[] salt;
    private byte[] passwordHash;

    public boolean validateHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes(StandardCharsets.UTF_8));
        md.update(this.salt);
        return md.digest() == this.passwordHash;
    }
}
