package data_access;

import entities.Exercise;
import entities.ExerciseSetGroup;
import entities.Template;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.TemplateDataAccessInterface;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores templates in a JSON file, keyed by template name. All templates are
 * loaded into memory on construction and the whole file is rewritten after
 * every change. Each exercise is stored in full so the file is self-contained.
 */
public class JsonTemplateDataAccessObject implements TemplateDataAccessInterface {
    public static final String DEFAULT_PATH = "userdata/templates.json";

    private final Path filePath;
    private final Map<String, Template> templates = new LinkedHashMap<>();

    public JsonTemplateDataAccessObject() {
        this(DEFAULT_PATH);
    }

    public JsonTemplateDataAccessObject(String filePath) {
        this.filePath = Paths.get(filePath);
        load();
    }

    @Override
    public void save(Template template) {
        templates.put(template.getName(), template);
        persist();
    }

    @Override
    public Template get(String name) {
        return templates.get(name);
    }

    @Override
    public boolean existsByName(String name) {
        return templates.containsKey(name);
    }

    @Override
    public List<Template> getAll() {
        return List.copyOf(templates.values());
    }

    @Override
    public boolean delete(String name) {
        boolean removed = templates.remove(name) != null;
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
                Template template = templateFromJson(array.getJSONObject(i));
                templates.put(template.getName(), template);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + filePath, e);
        }
    }

    private void persist() {
        JSONArray array = new JSONArray();
        for (Template template : templates.values()) {
            array.put(templateToJson(template));
        }
        JsonFiles.write(filePath, array.toString(2));
    }

    static JSONObject templateToJson(Template template) {
        JSONArray groups = new JSONArray();
        for (ExerciseSetGroup group : template.getGroups()) {
            JSONObject json = new JSONObject();
            json.put("exercise", exerciseToJson(group.getExercise()));
            json.put("weights", new JSONArray(group.getWeights()));
            json.put("reps", new JSONArray(group.getRepsList()));
            groups.put(json);
        }
        JSONObject json = new JSONObject();
        json.put("name", template.getName());
        json.put("groups", groups);
        return json;
    }

    static Template templateFromJson(JSONObject json) {
        Template template = new Template(json.getString("name"));
        JSONArray groups = json.getJSONArray("groups");
        for (int i = 0; i < groups.length(); i++) {
            JSONObject groupJson = groups.getJSONObject(i);
            ExerciseSetGroup group = new ExerciseSetGroup(exerciseFromJson(groupJson.getJSONObject("exercise")));
            JSONArray weights = groupJson.getJSONArray("weights");
            JSONArray reps = groupJson.getJSONArray("reps");
            for (int j = 0; j < weights.length(); j++) {
                group.addSet(weights.getDouble(j), reps.getInt(j));
            }
            template.addGroup(group);
        }
        return template;
    }

    private static JSONObject exerciseToJson(Exercise exercise) {
        JSONObject json = new JSONObject();
        json.put("id", exercise.getId());
        json.put("name", exercise.getName());
        json.put("targetMuscles", new JSONArray(exercise.getTargetMuscles()));
        json.put("equipments", new JSONArray(exercise.getEquipments()));
        json.put("bodyParts", new JSONArray(exercise.getBodyParts()));
        json.put("videoUrl", exercise.getVideoUrl() == null ? JSONObject.NULL : exercise.getVideoUrl());
        json.put("instructions", new JSONArray(exercise.getInstructions()));
        return json;
    }

    private static Exercise exerciseFromJson(JSONObject json) {
        return new Exercise(
                json.getString("id"),
                json.getString("name"),
                toStringList(json.optJSONArray("targetMuscles")),
                toStringList(json.optJSONArray("equipments")),
                toStringList(json.optJSONArray("bodyParts")),
                json.isNull("videoUrl") ? null : json.getString("videoUrl"),
                toStringList(json.optJSONArray("instructions")));
    }

    private static List<String> toStringList(JSONArray array) {
        List<String> result = new ArrayList<>();
        if (array == null) {
            return result;
        }
        for (int i = 0; i < array.length(); i++) {
            result.add(array.getString(i));
        }
        return result;
    }
}
