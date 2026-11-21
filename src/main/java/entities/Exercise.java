package entities;

import java.util.List;

/**
 * Immutable exercise entity supporting summary search results and full detail records.
 * List fields are defensively copied and exposed as unmodifiable lists.
 */
public class Exercise {
    private final String id;
    private final String name;
    private final List<String> targetMuscles;
    private final List<String> equipments;
    private final List<String> bodyParts;
    private final String videoUrl;
    private final List<String> instructions;

    /** Creates an immutable {@code Exercise} from {@code Exercisebuilder}, normalizing null lists to empty. */
    private Exercise(ExerciseBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.videoUrl = builder.videoUrl;

        if (builder.targetMuscles == null) {
            this.targetMuscles = List.of();
        } else {
            this.targetMuscles = List.copyOf(builder.targetMuscles);
        }

        if (builder.equipments == null) {
            this.equipments = List.of();
        } else {
            this.equipments = List.copyOf(builder.equipments);
        }

        if (builder.bodyParts == null) {
            this.bodyParts = List.of();
        } else {
            this.bodyParts = List.copyOf(builder.bodyParts);
        }

        if (builder.instructions == null) {
            this.instructions = List.of();
        } else {
            this.instructions = List.copyOf(builder.instructions);
        }
    }

    /** Getters */
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getTargetMuscles() {
        return targetMuscles;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public List<String> getBodyParts() {
        return bodyParts;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    /** Fluent builder for constructing {@link Exercise} instances. */
    public static class ExerciseBuilder {
        private String id;
        private String name;
        private List<String> targetMuscles;
        private List<String> equipments;
        private List<String> bodyParts;
        private String videoUrl;
        private List<String> instructions;

        public ExerciseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ExerciseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ExerciseBuilder targetMuscles(List<String> targetMuscles) {
            this.targetMuscles = targetMuscles;
            return this;
        }

        public ExerciseBuilder equipments(List<String> equipments) {
            this.equipments = equipments;
            return this;
        }

        public ExerciseBuilder bodyParts(List<String> bodyParts) {
            this.bodyParts = bodyParts;
            return this;
        }

        public ExerciseBuilder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public ExerciseBuilder instructions(List<String> instructions) {
            this.instructions = instructions;
            return this;
        }

        /** Builds and returns the immutable {@link Exercise}. */
        public Exercise build() {
            return new Exercise(this);
        }
    }
}