# Package 3: Object Actions, Swing View, and Controller

Implements [Issue #14](https://github.com/teafrea/team-project/issues/14) and the
[Package 3 work split](../README.md#package-3-object-actions-swing-view-and-controller).
The action contracts follow the [behavior design](diagrams/escape-room-behavior.puml).

## Build and preview

Requires JDK 11 or newer and Maven. Compile and run the behavior and headless Swing tests:

```sh
mvn test
```

After the dependencies have been downloaded, `mvn -o test` also runs offline.
The compiler uses `--release 11`.

Launch the local integration preview after compiling the tests:

```sh
java -cp target/classes:target/test-classes preview.Package3Preview
```

The preview uses the production view, controller, interaction interactor, assembler,
and domain classes with a small in-memory laboratory story. Open the drawer, read
the inscription, answer `007`, unlock the door, and escape. The two gates also work
in the opposite order. The preview's Save and Load buttons report that file adapters
will be connected by Package 5; they do not write or load a save file.

To render the same view without a display:

```sh
java -Djava.awt.headless=true -cp target/classes:target/test-classes \
  preview.Package3Preview --screenshot target/package3-preview.png
```

## Implemented behavior

`Interactable` owns its state, allowed values, descriptions by `mode`, actions,
and completed nonrepeatable action IDs. It has no lock. Its constructor validates
the state configuration and makes defensive copies. `snapshot()` returns a detached,
immutable `entity.state.ObjectState` for Package 5.

`Action`, `ActionPlan`, `ObjectStateCondition`, `AllOfCondition`, and
`SetObjectStateEffect` are immutable. The supported conditions read room-local
object state; effects only assign an accepted object state value. They receive
neither inventory nor game access. New condition types implement the pure, immutable,
deterministic `Condition` contract without changing action or conjunction logic;
template adapters register their configuration tags. Conditions must not mutate
runtime state or perform I/O. The current MVP supports only `SetObjectStateEffect`,
so action configuration rejects other effect implementations before execution.

`Game.interact(objectId, actionId, Optional<String> itemId)`:

1. Checks active status and resolves the object/action in the current room.
2. Returns unchanged feedback for a completed one-shot action before tool validation.
3. Resolves the selected tool from the actual inventory, then validates the exact
   required ID, conditions, and every state-effect target.
4. Preflights every appearing item. Held IDs are skipped; every other ID must identify
   a remaining item in the current room.
5. Applies the validated state effects and completion record, then transfers the same
   item instances to inventory and marks them revealed.

An invalid command changes no state or item location. Only newly acquired names
are appended to the result. Repeatable actions can change state again without
duplicating held items or notices. `changed` reflects final object values, completion
records, and item transfers, including a net-zero state update.

`Room` keeps its state-access capability private. Prepared plans are bound to their
exact runtime object and configured action; a stale or foreign plan is rejected
before mutation. The original Room constructor and getters remain available.

`InteractInteractor` resolves `GameContext` for each command and always presents fresh
immutable output, including failures and unchanged results. It does not cache the
session or use snapshots to assemble display data.

`GameController` receives all seven focused input boundaries and a `CommandDispatcher`.
Every entry point goes through that dispatcher. `GameView` is a headless-testable
`JPanel`; an application builder can place it in a `JFrame`. It renders immutable
view state and listens for view-model updates. Controls cover stories, held tools,
object actions, door requirements, questions and answers, movement, save/load, and
feedback. IDs are retained for routing even when labels are identical.
Action labels include the object's display name. Multiple-choice questions provide
a wrapped, scrollable preview of the complete selected option, including long answers
that share the same prefix.

Busy-state changes preserve the selected story, held tool, and answer text while
disabling command inputs.
Fresh output clears old story/tool/answer selections, including after load. Movement
uses the output's `canMove` and `finalRoom` flags for a single Next room / Escape button.
Rendering and queries never obtain items or replay earlier acquisition feedback.

## Integration contracts

The repository initially contained only `Room.java`. Shared value types and concrete
baseline dependencies are included so Package 3 compiles and can be tested now.
Keep the following handoffs when extending the other packages:

| Owner | Connection |
| --- | --- |
| Package 1 | Inject all boundaries into `GameController`; supply the serial `CommandDispatcher` and application assembly. Story-template validation owns appearance provenance and authored puzzle reachability. |
| Package 2 | Coordinate `Game` and `Room`; keep the acquisition helpers, `GameContext`, `DomainResult`, immutable output DTOs, and `GameOutputAssembler` signatures compatible. |
| Package 4 | Supply the presenter and EDT dispatchers, and extend lock/Trivia configuration and API retrieval. Update `GameViewModel` on the EDT, constructing `GameViewState(data, currentBusy)` inside that callback. Render the result before Package 1 releases busy and its admission guard. |
| Package 5 | Consume `Interactable.snapshot()` / `ObjectState`; add complete aggregate snapshots and validated restoration. Inject save/load boundaries and preserve the existing output path. |

### Ownership of the current files

The ownership below follows the README work split. Including a shared dependency
here does not transfer its final implementation or review responsibility to Package 3.

| Scope | Files or methods |
| --- | --- |
| Package 3 deliverables | `Interactable`, `Action`, `ActionPlan`, the condition/effect types, `RoomReadState`, `RoomMutation`, `InteractInputBoundary`, `InteractInteractor`, `GameController`, `GameView`, and their tests. |
| Package 3 methods in shared files | `Game.interact()`; `Room.prepareInteraction()`, `validatePlan()`, `applyInteraction()`, and private `StateAccess`; object-level `Interactable.snapshot()` production. |
| Package 1 supporting baseline | Maven test/compiler configuration and the `CommandDispatcher` interface. The actual serial worker and application builder remain Package 1's responsibility. |
| Package 2 supporting baseline | Remaining `Game`/`Room` core, `Collectable`, `Inventory`, `ActionOption`, domain result/status/error types, `GameContext`, movement boundary, output boundary/assembler and response DTOs. |
| Package 4 supporting baseline | `Lock`, `ItemLock`, `TriviaLock`, `Trivia`, `LockKind`, item/Trivia input boundaries, `GameViewState`, and `GameViewModel`. |
| Package 5 supporting contract | Immutable `ObjectState` and save/load input boundaries. Snapshot schema, validated restoration, and the object completion-state construction contract require coordination with Package 5. |

The baseline world, items, inventory, locks, output DTOs, and view-model types have
real behavior. Template parsing, API fetching, full save snapshots/restoration,
and final application assembly remain with their assigned packages. The integration
preview is under test sources and is not the final application's entry point.

Important signatures:

```java
new GameController(start, interact, useItem, trivia, move, save, load, commands);
new GameView(controller, viewModel);
new InteractInteractor(context, assembler, output);
new GameViewState(outputData, busy);

assembler.fromGame(game, message, activeLockId); // activeLockId: Optional<String>
assembler.fromStories(stories, message);
assembler.fromNoGame(message);
```

`GameOutputData.objectDescriptions()` maps stable object IDs to display text
(object name plus current description). The view displays the text, while action
DTOs carry their separate object/action IDs. An action's display label includes its
object name so identical choices such as Open remain distinguishable.
`TriviaData` contains prompt and choices
only; expected answers never enter UI output.

## Verification

| Test | Behavior covered |
| --- | --- |
| `ActionModelTest` | Config validation, extensible pure conditions, effects, defensive copies, snapshots, plan identity and stale-plan rejection, one-shot completion, net-zero updates. |
| `GameInteractionTest` | Whole-command atomicity, same-instance transfer, actual held tools, current-room access, cross-object puzzles, different actions naming a held item, repeatable effects, no-ops, queries, gate and escape checks. |
| `InteractInteractorTest` | Fresh success/error/no-op output, no-game feedback, malformed input, and session replacement. |
| `GameOutputAssemblerTest` | Immediate inventory/gate/movement refresh, distinguishable action labels, immutable output, read-only assembly, and answer-free question data. |
| `GameControllerTest` | Deferred routing of every command and exact stable-ID arguments. |
| `GameViewTest` | Actual Swing controls on the EDT, duplicate labels, busy handling, load selection reset, complete long-choice previews/submission, answer input, menu/escape behavior, and text layout. |
| `Package3FlowTest` | Real button clicks through controller, interactor, domain, assembler, model, and view: drawer discovery, clue reading, wrong/correct answer, unlocking, and escape. |
