# UC2: class challenges and the shared question API

Xinjia Shen owns this module. The latest README and the current course/API requirements take priority
over PLAN.md. The old Mathematics and offline-question-bank plan is not implemented.

## What works

- Read assigned courses and completion status through an injected state interface.
- Preview the three-question challenge and its 20-energy cost before starting.
- Load and validate API questions before charging. API failures throw and spend nothing.
- Show immediate correctness feedback and the correct answer after each submission.
- Commit one preparation point per correct answer only after finishing all three questions.
- View a completed course result without another charge or reward.
- Cancel freely before starting. Confirmed early exit keeps the energy cost and records no completion or points.
- Reject empty answers, duplicate starts, repeated submissions, stale question clicks, and old attempt IDs.
- Keep final feedback and allow Finish to retry if the shared state cannot commit completion.
- Run API work off the Swing event dispatch thread, disable controls while busy, and update controls on the EDT.

Three questions, 20 energy, and one point per correct answer are the non-conflicting defaults retained from
the secondary plan. They are defined in `ClassChallengeInteractor` and `ChallengeResult`.

## Clean Architecture

```mermaid
flowchart LR
    V[ClassChallengeView: Swing] --> C[ChallengeController]
    C --> I[ChallengeInputBoundary]
    I --> U[ClassChallengeInteractor]
    U --> E[Question / QuestionSession / ChallengeResult]
    U --> Q[QuestionGateway]
    H[HuggingFaceQuestionGateway: HTTP / Gson] -. implements .-> Q
    U --> S[ChallengeStateGateway]
    A[Team shared-state adapter: pending] -. implements .-> S
    U --> O[ChallengeOutputBoundary]
    P[ChallengePresenter] -. implements .-> O
    P --> M[ChallengeViewModel: plain state]
    M --> V
```

The entity and use-case packages import no Swing, HTTP or JSON libraries. The interactor does not know the
actual Player, Course, AcademicRecord, save file, or scraper implementation. `ChallengeCourse` and
`ChallengeOverview` are immutable projections, not replacement academic entities or a second Player.

The view calls its controller from a `SwingWorker`. The presenter converts output into plain view state;
the view dispatches rendering to the EDT. `ClassChallengeFactory` creates and connects the objects.

```mermaid
sequenceDiagram
    actor Player
    participant UI as Swing / Controller
    participant UC as ClassChallengeInteractor
    participant HF as QuestionGateway
    participant State as Shared state adapter
    Player->>UI: Confirm selected course
    UI->>UC: START(courseId), on worker
    UC->>State: Read current assigned course and energy
    UC->>HF: Load three practice questions
    alt API or data failure
        HF-->>UC: Throw QuestionLoadException
        UC-->>UI: Controller presents error; confirmation stays available
    else Questions valid
        UC->>State: Atomically revalidate, lock activity and spend 20 energy
        UC-->>UI: Question without the correct answer
        loop Three questions
            Player->>UI: Submit selected answer
            UI->>UC: ANSWER(attemptId, questionIndex, answerIndex)
            UC-->>UI: Correctness and source answer
            Player->>UI: Next / Finish
        end
        UC->>State: Atomically commit result and points; release lock
        UC-->>UI: Result and energy
    end
```

## Shared-state integration: still pending

