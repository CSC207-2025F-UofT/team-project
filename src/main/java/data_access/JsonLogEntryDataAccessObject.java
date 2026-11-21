package data_access;

import entities.LogEntry;
import entities.LogExerciseSet;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.LogEntryDataAccessInterface;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Stores log entries in a JSON file. All entries are loaded into memory on
 * construction and the whole file is rewritten after every change.
 */
public class JsonLogEntryDataAccessObject implements LogEntryDataAccessInterface {
    public static final String DEFAULT_PATH = "userdata/logs.json";

    private final Path filePath;
    private final List<LogEntry> entries = new ArrayList<>();

    public JsonLogEntryDataAccessObject() {
        this(DEFAULT_PATH);
    }

    public JsonLogEntryDataAccessObject(String filePath) {
        this.filePath = Paths.get(filePath);
        load();
    }

    @Override
    public void save(LogEntry entry) {
        entries.add(entry);
        entries.sort(Comparator.comparing(LogEntry::getTime));
        persist();
    }

    @Override
    public List<LogEntry> getAll() {
        return List.copyOf(entries);
    }

    @Override
    public List<LogEntry> getBetween(LocalDateTime from, LocalDateTime to) {
        List<LogEntry> result = new ArrayList<>();
        for (LogEntry entry : entries) {
            LocalDateTime time = entry.getTime();
            if (!time.isBefore(from) && !time.isAfter(to)) {
                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public List<LogEntry> getByExerciseName(String exerciseName) {
        List<LogEntry> result = new ArrayList<>();
        for (LogEntry entry : entries) {
            for (LogExerciseSet set : entry.getExercises()) {
                if (set.getExerciseName().equalsIgnoreCase(exerciseName)) {
                    result.add(entry);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public boolean delete(LocalDateTime time) {
        boolean removed = entries.removeIf(entry -> entry.getTime().equals(time));
        if (removed) {
            persist();
        }
        return removed;
    }

    private void load() {
        if (!Files.exists(filePath)) {
            return;
        }
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            if (content.isBlank()) {
                return;
            }
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                entries.add(logEntryFromJson(array.getJSONObject(i)));
            }
            entries.sort(Comparator.comparing(LogEntry::getTime));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + filePath, e);
        }
    }

    private void persist() {
        JSONArray array = new JSONArray();
        for (LogEntry entry : entries) {
            array.put(logEntryToJson(entry));
        }
        JsonFiles.write(filePath, array.toString(2));
    }

    static JSONObject logEntryToJson(LogEntry entry) {
        JSONArray exercises = new JSONArray();
        for (LogExerciseSet set : entry.getExercises()) {
            JSONObject json = new JSONObject();
            json.put("exerciseName", set.getExerciseName());
            json.put("weights", new JSONArray(set.getWeights()));
            json.put("reps", new JSONArray(set.getReps()));
            exercises.put(json);
        }
        JSONObject json = new JSONObject();
        json.put("templateName", entry.getTemplateName());
        json.put("time", entry.getTime().toString());
        json.put("exercises", exercises);
        return json;
    }

    static LogEntry logEntryFromJson(JSONObject json) {
        JSONArray exercisesJson = json.getJSONArray("exercises");
        List<LogExerciseSet> exercises = new ArrayList<>();
        for (int i = 0; i < exercisesJson.length(); i++) {
            JSONObject setJson = exercisesJson.getJSONObject(i);
            JSONArray weightsJson = setJson.getJSONArray("weights");
            JSONArray repsJson = setJson.getJSONArray("reps");
            List<Double> weights = new ArrayList<>();
            List<Integer> reps = new ArrayList<>();
            for (int j = 0; j < weightsJson.length(); j++) {
                weights.add(weightsJson.getDouble(j));
            }
            for (int j = 0; j < repsJson.length(); j++) {
                reps.add(repsJson.getInt(j));
            }
            exercises.add(new LogExerciseSet(setJson.getString("exerciseName"), weights, reps));
        }
        return new LogEntry(
                json.getString("templateName"),
                LocalDateTime.parse(json.getString("time")),
                exercises);
    }
}
