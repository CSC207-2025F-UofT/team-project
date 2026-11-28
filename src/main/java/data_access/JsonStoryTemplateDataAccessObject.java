package data_access;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import entity.StoryTemplate;
import use_case.StorySummary;
import use_case.StoryTemplateDataAccessInterface;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Reads a directory catalog, or the packaged laboratory story. IDs never become file paths. */
public final class JsonStoryTemplateDataAccessObject implements StoryTemplateDataAccessInterface {
    private final Path directory;
    private final ObjectMapper json = JsonMapper.builder()
            .addModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .addModule(new Jdk8Module())
            .enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION)
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS,
                    DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
            .build();

    public JsonStoryTemplateDataAccessObject(Path directory) { this.directory = Objects.requireNonNull(directory); }
    public JsonStoryTemplateDataAccessObject() { this.directory = null; }

    private Map<String, StoryTemplate> catalog() {
        Map<String, StoryTemplate> stories = new LinkedHashMap<>();
        try {
            if (directory == null) {
                try (InputStream stream = getClass().getResourceAsStream("/stories/laboratory.json")) {
                    if (stream == null) throw new IOException("Packaged laboratory story is missing");
                    add(stories, json.readValue(stream, StoryTemplate.class));
                }
            } else {
                try (Stream<Path> files = Files.list(directory)) {
                    for (Path file : files.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".json")).sorted().collect(Collectors.toList())) {
                        add(stories, json.readValue(file.toFile(), StoryTemplate.class));
                    }
                }
            }
        } catch (IOException failure) {
            throw new IllegalArgumentException("Cannot read story catalog: " + failure.getMessage(), failure);
        }
        return stories;
    }

    private static void add(Map<String, StoryTemplate> stories, StoryTemplate story) {
        if (stories.putIfAbsent(story.id, story) != null) throw new IllegalArgumentException("Duplicate story: " + story.id);
    }

    public List<StorySummary> listTemplates() {
        return catalog().values().stream().map(t -> new StorySummary(t.id, t.title, t.background))
                .collect(Collectors.toUnmodifiableList());
    }

    public StoryTemplate load(String templateId) {
        StoryTemplate story = catalog().get(templateId);
        if (story == null) throw new IllegalArgumentException("Unknown story: " + templateId);
        return story;
    }
}
