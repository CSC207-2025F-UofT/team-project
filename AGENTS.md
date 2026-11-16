# AGENTS.md — SpellBook (CSC207 team project)

Java 11 + Swing, built with Maven. Run with `mvn compile exec:java -Dexec.mainClass=app.Main`.

Every change must follow **Clean Architecture** and the **SOLID principles** as taught in
CSC207 (course notes ch. 8–11). Treat the rules below as requirements, not suggestions. If a
request can't be done without breaking one, say so and propose a design that keeps it.

## 0. The idea behind every rule

A program is a set of **policies**, and each one has a **level**: how far it is from input and output.

- **High-level policies** are the core logic, for example "a spell can't be cast without enough mana".
  They hold no matter how the player presses keys or how data is stored.
- **Low-level policies** sit close to I/O, for example "W means up", "draw frame 3 of the walk
  sprite" or "save to `scores.json`". They change often and for unrelated reasons.

Keep high-level policies independent of low-level ones. That's what the layers, the
Dependency Rule and SOLID are for: **low coupling, high cohesion**. When a rule below seems
unclear, ask "does this make core game logic depend on a detail?" If it does, the design is wrong.

## 1. The Dependency Rule (never break this)

Source-code dependencies point **inward only**, from lower-level to higher-level policy. An
inner layer never imports, names, or otherwise knows about a class in an outer layer.

```
app  →  view / data_access  →  interface_adapter  →  use_case  →  entity
(outermost, lowest level)                                    (innermost, highest level)
```

| Package | CA layer | May import | Must NOT import |
| --- | --- | --- | --- |
| `entity` | Entities (enterprise rules, highest-level policy) | `java.*` basics only | `use_case`, `interface_adapter`, `view`, `data_access`, `app`, `javax.swing`, `java.awt`, file/network/JSON libs |
| `use_case/<name>` | Use Cases (application rules) | `entity`, its own package, shared use-case interfaces | `interface_adapter`, `view`, `data_access`, `app`, Swing/AWT, file/network/JSON libs |
| `interface_adapter/<feature>` | Interface Adapters | `use_case`, `entity` (only when unavoidable) | `view`, `data_access`, `app`, Swing/AWT |
| `view` | Frameworks & Drivers | `interface_adapter`, Swing/AWT | `use_case`, `entity`, `data_access`, `app` |
| `data_access` | Frameworks & Drivers | `use_case` (to implement its DAO interfaces), `entity`, file/JSON/HTTP libs | `interface_adapter`, `view`, `app` |
| `app` | Main component | everything | — |

Data still has to flow outward (interactor → presenter → view). The rule allows that only
through **dependency inversion**: the inner layer owns an interface, and the outer layer
implements it. `UpdateGameOutputBoundary` lives in `use_case` and `GamePresenter` implements
it. The interactor never learns the presenter exists.

The boundary interfaces (Input Boundary, Output Boundary, Data Access Interface) **belong to
the use-case layer**. Never put one in `interface_adapter` or `data_access`.

**Self-check before finishing.** Every command must print nothing:

```sh
grep -rnE '^import (view|app|data_access)\.' src/main/java/entity src/main/java/use_case src/main/java/interface_adapter
grep -rnE '^import interface_adapter\.' src/main/java/entity src/main/java/use_case
grep -rnE '^import use_case\.' src/main/java/entity
grep -rnE '^import (use_case|entity|data_access|app)\.' src/main/java/view
grep -rnE '^import (javax\.swing|java\.awt|java\.io|java\.net)' src/main/java/entity src/main/java/use_case src/main/java/interface_adapter
grep -rn 'instanceof' src/main/java
```

## 2. The CA engine: one use case = this set of classes

Each user goal (one use case in README.md) gets its own package `use_case/<verb_noun>/`.
A single click, keypress or screen change is **not** a use case. Design around one coherent
user goal.

- `<Name>InputBoundary`: interface. The Interactor implements it and the Controller calls it.
  Its method returns **`void`**. Results go out through the presenter, never back to the controller.
- `<Name>InputData`: immutable. The controller builds it, in the form most convenient for the interactor.
- `<Name>Interactor`: the application rules. It knows only boundaries, input/output data,
  data access interfaces, and entities. It may call the data access interface several times
  (read, change entities, save) before it presents a result.
- `<Name>OutputBoundary`: interface the Presenter implements. It has a success method, plus a
  fail method such as `prepareFailView(String)` wherever the use case has a failure path.
- `<Name>OutputData`: immutable result passed to the presenter.
- `<Name>DataAccessInterface`: interface **owned by the use case**, listing only the
  methods this interactor needs (ISP). Its implementation lives in `data_access`.

