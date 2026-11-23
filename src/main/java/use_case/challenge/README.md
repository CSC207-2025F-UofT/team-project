# UC2 — Complete a Class Challenge

**Responsible member:** Xinjia Shen. **Building:** Lecture Hall.

This package owns course/status listing, participation confirmation, eligibility checks, question loading,
per-question feedback, cancellation, and challenge completion.

- `ClassChallengeInteractor` implements the input boundary and applies the workflow rules.
- Input/output boundaries and data classes carry requests and results across the use-case boundary.
- `ChallengeCourse` and `ChallengeOverview` are immutable views of shared academic/player data.
- `ChallengeStateGateway` defines the shared-state operations required by UC2. The actual team adapter is pending.
- `ChallengeStateException` reports state or eligibility failures.

A challenge has three questions, costs 20 Energy, and awards one preparation point per correct answer.
Questions load before charging. Confirmed early exit keeps the cost and records no completion or points.
Attempt IDs and question indexes protect against duplicate or stale requests.

The interactor depends on domain objects and interfaces. Swing, HTTP and JSON stay in outer packages.
