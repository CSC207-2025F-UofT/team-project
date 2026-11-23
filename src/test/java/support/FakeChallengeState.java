package support;

import entity.ChallengeResult;
import java.util.LinkedHashMap;
import java.util.Map;
import use_case.challenge.ChallengeCourse;
import use_case.challenge.ChallengeOverview;
import use_case.challenge.ChallengeStateException;
import use_case.challenge.ChallengeStateGateway;

/** Test fixture only. Production must adapt the team's actual state and activity lock. */
public final class FakeChallengeState implements ChallengeStateGateway {
    private final Map<String, ChallengeCourse> courses = new LinkedHashMap<>();
    private final Map<String, String> completedAttempts = new LinkedHashMap<>();
    public int energy = 100;
    public int starts;
    public int completions;
    public int awardedPoints;
    public boolean failCompletion;
    public String activeAttempt;
    private String activeCourse;

    public synchronized void assign(ChallengeCourse course) { courses.put(course.getId(), course); }

    @Override public synchronized ChallengeOverview loadOverview() {
        return new ChallengeOverview(energy, java.util.List.copyOf(courses.values()));
    }

    @Override public synchronized ChallengeOverview startChallenge(String courseId, String attemptId,
                                                                  int energyCost)
            throws ChallengeStateException {
        if (attemptId.equals(activeAttempt) && courseId.equals(activeCourse)) { return loadOverview(); }
        ChallengeCourse course = courses.get(courseId);
        if (course == null || course.getResult().isPresent() || activeAttempt != null
                || energy < energyCost) {
            throw new ChallengeStateException("Cannot start: course, completion, activity or energy changed.");
        }
        energy -= energyCost;
        activeAttempt = attemptId;
        activeCourse = courseId;
        starts++;
        return loadOverview();
    }

    @Override public synchronized ChallengeOverview completeChallenge(String courseId, String attemptId,
                                                                     ChallengeResult result)
            throws ChallengeStateException {
        if (courseId.equals(completedAttempts.get(attemptId))) { return loadOverview(); }
        if (failCompletion || !attemptId.equals(activeAttempt) || !courseId.equals(activeCourse)) {
            throw new ChallengeStateException("Cannot record completion. Please retry Finish.");
        }
        ChallengeCourse old = courses.get(courseId);
        courses.put(courseId, new ChallengeCourse(old.getId(), old.getName(), old.getTopic(), result));
        completedAttempts.put(attemptId, courseId);
        awardedPoints += result.getPreparationPoints();
        activeAttempt = null;
        activeCourse = null;
        completions++;
        return loadOverview();
    }

    @Override public synchronized ChallengeOverview abandonChallenge(String attemptId)
            throws ChallengeStateException {
        if (activeAttempt != null && !attemptId.equals(activeAttempt)) {
            throw new ChallengeStateException("Another activity owns the lock.");
        }
        activeAttempt = null;
        activeCourse = null;
        return loadOverview();
    }
}