And in `interface_adapter/<feature>/`:

- **Controller**: converts raw UI input into domain-friendly values (key states into a move
  direction, text into a number), wraps them in `InputData`, and calls the input boundary.
  It holds no rules and no `if` that decides game behaviour.
- **Presenter**: implements the output boundary. It turns `OutputData` into display-ready
  strings and numbers, writes them to the ViewModel, and fires its change notification.
  It holds no rules.
- **ViewModel + State**: plain storage for what the view shows. It notifies the view when it
  changes (observer / property-change style). The view never pulls from entities.

In Frameworks & Drivers:

- **View**: Swing only. An event listener (key, click, timer) is low-level policy. It belongs in
  `view` and immediately calls a controller method. The view reads only its ViewModel.
- **Data access**: reads and writes files, JSON or the network, implements the use case's data
  access interface, and builds the entity objects the interactor works with.

Data flows View → Controller → InputBoundary/Interactor → (DataAccess, Entities) → OutputBoundary/Presenter
→ ViewModel → View. The View and the Entities never know about each other. That
coupling is the MVC flaw that CA and MVP were designed to remove.

Copy the shape of the existing `use_case/update_game` + `interface_adapter/game` code
when adding a new feature.

## 3. Where code goes

- **Enterprise rules** hold for the whole game no matter which use case runs, so they go in
  **entities**. Examples: health never goes below 0 or above max, a spell's mana cost and cooldown.
  An entity enforces its own invariants. Don't leave that to callers.
- **Application rules** are specific to one user goal, so they go in **that interactor**.
  Examples: what happens on one game tick, when a wave counts as cleared.
- Never put either kind in a controller, presenter, view, data access class, or `GameLoop`.
- **Formatting** (text, colours, which sprite or animation, rounding for display) goes in the
  **presenter / view**, never in the interactor or entity.
- **Persistence, files, APIs, JSON, network** go in a `data_access` class that implements a
  use-case-owned interface. Interactors never touch `java.io`, HTTP clients, or JSON types.
- **Swing/AWT** (`JPanel`, `KeyEvent`, `Graphics`, `Color`, `BufferedImage`) is allowed only in
  `view` (and `app` for wiring).
- **Wiring** (`new`, choosing which implementation to plug in) happens only in `app` (`Main`,
  or a builder class in `app`). Inner layers receive dependencies through their constructors
  and never call `new` on an outer-layer class. A test can act as a different "main" by wiring
  fakes instead.
- `GameLoop` only decides *when* things run. It calls controllers, never interactors or entities directly.

## 4. SOLID checklist (apply to every class you add or change)

- **SRP: one reason to change.** "Reason" means an **actor**: one person, group or business
  function that could ask for the change. Group things that change for the same reason and
  separate things that change for different reasons. If a class mixes rules with display,
  display with storage, or two features' rules, split it. Don't grow `UpdateGameInteractor`
  into a god class. A new user goal gets its own use case.
- **OCP: open for extension, closed for modification.** Add behaviour the way a plugin would:
  write a new class behind an existing interface (a new `Spell` or `Enemy` implementation)
  instead of editing working code. Adding a case to an `if`/`switch`/`instanceof` on type in
  existing code is the sign you missed an abstraction.
- **LSP: subtypes must be substitutable.** Every subtype honours its supertype's documented
  contract. No overrides that throw `UnsupportedOperationException`, silently do nothing,
  tighten preconditions, or break an invariant the caller relies on. "Is-a" in the real world
  doesn't justify `extends`: a square is a rectangle, but `Square extends Rectangle` breaks
  `setWidth`/`setHeight`. If a subtype can't do something, the abstraction is wrong
  (`Bird.fly()` / `Penguin`). Split it (`FlyingBird`), use a smaller interface, or use
  composition. The contract has to be written down, so give every interface and abstract
  method Javadoc that states what callers may expect.
- **ISP: no one depends on methods they don't use.** Look at it from both sides.
  *Implementers*: no class should be forced to fill in methods that don't apply to it.
  *Clients*: no class should depend on an interface wider than it needs, because changes to the
  unused methods still ripple into it. Each interactor gets its own small data access interface.
  One DAO class may implement several of them. Don't dodge ISP with `default` methods that do nothing.
- **DIP: depend on abstractions, and high-level code owns them.** Interactors hold
  `OutputBoundary` / `DataAccessInterface` fields, never `Presenter` or a concrete DAO.
  Controllers hold `InputBoundary`, never the `Interactor` class. Keep these interfaces
  **stable**: shape them around what the use case needs, not around how the current
  implementation works. Only `app` names the concrete low-level classes.

