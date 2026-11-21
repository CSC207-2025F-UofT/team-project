package entities;

import java.util.List;

/**
 * Criteria for searching exercises: by name, body part, target muscle and/or equipment.
 * A null/empty criterion means "don't filter on this field".
 */
public class FilterCriteria {
    private final String nameQuery;
    private final String bodyPart;
    private final String targetMuscle;
    private final String equipment;

    public FilterCriteria(String nameQuery, String bodyPart, String targetMuscle, String equipment){
        this.nameQuery = nameQuery;
        this.bodyPart = bodyPart;
        this.targetMuscle = targetMuscle;
        this.equipment = equipment;
    }

    public String getNameQuery(){
        return nameQuery;
    }

    public String getBodyPart(){
        return bodyPart;
    }

    public String getTargetMuscle(){
        return targetMuscle;
    }

    public String getEquipment(){
        return equipment;
    }

    /** @return true if the given exercise satisfies every non-empty criterion (case-insensitive). */
    public boolean matches(Exercise exercise){
        if (!isBlank(nameQuery)
                && !exercise.getName().toLowerCase().contains(nameQuery.toLowerCase())){
            return false;
        }
        return matchesAny(exercise.getBodyParts(), bodyPart)
                && matchesAny(exercise.getTargetMuscles(), targetMuscle)
                && matchesAny(exercise.getEquipments(), equipment);
    }

    private static boolean matchesAny(List<String> values, String criterion){
        if (isBlank(criterion)) return true;
        for (String value : values){
            if (value.equalsIgnoreCase(criterion)) return true;
        }
        return false;
    }

    private static boolean isBlank(String s){
        return s == null || s.isBlank();
    }
}
