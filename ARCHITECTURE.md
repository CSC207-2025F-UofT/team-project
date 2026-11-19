# CityGuesser architecture guide

How the code is laid out and the decisions everyone agreed to follow. Read this before starting your use case.

## Running it
1. Open the project in IntelliJ as a Maven project (it downloads the dependencies).
2. Optional: set the `MAPBOX_ACCESS_TOKEN` environment variable to your Mapbox token (in IntelliJ: Run > Edit Configurations > Environment variables). **Never commit the token.** Without it the game shows a placeholder picture instead of a satellite image.
3. Run `app.Main`.
4. Run the tests with `mvn test` (or right-click `src/test/java` > Run All Tests).

## Packages (one layer per package)
| Package | What goes in it | May import |
|---|---|---|
| `entity` | `City`, `AnswerCity`, `Player`, `Round`, `RoundStatus`, `GameRules` | plain Java only |
| `use_case` | interactors, input/output boundaries and data, data access **interfaces** | `entity` |
| `interface_adapter` | controllers, presenters, view models, `ViewManagerModel` | `use_case`, `entity` |
| `data_access` | data access **classes** (JSON, Mapbox, in-memory, later SQLite) | `use_case`, `entity`, libraries |
| `view` | Swing screens and `ViewManager` | `interface_adapter` |
| `app` | `Main` and `AppBuilder` | everything |

`ArchitectureImportTest` fails if anything in `entity` or `use_case` imports Swing, AWT, SQL, JSON, HTTP, or an outer layer.

## File naming (same for all five use cases)
Each use case has a subpackage in both `use_case` and `interface_adapter`: `enter_name`, `start_round`, `submit_guess`, `finish_round`, `view_stats`.

Using Submit Guess as the example:
- `use_case/submit_guess/`: `SubmitGuessInputBoundary`, `SubmitGuessInputData`, `SubmitGuessInteractor`, `SubmitGuessOutputBoundary`, `SubmitGuessOutputData`, `SubmitGuessDataAccessInterface`
- `interface_adapter/submit_guess/`: `SubmitGuessController`, `SubmitGuessPresenter`, `SubmitGuessViewModel` (and a `SubmitGuessState` if you want one)

Each person creates their own files on their own branch. View models can extend `interface_adapter.ViewModel<T>`.

## Data access
Interfaces live in `use_case`. Each use case only sees the methods it needs.

| Data source | Use case | Interface | Methods |
|---|---|---|---|
| Player storage | Enter Name | `EnterNameDataAccessInterface` | `playerExists(name)`, `getPlayer(name)`, `savePlayer(player)` |
| Player storage | Finish Round | `FinishRoundDataAccessInterface` | `savePlayer(player)` |
| Player storage | View Stats | `ViewStatsDataAccessInterface` | `getPlayer(name)` |
| City dataset | Start Round | `StartRoundDataAccessInterface` | `getRandomAnswerCity()` |
| City dataset | Submit Guess | `SubmitGuessDataAccessInterface` | `findCityByName(name)` |
| Images | Start Round, Submit Guess | `shared/ImageDataAccessInterface` | `getImage(city, zoomStep)` returns raw bytes |

Classes in `data_access`, one per data source:
- `InMemoryPlayerDataAccessObject`: fake (HashMap). The SQLite version replaces it and implements the same three interfaces.
- `JSONCityDataAccessObject`: real. Reads `src/main/resources/data/answer_cities.json` (round targets, with landmarks) and `guessable_cities.json` (everything you can guess, with aliases like "NYC").
- `MapboxImageDataAccessObject`: real. Zoom step 1 to 5 maps to Mapbox zoom 17 to 13.
- `PlaceholderImageDataAccessObject`: fake. Same picture every time; used when there's no Mapbox token.

When something can't be read, saved or fetched, data access objects throw `use_case.shared.DataAccessException`. Interactors catch it and send an error to their presenter.

## Shared game state
`GameDataAccessObject` holds the current player and current round in memory. It isn't static or global: `AppBuilder` creates one and passes it in. Interactors only see it through these interfaces in `use_case/shared`:
- `CurrentPlayerDataAccessInterface`: `getCurrentPlayer()`, `setCurrentPlayer(player)`
- `CurrentRoundDataAccessInterface`: `getCurrentRound()`, `setCurrentRound(round)`

| Interactor | Current player | Current round |
|---|---|---|
| Enter Name | yes | |
| Start Round | | yes |
| Submit Guess | | yes |
| Finish Round | yes | yes |
| View Stats | yes | |

## AppBuilder
`app/AppBuilder` has one empty method per use case (`addEnterNameUseCase()`, etc.). Fill in only yours. Its comment says which data access objects your interactor gets. `AppBuilder` is the only file that names concrete data access classes, so swapping a fake for the real one is a one-line change there.

## Views
- Screens: `WelcomeView`, `MainMenuView`, `GameView`, `StatsView`, and the `ResultView` popup. `ViewManager` switches screens when `ViewManagerModel` changes.
- The buttons currently switch screens directly so the flow can be clicked through. Each one has a `TODO` saying which controller replaces it.
- **Rule:** views only call controllers and only read from view models. They never touch interactors, entities or data access.

## Decisions
1. **How a round ends.** Submit Guess's output data says whether the round is in progress, won or lost. Its presenter updates the Submit Guess view model, and `GameView` then calls the Finish Round controller. The Submit Guess and Finish Round interactors never call each other.
2. **Valid guesses.** Only cities in `guessable_cities.json` (including aliases). Anything else gets "not a known city" and doesn't use up an attempt. Empty guesses and repeat guesses don't use an attempt either.
3. **Formatting happens in presenters.** Interactors output plain data (distance as a number, direction as a value). Presenters turn it into text like "1,240 km northeast".
4. **Fakes and real classes behave the same.** For example, `getPlayer` returns `null` for an unknown name in both the in-memory and SQLite versions.
5. **Import check.** Nothing in `entity` or `use_case` imports Swing, SQL, JSON or HTTP classes (enforced by `ArchitectureImportTest`).
6. **Mapbox zoom numbers live in `MapboxImageDataAccessObject`.** Entities and use cases only know the zoom step 1 to 5 (`Round.getZoomStep()`).

## Who adds what to the entities
- Elliot: `distanceTo(City)` and `directionTo(City)` on `City`; deciding WON/LOST via `Round.setStatus`.
- Ingrid: `recordWin(attempt)` and `recordLoss()` on `Player`, using `GameRules`.
