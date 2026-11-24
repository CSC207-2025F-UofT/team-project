package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.QualificationProfile;
import entity.User;
import entity.WorkArrangement;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.profile.ProfileData;
import use_case.profile.ProfileStorageException;

class FileProfileDataAccessTest {
  @TempDir Path directory;

  @Test
  void restoresIdentityAndNormalizedQualificationsAcrossNewInstances() throws Exception {
    Path path = directory.resolve("nested/profile.json");
    FileProfileDataAccess first = new FileProfileDataAccess(path);
    assertTrue(first.load().isEmpty());
    assertFalse(Files.exists(path));
    User owner = new User("student-123", "Student", "student@example.org");
    first.create(
        new ProfileData(
                " Math ", "2028", "SQL, python, sql", " Data  science ", "Montréal", "hybrid")
            .toProfile(owner));
    QualificationProfile loaded = new FileProfileDataAccess(path).load().orElseThrow();
    assertEquals("student-123", loaded.getOwner().getUserId());
    assertEquals("student@example.org", loaded.getOwner().getEmail());
    assertEquals("Student", loaded.getOwner().getName());
    assertEquals("Math", loaded.getProgram());
    assertEquals(2028, loaded.getGraduationYear().intValue());
    assertEquals(List.of("SQL", "python"), loaded.getSkills());
    assertEquals(List.of("Data science"), loaded.getPreferredRoles());
    assertEquals(List.of("Montréal"), loaded.getPreferredLocations());
    assertEquals(Optional.of(WorkArrangement.HYBRID), loaded.getPreferredArrangement());
    first.update(new ProfileData("Math", "", "Go", "", "", "ANY").toProfile(loaded.getOwner()));
    assertEquals("", ProfileData.from(first.load().orElseThrow()).getYear());
    assertEquals(Optional.empty(), first.load().orElseThrow().getPreferredArrangement());
  }

  @Test
  void duplicateCreateFailedWritesAndForeignUpdatesPreserveSavedData() throws Exception {
    Path path = directory.resolve("profile.json");
    FileProfileDataAccess store = new FileProfileDataAccess(path);
    QualificationProfile original = profile();
    QualificationProfile foreign = profile();
    assertThrows(ProfileStorageException.class, () -> store.update(original));
    store.create(original);
    final String before = Files.readString(path);
    assertThrows(ProfileStorageException.class, () -> store.create(foreign));
    assertThrows(ProfileStorageException.class, () -> store.update(foreign));
    try (FileChannel channel =
            FileChannel.open(directory.resolve("profile.json.lock"), StandardOpenOption.WRITE);
        FileLock held = channel.lock()) {
      assertTrue(held.isValid());
      assertThrows(ProfileStorageException.class, () -> store.update(original));
    }
    assertEquals(before, Files.readString(path));
  }

  @Test
  void damagedFileStaysIntactAfterReadAndWriteAttempts() throws Exception {
    Path path = directory.resolve("profile.json");
    Files.writeString(path, "{broken profile");
    FileProfileDataAccess store = new FileProfileDataAccess(path);
    assertThrows(ProfileStorageException.class, store::load);
    assertThrows(ProfileStorageException.class, () -> store.create(profile()));
    assertThrows(ProfileStorageException.class, () -> store.update(profile()));
    assertEquals("{broken profile", Files.readString(path));
  }

  private static QualificationProfile profile() {
    return new ProfileData("Math", "", "SQL", "", "", "ANY").toProfile(new User());
  }
}
