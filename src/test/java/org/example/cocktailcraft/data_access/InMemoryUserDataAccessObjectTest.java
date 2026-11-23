package org.example.cocktailcraft.data_access;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserDataAccessObjectTest {

    @Test
    void testValidCredentials() {
        InMemoryUserDataAccessObject dao =
                new InMemoryUserDataAccessObject();

        dao.createUser("alice", "password123");

        assertTrue(dao.existsByName("alice"));
        assertTrue(dao.validateCredentials("alice", "password123"));
    }

    @Test
    void testIncorrectPassword() {
        InMemoryUserDataAccessObject dao =
                new InMemoryUserDataAccessObject();

        dao.createUser("alice", "password123");

        assertFalse(dao.validateCredentials("alice", "wrongpassword"));
    }

    @Test
    void testUnknownUser() {
        InMemoryUserDataAccessObject dao =
                new InMemoryUserDataAccessObject();

        assertFalse(dao.validateCredentials("unknown", "password123"));
    }
}