Coordination issues: [energy and state #9](https://github.com/yuanyx233/team-project/issues/9),
[academic data #10](https://github.com/yuanyx233/team-project/issues/10), and
[question API/navigation #11](https://github.com/yuanyx233/team-project/issues/11).
These interfaces are proposals for review, not an agreed or completed team integration.

The inspected baseline is main `03f4400` (UC2, the UC3 shell, and UC4/UC5), the `uc1-choose-major` branch,
and UC3's new `ExamBattle` at `3c51fad`. The battle permits up to five answers, so EXAM question requests now
support up to five questions. This does not change the three-question UC2 challenge or UC3's battle rules.

| Actual teammate code | Remaining connection |
|---|---|
| `Player.getEnergy/getMaxEnergy/restoreEnergyToPercent` | No consume-energy operation yet |
| `Course.getId/getName/getQuestionCategory` | Categories are currently computing/biology/history, not six course-specific topics |
| `AcademicRecord.getSelectedMajor/getCourseProgress` | Use this same record across buildings and Continue |
| `CourseProgress.getCourseId` | No challenge result or preparation-point fields yet |
| `FileGameStateDataAccessObject` | Does not persist academic progress yet; Continue replaces Player |
| `AppBuilder` and `GameView` | Own the shared state and navigation; Lecture Hall remains a placeholder |

The UC1 demo currently uses COMP1/COMP2, BIO1/BIO2, HIST1/HIST2. They have not been assumed as agreed IDs.
UC2 treats IDs as opaque and receives a `QuestionTopic` explicitly. Do not infer identity from the course name
or pass a whole major's category as a course-specific topic.

The state owner supplies an adapter implementing:

```java
ChallengeOverview loadOverview() throws ChallengeStateException;
ChallengeOverview startChallenge(String courseId, String attemptId, int energyCost)
        throws ChallengeStateException;
ChallengeOverview completeChallenge(String courseId, String attemptId, ChallengeResult result)
        throws ChallengeStateException;
ChallengeOverview abandonChallenge(String attemptId) throws ChallengeStateException;
```

- Start must atomically recheck enrolment, unfinished status, energy, and the shared activity lock, then
  charge exactly once. A failed check changes nothing. The check occurs after loading API questions because
  the state may have changed while waiting for the network.
- Complete must record this course's completion/points once and release the lock without touching exam grades.
  Replaying the same attempt ID must not add points again. A failure must leave state unchanged.
- Abandon releases only the matching lock, without a refund or rewards; replaying it must be harmless.
- State mutations return a consistent new overview. The adapter must use the current Player/AcademicRecord
  dynamically after Continue, not cache a replaced object.
- Rest, Continue and New Game must respect the same active-activity rule. The checkpoint must include completed
  results and preparation points. Do not save an unfinished QuestionSession.

After those APIs are agreed, create the page on the EDT with the **same shared state** as the other buildings:

```java
ClassChallengeView lecture = ClassChallengeFactory.create(
        sharedChallengeStateAdapter, () -> navigation.setActiveView("street"));
```

`sharedChallengeStateAdapter` is the proposed external adapter, not an existing concrete class.
Replace GameView's lecture placeholder with this page. The page refreshes when shown; `refresh()` can also
be called explicitly after a state replacement. Use `requestLeave()` when navigating away or closing a
window, so an unfinished challenge goes through confirmation. It invokes the supplied navigation callback
only after the state adapter successfully releases the activity.

No production fake player, hard-coded enrolment, duplicate academic entities, scraper, exam rules, or save
schema have been added. `FakeChallengeState` and the preview exist only under `src/test/java`.

## Shared question API for UC3

```java
QuestionGateway gateway = new HuggingFaceQuestionGateway();
List<Question> exam = gateway.getQuestions(
        QuestionTopic.SOFTWARE_DESIGN, QuestionPurpose.EXAM, 5);
```

The gateway returns exactly the requested number of distinct questions, or throws `QuestionLoadException`.
The selection supports one to three PRACTICE questions and one to five EXAM questions per course.
Existing three-question exam requests retain the same questions and ordering. UC3 owns answer hiding,
hint eligibility, energy policy, grading and final exam persistence.

`QuestionSession` supports editing exam answers, checking completeness, counting correct answers and using
one hint. `useHint(index)` returns the index of one wrong option to hide; it does not award points or change
answers. UC2 never uses the exam hint. Correct answers are absent from classroom output until feedback.

## Six courses and introductory selection

| Course / QuestionTopic | MMLU subject | Practice row IDs | Exam row IDs |
|---|---|---|---|
| Programming Foundation / PROGRAMMING_FOUNDATION | high_school_computer_science | 1, 21, 29 | 59, 60, 72, 65, 83 |
| Software Design / SOFTWARE_DESIGN | high_school_computer_science | 4, 20, 34 | 57, 63, 93, 69, 71 |
| Cell Biology / CELL_BIOLOGY | high_school_biology | 5, 18, 19 | 62, 67, 97, 84, 99 |
| Ecology / ECOLOGY | high_school_biology | 8, 21, 26 | 64, 82, 91, 79, 88 |
| World History / WORLD_HISTORY | high_school_world_history | 15, 23, 49 | 53, 57, 89, 62, 68 |
| Historical Perspective / HISTORICAL_PERSPECTIVE | high_school_world_history | 5, 38, 41 | 59, 64, 65, 79, 97 |

The provider is a dataset API, not a question-generation service. It has **no difficulty setting**. These
remote selections favour elementary operations, specifications/testing/documentation, cell structures,
ecosystems, world events and source interpretation. History still includes reading passages; there is no
claim of a measured easy difficulty or exact coverage of a UofT syllabus. Data quality and answer keys remain
provider limitations; the source answer is displayed rather than inventing an explanation.

Practice fetches rows 0–49; exams fetch 50–99. Only row references are stored in production, never question
text or answers. Every call fetches the remote page again; those references provide no offline behaviour.
The two purposes have disjoint question IDs per course. A cancelled retry uses the same practice selection
and costs energy again, but there is still at most one completed/rewarded challenge per course.

Example endpoint:

```text
https://datasets-server.huggingface.co/rows?dataset=cais%2Fmmlu&config=high_school_biology&split=test&offset=0&length=50
```

Transport and request timeouts are five seconds. Non-200 HTTP status, network failure, incomplete response,
missing selected rows, truncated cells, incorrect subject, invalid choices, and invalid answer indexes all
fail explicitly. Thread interruption is preserved. There is no fallback or disk cache. Test JSON fixtures
are synthetic and never included in the production classpath.

## Run and verify

Normal checks (no live service required):

```sh
mvn test
```

The UI tests exercise selection, confirmation, duplicate-start protection, background loading, empty-answer
feedback, all three submissions, completion, navigation after success, and API error/retry presentation.
Interactor tests also cover enrolment, completed-result viewing, exact/insufficient energy, early exit,
stale requests, concurrency, and failed completion retry. HTTP tests use a local test server or injected
responses. Tests with the fake state validate UC2 and the proposed port, not the unfinished team adapter.

Live API check on macOS/Linux, after building:

```sh
mvn test-compile
java -cp "target/classes:$HOME/.m2/repository/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar" app.QuestionApiCheck
```

Verified on 2026-11-23 with Java 17 compiling with `--release 11`: all 12 course/purpose requests returned
the requested three practice or five exam questions (48 total). An initial service HTTP 502 propagated
as an error; a separate manual rerun passed all requests. The check has no automatic fallback or API key.

To preview the Swing page with test state and real questions:

```sh
java -cp "target/test-classes:target/classes:$HOME/.m2/repository/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar" view.ClassChallengePreview cs
```

Replace `cs` with `biology` or `history`. IntelliJ can also run this test-source main class directly.
This preview cannot verify real UC1 enrolment, shared energy, Rest/Continue locking or checkpoint restoration;
those need the shared adapter. The shared app's current lecture placeholder is intentionally unchanged.

Optional UI images for review:

```sh
mvn test -Duc2.render.dir=/tmp/uc2-ui-review
```

The initial UC2 implementation was merged into `yuanyx233/team-project` main through PR #13. Further
changes should be reviewed by two teammates before merging, as required by TeamContract.md.
