# UC2 and shared question tests

**Responsible member for the following tests:** Xinjia Shen.

| Folder / file | Responsibility |
|---|---|
| `entity/QuestionSessionTest` | Question validation, answers, completeness and one-time hint use |
| `use_case/challenge/ClassChallengeInteractorTest` | UC2 completion, Energy boundaries, cancellation, duplicates, stale requests and failures |
| `interface_adapter/challenge/ChallengeAdapterTest` | Controller errors and presenter/view-model state |
| `data_access/question` | HTTP behaviour, MMLU parsing, requested counts, disjoint purposes and errors |
| `view/ClassChallengeViewTest` | Asynchronous Swing challenge and error flows |
| `view/GameViewTest` | Shared navigation tests; UC2 adds the injected Lecture Hall round trip |
| `support/FakeChallengeState` | Test-only implementation of the proposed UC2 state contract |
| `view/ClassChallengePreview` | Optional UI preview with test state and the real question API |

Run `mvn test` from the repository root. Normal tests use fixtures or a local test server; they do not
depend on Hugging Face being online. Test data and fake player/course state remain under this test source root.

Other tests in this shared folder are maintained by their respective use-case owners.
