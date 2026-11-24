package data_access;

import entity.QualificationProfile;
import entity.User;
import entity.WorkArrangement;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.profile.ProfileRepository;
import use_case.profile.ProfileStorageException;

/** Stores one local qualification profile as UTF-8 JSON using whole-file replacement. */
public final class FileProfileDataAccess implements ProfileRepository {
  private final Path path;

  /** Uses the supplied file, creating its parent directory on the first save. */
  public FileProfileDataAccess(Path path) {
    this.path = path.toAbsolutePath().normalize();
  }

  @Override
  public Optional<QualificationProfile> load() throws ProfileStorageException {
    try {
      JSONObject json = new JSONObject(Files.readString(path));
      User owner =
          new User(json.getString("userId"), json.getString("name"), json.getString("email"));
      Object year = json.get("graduationYear");
      return Optional.of(
          new QualificationProfile(
              owner,
              json.getString("program"),
              year == JSONObject.NULL ? null : Integer.valueOf(year.toString()),
              strings(json, "skills"),
              strings(json, "preferredRoles"),
              strings(json, "preferredLocations"),
              json.isNull("preferredArrangement")
                  ? Optional.empty()
                  : Optional.of(WorkArrangement.valueOf(json.getString("preferredArrangement")))));
    } catch (NoSuchFileException absent) {
      return Optional.empty();
    } catch (IOException | JSONException | IllegalArgumentException | SecurityException exception) {
      throw new ProfileStorageException(
          "Could not read the saved profile. Check the file and retry.", exception);
    }
  }

  @Override
  public void create(QualificationProfile profile) throws ProfileStorageException {
    write(profile, true);
  }

  @Override
  public void update(QualificationProfile profile) throws ProfileStorageException {
    write(profile, false);
  }

  private void write(QualificationProfile profile, boolean creating)
      throws ProfileStorageException {
    try {
      Files.createDirectories(path.getParent());
      Path lockPath = path.resolveSibling(path.getFileName() + ".lock");
      try (FileChannel channel =
              FileChannel.open(lockPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
          FileLock lock = channel.tryLock()) {
        if (lock == null) {
          throw new ProfileStorageException("Another copy of the app is saving. Try again.");
        }
        Optional<QualificationProfile> existing = load();
        if (creating && existing.isPresent()) {
          throw new ProfileStorageException("A profile already exists. Reload it to edit it.");
        }
        if (!creating
            && (existing.isEmpty()
                || !existing.get().getOwner().getUserId().equals(profile.getOwner().getUserId()))) {
          throw new ProfileStorageException(
              "The saved profile has changed. Reload it before editing.");
        }
        replaceFile(profile);
      }
    } catch (OverlappingFileLockException exception) {
      throw new ProfileStorageException("Another save is in progress. Try again.", exception);
    } catch (IOException | SecurityException exception) {
      throw new ProfileStorageException(
          "Could not save the profile. Check file access and retry.", exception);
    }
  }

  private void replaceFile(QualificationProfile profile) throws IOException {
    User owner = profile.getOwner();
    JSONObject json =
        new JSONObject()
            .put("userId", owner.getUserId())
            .put("name", owner.getName())
            .put("email", owner.getEmail())
            .put("program", profile.getProgram())
            .put(
                "graduationYear",
                profile.getGraduationYear() == null ? JSONObject.NULL : profile.getGraduationYear())
            .put("skills", new JSONArray(profile.getSkills()))
            .put("preferredRoles", new JSONArray(profile.getPreferredRoles()))
            .put("preferredLocations", new JSONArray(profile.getPreferredLocations()))
            .put(
                "preferredArrangement",
                profile.getPreferredArrangement().<Object>map(Enum::name).orElse(JSONObject.NULL));
    Path temporary = Files.createTempFile(path.getParent(), ".profile-", ".tmp");
    try {
      Files.writeString(temporary, json.toString(2));
      Files.move(
          temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } finally {
      Files.deleteIfExists(temporary);
    }
  }

  private static List<String> strings(JSONObject json, String key) {
    JSONArray array = json.getJSONArray(key);
    List<String> values = new ArrayList<>();
    for (int index = 0; index < array.length(); index++) {
      values.add(array.getString(index));
    }
    return values;
  }
}
