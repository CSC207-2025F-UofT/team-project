# Shared job contract and Ling compatibility review

This is the implemented UC4 contract on `feature/finder`. It is available for the
Profile/Recommendation feature to consume; it is not a claim that the team has
already approved or integrated those features.

## Reviewed snapshot

- Main: `c833a7382dabe5fb2841e8aae55ff105b0668128`.
- Ling's PR #4 / `LM-UML`: `c5d2075c984d31e9ed983735580ae48b1463d033`.
- The PR adds README content and seven PlantUML diagrams with SVG/PNG exports.
  It adds no Profile/Recommendation Java implementation. Java files on that branch
  are older starter skeletons, including an empty `Internship`.
- A three-way trial merge of our synchronized branch with Ling's branch completed
  without conflicts before implementation. Recheck against the latest remote refs
  before integration; unpushed work cannot be evaluated.

## Clean Architecture findings

The three class diagrams show Controllers depending on Input Boundaries,
Interactors depending on Output Boundaries and repository/job-source interfaces,
and Presenters implementing those Output Boundaries. Recommendation matching is
delegated through `InternshipMatcher`. None of these depicted dependencies
obviously violates the Dependency Rule.

The diagrams do not declare all package locations or implement the DAO/DTO classes.
Therefore this is a design review, not proof that executable code follows CA.
For implementation:

- Input/output boundaries, `ProfileRepository`, `JobSource`, their error/data types,
  and profile validation/matching policies must belong to inner layers.
- `User` and `JobPosting` must not import presenters, Swing, HTTP, JSON or storage code.
- File and HTTP adapters implement the inward-facing interfaces.
- The worker executor and UI executor remain in outer adapters/composition code.

Minor documentation issues in Ling's snapshot: the recommendation sequence
references a "Diagram 13" that is not included, and sequence error arguments are
more detailed than the single `ProfileError` argument shown in class diagrams.
These do not block Finder implementation, but should be resolved when the
Profile/Recommendation source and diagrams are synchronized.

## One canonical model and source

| Concern | Implemented contract | Ling integration action |
| --- | --- | --- |
| Job entity | `entity.JobPosting` | Replace UML/type references to `Internship`; reuse main's entity rather than introducing a second model. |
| Retrieval | `use_case.port.JobSource.fetch() throws JobSourceException` | Same operation name as Ling's UML; add the checked exception to source declarations. |
| Success result | `JobFetchResult` | Use `getJobs()`, `getSuccessCount()`, `getFailedBoards()`; detailed reasons are in `getFailures()`. |
| All boards fail / none configured | `JobSourceException` | Catch and present an error, as shown in Ling's sequence. Do not turn it into an empty successful list. |
| Partial success | Immutable successful jobs plus failed-board information | Preserve warnings even if recommendation/search returns zero matches. |
| Internship identification | `use_case.common.InternshipClassifier.isInternship(title)` | Reuse this rule before profile-specific filtering and ranking. |
| Unknown work arrangement | `entity.WorkArrangement.UNKNOWN` | Keep unknown values visible; Greenhouse list results do not reliably provide this field. |

`JobPosting.Builder(source, boardToken, externalJobId)` requires a title, company
and application URL before `build()`. Location and description default to empty
strings. Work arrangement defaults to UNKNOWN. Blank mandatory values and colons
in identity components are rejected. Snapshots are immutable.

`getStableId()` is `source:boardToken:externalJobId`, for example
`greenhouse:figma:12345`. Two boards can reuse a numeric job ID without colliding.
Consumers should use this key for saved-job identity, not object identity, title,
or result-row index.

The job description is already plain text. Missing location remains an empty
string in the entity; only the Presenter supplies the display label
"Not specified." Return collections are defensive, unmodifiable copies.
Recommendation ranking must create its own sorted list rather than sorting
`getJobs()` in place.

The shared result deliberately retrieves all postings. Each use case applies the
shared internship classifier, then its own search or recommendation policy.
Finder does not call a concrete Recommendation Interactor or access saved profiles.

## Data access and failure rules

`data_access.GreenhouseJobSource` implements `JobSource` using an injected
Java 11 HTTP client, boards endpoint, company configuration and request timeout.
It asks for `content=true`, checks status codes, decodes HTML, and maps snapshots.

A successful empty board increments the success count. HTTP errors, timeouts,
network errors and invalid JSON/records mark that board unavailable. A malformed
record invalidates its board response so incomplete data is not silently shown
as complete. Other boards continue. Interruption aborts retrieval, preserves
the interrupt flag and throws `JobSourceException`.

The recommended application configuration is a non-empty list of boards.
If the source itself is constructed with none, `fetch()` throws instead of
claiming a successful zero result.

## Ownership

Finder implements UC4, the shared job model/source, and its own presentation.
Ling retains ownership of profile validation, profile persistence, preference
filtering, skill ranking and the UC5-7 UI. Tracker retains saved applications,
duplicate prevention and application status. Opening an application link does
not create or change an application record.

The resulting Java source is the integration reference. Update the design diagrams
to match these signatures when those modules are implemented.
