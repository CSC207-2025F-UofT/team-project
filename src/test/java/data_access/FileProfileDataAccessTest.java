package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import java.util.UUID;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.profile.ProfileData;
import use_case.profile.ProfileStorageException;
import use_case.profile.SavedProfile;

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
    SavedProfile snapshot = new FileProfileDataAccess(path).loadSnapshot().orElseThrow();
    QualificationProfile loaded = snapshot.getProfile();
    assertEquals("student-123", loaded.getOwner().getUserId());
    assertEquals("student@example.org", loaded.getOwner().getEmail());
    assertEquals("Student", loaded.getOwner().getName());
    assertEquals("Math", loaded.getProgram());
    assertEquals(2028, loaded.getGraduationYear().intValue());
    assertEquals(List.of("SQL", "python"), loaded.getSkills());
    assertEquals(List.of("Data science"), loaded.getPreferredRoles());
    assertEquals(List.of("Montréal"), loaded.getPreferredLocations());
    assertEquals(Optional.of(WorkArrangement.HYBRID), loaded.getPreferredArrangement());
    first.update(
        new ProfileData("Math", "", "Go", "", "", "ANY").toProfile(loaded.getOwner()),
        snapshot);
    assertEquals("", ProfileData.from(first.load().orElseThrow()).getYear());
    assertEquals(Optional.empty(), first.load().orElseThrow().getPreferredArrangement());
  }

  @Test
  void duplicateCreateFailedWritesAndForeignUpdatesPreserveSavedData() throws Exception {
    Path path = directory.resolve("profile.json");
    FileProfileDataAccess store = new FileProfileDataAccess(path);
    QualificationProfile original = profile();
    QualificationProfile foreign = profile();
    SavedProfile missing = new SavedProfile(original, UUID.randomUUID());
    assertThrows(ProfileStorageException.class, () -> store.update(original, missing));
    SavedProfile baseline = store.create(original);
    final String before = Files.readString(path);
    assertThrows(ProfileStorageException.class, () -> store.create(foreign));
    assertThrows(ProfileStorageException.class, () -> store.update(foreign, baseline));
    SavedProfile foreignBaseline = new SavedProfile(foreign, baseline.getRevision());
    assertThrows(ProfileStorageException.class, () -> store.update(original, foreignBaseline));
    try (FileChannel channel =
            FileChannel.open(directory.resolve("profile.json.lock"), StandardOpenOption.WRITE);
        FileLock held = channel.lock()) {
      assertTrue(held.isValid());
      assertThrows(ProfileStorageException.class, () -> store.update(original, baseline));
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
    QualificationProfile edited = profile();
    SavedProfile baseline = new SavedProfile(edited, UUID.randomUUID());
    assertThrows(ProfileStorageException.class, () -> store.update(edited, baseline));
    assertEquals("{broken profile", Files.readString(path));
  }

  @Test
  void olderWindowsCanMergeEditsAfterLegacyProfileMigration() throws Exception {
    Path path = directory.resolve("profile.json");
    FileProfileDataAccess store = new FileProfileDataAccess(path);
    User owner = new User();
    store.create(
        new ProfileData("Math", "2028", "Java", "Dev", "Toronto", "remote").toProfile(owner));
    JSONObject legacy = new JSONObject(Files.readString(path));
    legacy.remove("revision");
    String contents = legacy.toString();
    Files.writeString(path, contents);

    SavedProfile first = store.loadSnapshot().orElseThrow();
    FileProfileDataAccess other = new FileProfileDataAccess(path);
    SavedProfile second = other.loadSnapshot().orElseThrow();
    assertEquals(first.getRevision(), second.getRevision());
    assertEquals(contents, Files.readString(path));

    SavedProfile committed =
        store.update(
            new ProfileData("Math", "", "SQL", "Dev", "Montréal", "ANY").toProfile(owner), first);
    String afterSave = Files.readString(path);
    assertEquals(
        committed.getRevision().toString(), new JSONObject(afterSave).getString("revision"));
    other.update(
        new ProfileData("Math", "2028", "Java", "", "Toronto", "remote").toProfile(owner), second);
    QualificationProfile merged = store.load().orElseThrow();
    assertNull(merged.getGraduationYear());
    assertEquals(List.of("SQL"), merged.getSkills());
    assertEquals(List.of(), merged.getPreferredRoles());
    assertEquals(List.of("Montréal"), merged.getPreferredLocations());
    assertEquals(Optional.empty(), merged.getPreferredArrangement());
  }

  private static QualificationProfile profile() {
    return new ProfileData("Math", "", "SQL", "", "", "ANY").toProfile(new User());
  }
}
