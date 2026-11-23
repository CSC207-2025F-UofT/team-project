# Production code and UC2 responsibilities

Production Java packages share this source root. Tests live in `src/test/java`.

| Package | Responsibility | Xinjia Shen's contribution |
|---|---|---|
| `app` | Construct and connect the application objects | [ClassChallengeFactory and QuestionApiCheck](app/README.md) |
| `entity` | Game rules and domain objects | [Question, QuestionSession and ChallengeResult](entity/README.md) |
| `use_case` | Application workflows and dependency interfaces | [UC2 class challenges](use_case/challenge/README.md) and the [shared question contract](use_case/question/README.md) |
| `interface_adapter` | Convert controller input and presenter output | [UC2 controller, presenter and view model](interface_adapter/challenge/README.md) |
| `data_access` | HTTP, storage and other external implementations | [Shared Hugging Face question adapter](data_access/question/README.md) |
| `view` | Swing screens and navigation | [ClassChallengeView and its navigation connection](view/README.md) |

UC2 is Complete a Class Challenge. The question API is shared by UC2 and UC3.
Shared folders contain teammates' files as well; the linked descriptions identify UC2's scope.
The main source root contains the Clean Architecture layers, rather than separate copies for each member.

See [UC2 integration](../../../docs/uc2-integration.md) for the proposed state interfaces and pending connections.
