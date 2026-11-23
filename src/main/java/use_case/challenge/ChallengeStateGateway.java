package use_case.challenge;

import entity.ChallengeResult;

/**
 * Proposed integration port; see GitHub issues #9 and #10.
 * Mutations must be atomic, attempt-ID-idempotent, and leave state unchanged on failure.
 * The shared state owner supplies an implementation over the current Player/academic record.
 */
public interface ChallengeStateGateway {
    ChallengeOverview loadOverview() throws ChallengeStateException;

    /** Rechecks enrolment, unfinished status, energy and the global activity lock before charging. */
    ChallengeOverview startChallenge(String courseId, String attemptId, int energyCost)
            throws ChallengeStateException;

    /** Records completion and points once, preserves exam progress, and releases the activity lock. */
    ChallengeOverview completeChallenge(String courseId, String attemptId, ChallengeResult result)
            throws ChallengeStateException;

    /** Releases the matching attempt lock without a refund or any academic reward. */
    ChallengeOverview abandonChallenge(String attemptId) throws ChallengeStateException;
}
