package entity;

import java.util.Objects;
import java.util.UUID;

/** A student's identity, shared across features independently of editable qualifications. */
public class User {
  private final String name;
  private final String userId;
  private final String email;

  /** Creates a local identity the first time the student saves a profile. */
  public User() {
    this(UUID.randomUUID().toString(), "", "");
  }

  /** Restores the identity of an existing student. */
  public User(String userId, String name, String email) {
    if (userId == null || userId.isBlank()) {
      throw new IllegalArgumentException("A student ID is required.");
    }
    this.userId = userId;
    this.name = Objects.requireNonNull(name);
    this.email = Objects.requireNonNull(email);
  }

  public String getName() {
    return name;
  }

  public String getUserId() {
    return userId;
  }

  public String getEmail() {
    return email;
  }
}
