package entity;

import java.util.List;

public class Exercise {
    private final String exerciseID;
    private final String name;
    private final String description;
    private final String mainMuscleGroup;
    private final List<String> musclesWorked;
    private final String equipment;
    private final String imageUrl; // optional, may be null

    public Exercise(String exerciseID, String name, String description, String mainMuscleGroup,
                    List<String> musclesWorked, String equipment, String imageUrl) {
        this.exerciseID = exerciseID;
        this.name = name;
        this.description = description;
        this.mainMuscleGroup = mainMuscleGroup;
        this.musclesWorked = musclesWorked;
        this.equipment = equipment;
        this.imageUrl = imageUrl;
    }

    public String getExerciseID() { return exerciseID; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getMainMuscleGroup() { return mainMuscleGroup; }
    public List<String> getMusclesWorked() { return musclesWorked; }
    public String getEquipment() { return equipment; }
    public String getImageUrl() { return imageUrl; }
}
