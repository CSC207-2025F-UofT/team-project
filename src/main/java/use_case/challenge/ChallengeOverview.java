package use_case.challenge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A consistent snapshot, never a second mutable copy of the shared Player. */
public final class ChallengeOverview {
    private final int energy;
    private final List<ChallengeCourse> courses;

    public ChallengeOverview(int energy, List<ChallengeCourse> courses) {
        if (energy < 0) { throw new IllegalArgumentException("Energy cannot be negative"); }
        this.energy = energy;
        this.courses = List.copyOf(courses);
        Set<String> ids = new HashSet<>();
        for (ChallengeCourse course : this.courses) {
            if (!ids.add(course.getId())) { throw new IllegalArgumentException("Duplicate course ID"); }
        }
    }

    public int getEnergy() { return energy; }
    public List<ChallengeCourse> getCourses() { return courses; }
}
