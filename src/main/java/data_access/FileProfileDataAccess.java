package data_access;

import entity.QualificationProfile;
import entity.User;
import entity.WorkArrangement;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.profile.ProfileRepository;
import use_case.profile.ProfileStorageException;
import use_case.profile.SavedProfile;

/** Stores one local qualification profile as UTF-8 JSON using whole-file replacement. */
public final class FileProfileDataAccess implements ProfileRepository {
  private final Path path;

  /** Uses the supplied file, creating its parent directory on the first save. */
  public FileProfileDataAccess(Path path) {
    this.path = path.toAbsolutePath().normalize();
  }

  @Override
  public Optional<SavedProfile> loadSnapshot() throws ProfileStorageException {
    try {
      String contents = Files.readString(path);
      JSONObject json = new JSONObject(contents);
      User owner =
          new User(json.getString("userId"), json.getString("name"), json.getString("email"));
      Object year = json.get("graduationYear");
      QualificationProfile profile =
          new QualificationProfile(
              owner,
              json.getString("program"),
              year == JSONObject.NULL ? null : Integer.valueOf(year.toString()),
              strings(json, "skills"),
              strings(json, "preferredRoles"),
              strings(json, "preferredLocations"),
              json.isNull("preferredArrangement")
                  ? Optional.empty()
                  : Optional.of(WorkArrangement.valueOf(json.getString("preferredArrangement"))));
      // Older files have no revision. Two readers of the same bytes must get the same token.
      UUID revision =
          json.has("revision")
              ? UUID.fromString(json.getString("revision"))
              : UUID.nameUUIDFromBytes(contents.getBytes(StandardCharsets.UTF_8));
      return Optional.of(new SavedProfile(profile, revision));
    } catch (NoSuchFileException absent) {
      return Optional.empty();
    } catch (IOException | JSONException | IllegalArgumentException | SecurityException exception) {
      throw new ProfileStorageException(
          "Could not read the saved profile. Check the file and retry.", exception);
    }
  }

  @Override
  public SavedProfile create(QualificationProfile profile) throws ProfileStorageException {
    return write(profile, Optional.empty());
  }

  @Override
  public SavedProfile update(QualificationProfile edited, SavedProfile baseline)
      throws ProfileStorageException {
    return write(edited, Optional.of(baseline));
  }

  private SavedProfile write(QualificationProfile profile, Optional<SavedProfile> baseline)
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
        Optional<SavedProfile> existing = loadSnapshot();
        QualificationProfile toSave = profile;
        if (baseline.isPresent()) {
          SavedProfile current =
              existing.orElseThrow(
                  () ->
                      new ProfileStorageException(
                          "The saved profile is missing. Click Reload profile."));
          toSave = current.applyEdits(baseline.get(), profile);
        } else if (existing.isPresent()) {
          throw new ProfileStorageException("A profile already exists. Reload it to edit it.");
        }
        SavedProfile saved = new SavedProfile(toSave, UUID.randomUUID());
        replaceFile(saved);
        return saved;
      }
    } catch (OverlappingFileLockException exception) {
      throw new ProfileStorageException("Another save is in progress. Try again.", exception);
    } catch (IOException | SecurityException exception) {
      throw new ProfileStorageException(
          "Could not save the profile. Check file access and retry.", exception);
    }
  }

  private void replaceFile(SavedProfile saved) throws IOException {
    QualificationProfile profile = saved.getProfile();
    User owner = profile.getOwner();
    JSONObject json =
        new JSONObject()
            .put("revision", saved.getRevision().toString())
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
