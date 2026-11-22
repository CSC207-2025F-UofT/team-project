package data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import entity.LabModule;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Keeps the student's lab, modules and checkpoint questions in memory, and writes the student's
 * progress to a JSON file so it is still there when the app opens again. Only progress is saved
 * (each module's answers and when it was last edited); the module content stays in code.
 */
public class JsonLabDataAccessObject extends InMemoryLabDataAccessObject {
  // TODO: also save the lab and its undo snapshots, with data.LabJson, once Lab has fields.
  private final Path file;
  private final Gson gson = new Gson();

  /**
   * Starts with the Pre-lab 3 module and the progress saved in the file.
   *
   * @param file the save file; it is created on the first save
   */
  public JsonLabDataAccessObject(Path file) {
    this(file, List.of(PreLab3Quiz.module()));
  }

  /**
   * Starts with the given modules and the progress saved in the file. If the file can't be
   * read as a save file, it is moved aside to {@code <file>.broken} and the student starts
   * fresh, so the next save doesn't overwrite it.
   *
   * @param file the save file; it is created on the first save
   * @param modules the modules assigned to the student
   */
  public JsonLabDataAccessObject(Path file, List<LabModule> modules) {
    super(modules);
    this.file = file;
    load();
  }

  @Override
  public void saveModule(LabModule module) {
    super.saveModule(module);
    write();
  }

  private void load() {
    if (!Files.exists(file)) {
      return;
    }
    SaveFile saved;
    try {
      saved = gson.fromJson(Files.readString(file), SaveFile.class);
    } catch (JsonParseException e) {
      moveAside();
      return;
    } catch (IOException e) {
      throw new UncheckedIOException("Couldn't read " + file, e);
    }
    if (saved == null || saved.getModules() == null) {
      return;
    }
    for (LabModule module : getModules()) {
      ModuleProgress progress = saved.getModules().get(module.getTitle());
      if (progress == null) {
        continue;
      }
      List<Set<Integer>> answers = progress.getSavedAnswers();
      // A different number of answers means the questions changed since the save.
      if (answers != null
          && answers.size() == module.getPreProcedureQuestions().size()) {
        module.setSavedAnswers(answers);
      }
      if (progress.getLastEdited() != null) {
        module.setLastEdited(LocalDateTime.parse(progress.getLastEdited()));
      }
    }
  }

  private void moveAside() {
    try {
      Files.move(file, file.resolveSibling(file.getFileName() + ".broken"),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new UncheckedIOException("Couldn't move aside unreadable " + file, e);
    }
  }

  private void write() {
    Map<String, ModuleProgress> progress = new LinkedHashMap<>();
    for (LabModule module : getModules()) {
      progress.put(module.getTitle(), new ModuleProgress(module.getSavedAnswers(),
          module.getLastEdited().map(LocalDateTime::toString).orElse(null)));
    }
    try {
      Path parent = file.toAbsolutePath().getParent();
      Files.createDirectories(parent);
      // Write a temporary file first so a crash mid-write can't leave a half-written save.
      Path temporary = Files.createTempFile(parent, "save", ".tmp");
      Files.writeString(temporary, gson.toJson(new SaveFile(progress)));
      Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.ATOMIC_MOVE);
    } catch (IOException e) {
      throw new UncheckedIOException("Couldn't save to " + file, e);
    }
  }

  /** The save file's contents: each module's progress, by module title. */
  private static final class SaveFile {
    private final Map<String, ModuleProgress> modules;

    SaveFile(Map<String, ModuleProgress> modules) {
      this.modules = modules;
    }

    Map<String, ModuleProgress> getModules() {
      return modules;
    }
  }

  /** One module's saved answers, and when it was last edited (ISO text, null if never). */
  private static final class ModuleProgress {
    private final List<Set<Integer>> savedAnswers;
    private final String lastEdited;

    ModuleProgress(List<Set<Integer>> savedAnswers, String lastEdited) {
      this.savedAnswers = savedAnswers;
      this.lastEdited = lastEdited;
    }

    List<Set<Integer>> getSavedAnswers() {
      return savedAnswers;
    }

    String getLastEdited() {
      return lastEdited;
    }
  }
}