## 5. Design before code

For a new feature:

1. **User story** in README.md: "As a [kind of player], I want to [goal] so that [benefit]."
   Check its scope. If it holds several goals, it's an epic: split it ("manage my spellbook").
   If it names a class, screen or algorithm, it's a task: find the goal behind it ("build the
   HUD panel"). One story should lead to one use case and be something one teammate can make
   real progress on.
2. **Use case** in README.md, written as intent and observable behaviour, with no class names,
   method calls, widgets or storage details:
   - **Name**: an active verb phrase for the actor's goal ("Cast a spell").
   - **Primary actor**: who starts it. The system is never an actor.
   - **Preconditions**: facts already true, not setup steps.
   - **Trigger**, **Success outcome** (observable and testable), and a numbered **Main success scenario**.
   - **Extensions**: each tied to a step number (`2a.`), saying what the player observes and
     whether state changes.
   - Keep separate goals and separate actors in separate use cases.
3. **Noun–verb analysis**: nouns are candidate classes or fields, verb phrases are candidate
   responsibilities. Most nouns end up as values, not classes (a colour, a name, a fixed choice
   that fits an `enum`). Assign each responsibility to one class. Use an **interface**, not a
   subclass, when a capability cuts across a hierarchy (Java allows only one superclass).
4. **Scenario walk-through**: trace the main path and every extension against the design.
   Whenever a step has no responsible class, or a class needs data it can't get, fix the design
   and start over. Stop when a full pass needs no changes.
5. Then build the CA engine for that one use case (section 2), wire it in `app`, and update
   the README "Architecture" section. Larger features can get a spec in `docs/specs/`.
6. Every extension (failure path) becomes a presenter fail view or a distinct output, never an
   exception that escapes to the view.

## 6. Testing

- Interactors must be testable with **no Swing and no real files**. Use a fake `OutputBoundary`
  that records what it received and an in-memory `DataAccessInterface`. If you can't, the design
  is leaking a detail. Fix the design, not the test.
- Test presenters by checking the ViewModel state they produce.
- Put tests in `src/test/java` mirroring the main package. Cover the main success scenario
  and **each extension** of the use case.

## 7. Red flags: stop and redesign if you see these

- `import javax.swing`, `java.awt`, `java.io`, `java.net`, or a JSON/HTTP library inside `entity` or `use_case`.
- `import view.*`, `import data_access.*` or `import app.*` from anywhere except `app`.
- A boundary or data access interface that lives outside the `use_case` package.
- A controller, presenter, view listener or `GameLoop` containing an `if` that decides a game rule.
- An interactor that returns a value to the controller, or calls a presenter by its concrete class.
- An entity that knows about the screen, a sprite, a key code, or a file.
- `instanceof` / type-switching to choose behaviour.
- One interface that every implementer only partly fills in, or a `default` method added so they don't have to.
- An override that throws `UnsupportedOperationException` or does nothing.
- `new SomeConcreteOuterClass()` anywhere but `app`.
- A class whose name needs "And" or "Manager" to describe it.

## 8. Code style

- Keep the existing layout: `app`, `entity`, `use_case/<name>`, `interface_adapter/<feature>`,
  `view`, plus `data_access` when persistence arrives.
- Short Javadoc on each class saying its single job (as the existing classes do). Javadoc on
  every interface method stating its contract (see LSP).
- Prefer clear `if` statements over ternaries and lambdas over method references, matching recent refactors.
- Update the README "Architecture" section when you add a use case or layer class.

## 9. Before you say "done"

1. Run the §1 self-check greps. Every command must print nothing.
2. Go through §4 (SOLID) and §7 (red flags) for each class you touched.
3. `mvn -q test` passes, with tests for the success path and each extension.
4. README use case and "Architecture" section match the code.
5. Commit messages follow §10.
6. In your reply, name any rule you bent and why. Silence means none were bent.

## 10. Commits

- Format: `type(scope): subject`. Scope is optional.
- Types: `feat`, `fix`, `refactor`, `tune` (balance/number tweaks), `test`, `docs`, `chore`, `revert`.
- Subject: lowercase, imperative ("add", not "added"), no trailing period.
- Body only when needed: what changed and why.
- No AI co-authors or AI attribution lines in commits or PRs.
- Branches: `feat/...`, `fix/...`, etc. Example: `feat/enemy-spawning`.

Example: `feat(player): add WASD and arrow key movement`
