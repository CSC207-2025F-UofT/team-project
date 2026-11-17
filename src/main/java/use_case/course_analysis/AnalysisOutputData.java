package use_case.course_analysis;
import java.util.ArrayList;

/**
 * Output Data for the Analysis Use Case.
 */
public class AnalysisOutputData {

    private final ArrayList<String> skills;

    public AnalysisOutputData(ArrayList<String> skills) {
        this.skills = skills;
    }

    public ArrayList<String> getSkills() {
        return new ArrayList<>(skills);
    }
}
