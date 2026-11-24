package interface_adapter.skills_list;
import java.util.ArrayList;

/**
 * The state for the Signup View Model.
 */
public class SkillListState {

    private ArrayList<String> skills = new ArrayList<>();
    private String skillsError;

    public String getSkillsError() {
        return skillsError;
    }

    public void setSkillsError(String error) {
        this.skillsError = error;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public ArrayList<String> getSkills() {
        return new ArrayList<>(skills);
    }

    @Override
    public String toString() {
        return "AnalysisState{"
                + "skills='" + skills.toString() + '\''
                + '}';
    }
}
