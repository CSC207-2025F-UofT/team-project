# Team Project

# Project Name

- CocktailCraft

## Team Information

- Team name: Team400
- TUT: 0401 BA2200 Group4

### Team Members

- Qinli Yang
- Enhao Tang
- Jiaqi Shen
- Wenqian Ling

---

## Project Description

CocktailCraft is a Java Swing desktop application for discovering, customizing,
and managing cocktail recipes. Users will browse recipes, search by name and base
spirit, view recipe details and available images, save favourites, and create
personal recipe variations. A personal profile will provide access to each user's
favourites and customized recipes across application sessions.

The project currently provides a shared scaffold. The user stories below describe
planned behaviour; they are not claims that the features have been implemented.

### Domain

- Food: drinks

---

## User Stories

### User Story 1 [Qinli]

As a user, I want to search for cocktails by name and/or base spirit so that I can
find recipes that match my preferences.

### User Story 2 [Enhao]

As a user, I want to customize an existing cocktail and save it as my own recipe
so that I can revisit and edit my variations without changing the original recipe.

### User Story 3 [Jiaqi]
As a user, I want to register an account, log in and log out, and access my personal profile so that I can securely manage my account and revisit my favourite cocktails and customized recipes.
### User Story 4 [Wenqian]
As a user, I want to browse cocktails on the main page and save my favourite cocktails so that I can easily find and revisit them later.

---

## Use Cases

### Use Case 1 [Qinli]

**Inputs**

- An optional cocktail name.
- Zero or more selected base spirits.

**Input Requirement**

The user must enter a name, select at least one base spirit, or both.

**Normal Flow**

1. The user enters a name and/or selects base spirits in the Home search area.
2. The user clicks Search.
3. The system finds matching cocktails. Name matching is case-insensitive and
   supports partial matches. A cocktail may match any selected spirit; when both
   name and spirit criteria are provided, both must be satisfied.
4. The system displays matching cocktails using the same card layout as Home.
5. The user selects a card to open Cocktail Details.
6. The user can clear the search to return to catalog browsing.

**Alternative Flows**

- If neither criterion is provided, the system requests a name or base spirit.
- If no cocktails match, the system displays “No cocktails found.”
- If the catalog cannot be accessed, the system reports the failure and allows retry.

### Use Case 2 [Enhao]

**Inputs**

- An existing recipe selected from Cocktail Details, or the user's saved Custom Recipe.
- A recipe name, ingredient names, quantities, and units.

**Precondition**

The user is logged in.

**Normal Flow**

1. The user opens Cocktail Details and clicks Customize.
2. The system displays an editable copy of the original recipe.
3. The user changes its name, ingredients, quantities, and/or units.
4. The user clicks Save, and the system validates the recipe.
5. The system saves a separate Custom Recipe belonging to the current user.
6. The original recipe remains unchanged.
7. The user opens My Custom Cocktails from Profile to view saved variations.
8. The user can reopen a variation, edit it, and save the changes.
9. Saved variations remain available after restarting and logging in again.

**Alternative Flows**

- Guests are asked to log in before customizing.
- Blank recipe or ingredient names prompt the user to complete them.
- Quantities must be positive numbers, and units must be provided.
- A recipe must contain at least one ingredient.
- Cancel discards unsaved changes.
- Saving failures are reported, and the user's input is preserved for retry.
- An empty personal collection displays an empty-state message.

### Use Case 3 [Jiaqi]
INPUTS
- Username: The username entered by the user.
- Password: The password entered by the user.
- Confirm Password: Required only when registering a new account.
- Login, Sign Up, Profile, and Logout buttons.

MAIN SUCCESS SCENARIO
1. The user opens the Login page.
2. The user enters their username and password.
3. The user clicks the "Login" button.
4. The system verifies the username and password.
5. If the credentials are valid, the system logs the user in and establishes the current-user session.
6. The system navigates to the Home page after successful login.
7. The user can access their personal profile from the Home page, which displays their username and Favourite Cocktails.
8. When the user clicks "Logout", the system clears the current-user session and returns to the Login page.

EXTENSION

User does not have an account
  1. The user clicks the "Sign Up" button on the Login page.
  2. The system displays the Registration page.
  3. The user enters a username, password, and password confirmation.
  4. The user clicks the "Sign Up" button.
  5. The system checks that the required fields are completed, the passwords match, and the username is available.
  6. The system creates and saves the new account.
  7. The system displays a registration success message and returns to the Login page.
  8. The user continues from Step 2 of the Main Success Scenario.

Invalid username or password
  1. The system displays an error message indicating that the username or password is incorrect.
  2. The user can re-enter their credentials and try again.

Invalid registration information
  1. If the system cannot verify the account because the data service is unavailable, it displays an error message.
  2. The user remains on the Login page and can try again later.

### Use Case 4 [Wenqian]
PRECONDTION
1. The application is running.
2. The user is on the main page.

MAIN SUCCESS SCENARIO
1. User browses the cocktails.
2. User click the cocktail.
3. The system display the detail of this cocktail.
4. User selects a cocktail they like.
5. User clicks the "Favourite" button.
6. The system saves the selected cocktail to the user's favourites.
   
EXTENSION
1. If the cocktail is already saved, the system informs the user and ask user whether to remove it from favourites.
2. If saving fails, the system displays an error message.

(Add additional use cases as needed.)
---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Qinli | The user enters a cocktail name and/or selects one or more base spirits, then clicks Search. The system displays matching cocktail names or a message indicating missing input, no matches, or an API failure. |
| Enhao | The user selects an existing cocktail recipe, adds, removes, or replaces ingredients, and adjusts quantities. The user then names and saves the customized recipe. The system validates the input and saves it separately from the original recipe.  |
| Jiaqi | The users register a new account, log in with their username and password, and access their personal profile to access saved cocktails. The system saves user account information so that users can log in again after restarting the application.|
| Wenqian | The user clicks a cocktail, the system display the ingredients of it. The user click 'Favourite' button, then save this cocktail to home page.|
---

## Entities

Existing fields and planned additions are distinguished below. Planned entities
and contracts require team agreement before implementation.

### Entity 1

**Cocktail — implemented minimal version**

- `id`
- `name`
- `ingredients`: a list of `RecipeIngredient`
- Planned additions: `instructions` and optional `description`.
- Base-spirit classification and image metadata representation are not finalized.

### Entity 2

**Ingredient — under discussion; no separate Class currently exists**

- A stable ingredient ID and name may be needed.
- Classification/category fields should follow the agreed Search requirements.

### Entity 3
RecipeIngredient
- ingredient
- unit
- quantity
### Entity 4
User
- userId
- userName
- favourite
---

## API Usage

**API Name:** TheCocktailDB — planned; not implemented.

**Documentation:** [Official API documentation](https://www.thecocktaildb.com/api.php)

**How it is used in the program:** Planned image lookup for Cocktail Details,
with matching and fallback behaviour owned by Wenqian. The bundled Bar Assistant
recipe remains the recipe source. Exact API integration is still pending.

**API Name:** Course User API — candidate; not implemented.

**Documentation:** [Course Login example, db-user branch](https://github.com/CSC207-2026F-UofT/ca-login/tree/db-user)

**How it is used in the program:** Jiaqi will investigate Account persistence.
The example demonstrates username/password operations, but documentation for
associated-JSON Save/Load/Update still needs to be found. Storage of Favourites
and Custom Recipes through this API is not assumed.

The Bar Assistant dataset is a bundled offline snapshot; loading it does not
constitute runtime external API usage. User-data persistence is also outstanding.

---

## Design Diagrams

### UML Class Diagram

Current startup structure (individual use case classes are empty):

```mermaid
classDiagram
    class Main
    class AppBuilder
    class MainPageView
    class SearchView
    Main ..> AppBuilder : starts on Swing EDT
    AppBuilder ..> MainPageView : constructs
    AppBuilder ..> SearchView : constructs
    MainPageView o-- SearchView : opening search controls
```

Feature-specific diagrams will be added by their owners as implementations grow.
See [architecture handoff](docs/ARCHITECTURE.md) for the current package structure.

### Sequence Diagrams

```mermaid
sequenceDiagram
    participant Main
    participant EDT as Swing EDT
    participant AppBuilder
    participant Window as JFrame
    Main->>EDT: Schedule application startup
    EDT->>AppBuilder: build()
    AppBuilder->>Window: Create window with opening Home layout
    AppBuilder-->>EDT: Return window
    EDT->>Window: setVisible(true)
```

Search, Profile, Browse, and Customize sequences remain owner work.

---

## How to Run

### Setup

1. Open the Maven project in IntelliJ IDEA.
2. Use JDK 11 or newer. Maven targets Java 11 APIs and bytecode with `--release 11`.
3. Reload the Maven project to download dependencies.

### Launch the application

Run `org.example.cocktailcraft.app.Main`.

The application opens Home with a title, name input, disabled Search/base-spirit/
Clear controls, a disabled Profile button, and a catalog placeholder.
No other pages or navigation are implemented. Feature owners will add their own
Views and connect their use cases. The GUI does not load the dataset at startup.

### Verify dataset loading

Run `org.example.cocktailcraft.app.DataReadCheck`.

Expected output:

```text
Loaded cocktails: 663
Cocktail ID: daiquiri
Cocktail: Daiquiri
Light Rum: 60 ml
Lime juice: 22.5 ml
Simple Syrup: 22.5 ml
```

The check loads the full dataset once, then prints Daiquiri as a known sample.
It is not the application's Search functionality. GUI startup currently does not
load the catalog or make network requests.

### Run shared checks

From the project root:

```bash
mvn test
mvn verify
```

Both commands run the team Checkstyle configuration during `validate`.
`mvn verify` also builds the JAR. IntelliJ Build Project alone does not necessarily
run Maven tests or Checkstyle. The opening-window test is skipped in headless environments.

Reports are in `target/checkstyle-result.xml` and `target/surefire-reports/`.
`target/` is ignored by Git. The JAR is not a standalone executable bundle; Gson
remains a runtime dependency. Use IntelliJ for launching at this stage.

---

## Notes

### Local dataset and shared scaffold status

- The Bar Assistant snapshot contains 663 recipes, 296 ingredient records, and
  8 auxiliary JSON files. Images are not bundled.
- [Dataset source/version](src/main/resources/bar-assistant/SOURCE.md) and
  [upstream license](src/main/resources/bar-assistant/LICENSE) are included.
- `CocktailJsonReader.readAll()` returns a new unmodifiable `List<Cocktail>` per call;
  it does not cache, search, or save user data.
- `cocktail-index.txt` lists recipe resource paths and must be regenerated if recipe
  files are added or removed. The separate Daiquiri example is not counted twice.
- Existing Entity mapping covers ID, name, and ingredient name/amount/unit only;
  instructions/description are shared follow-up work.
- Implemented shared code includes Main/AppBuilder, the opening Home layout,
  offline JSON reading, JUnit checks, and Checkstyle. The four feature groups
  contain empty CA declarations; their Views and behaviour remain owner work.
- The structure follows the [course CA Login Lab](https://github.com/CSC207-2026F-UofT/ca-login/blob/main/LAB_INSTRUCTIONS.md).
- See [course checklist](docs/COURSE_REQUIREMENTS.md) for outstanding requirements.

### Temporary Team Guide — Remove Before Final Submission

> This section is a temporary handoff guide. Remove it before final submission.
> The four feature packages contain **empty class/interface declarations only**:
> no fields, constructors, methods, business logic, or feature wiring.
> Owners may change or remove these starting files as their use cases require.

#### Ownership

All Java paths below are relative to `src/main/java/org/example/cocktailcraft/`.
Each owner completes their feature's use case classes, adapters, Views, concrete
data access implementation, and tests.

| Owner | Feature | Packages and Views |
|---|---|---|
| Qinli | Search & Filter | `use_case/search/`, `interface_adapter/search/`, `view/search/`. `SearchView.java` currently contains only inactive controls for the opening Home layout. |
| Enhao | Customize | `use_case/customize/`, `interface_adapter/customize/`, `view/customize/`. The View folder is empty, tracked with `.gitkeep`. |
| Jiaqi | Profile and Account | `use_case/profile/`, `interface_adapter/profile/`, `view/profile/`. The View folder is empty, tracked with `.gitkeep`. |
| Wenqian | Browse, Details, and Favourites | `use_case/browse/`, `interface_adapter/browse/`, `view/browse/`. `MainPageView.java` currently contains only the opening Home layout. Other Views are not created yet. |

Owners decide whether to split their feature into additional use cases. No
Login, Details, Favourite, or Customize page has been implemented for them.

#### Starting files and their intended purpose

`<Feature>` means Search, Customize, Profile, or Browse. Every feature has the
same starting filenames. These declarations do not define final contracts.

| Package | File | Intended purpose when implemented |
|---|---|---|
| `use_case/<feature>/` | `<Feature>InputBoundary.java` | Entry interface for the use case. |
| `use_case/<feature>/` | `<Feature>InputData.java` | Data submitted to the use case. |
| `use_case/<feature>/` | `<Feature>Interactor.java` | Validation and business rules. |
| `use_case/<feature>/` | `<Feature>OutputBoundary.java` | Interface for reporting the outcome. |
| `use_case/<feature>/` | `<Feature>OutputData.java` | Data returned by a successful use case. |
| `use_case/<feature>/` | `<Feature>DataAccessInterface.java` | Storage operations required by the use case; implemented by a concrete DAO in `data_access/`. |
| `interface_adapter/<feature>/` | `<Feature>Controller.java` | Converts View input into a use case request. |
| `interface_adapter/<feature>/` | `<Feature>Presenter.java` | Converts use case output into display state. |
| `interface_adapter/<feature>/` | `<Feature>ViewModel.java` | Exposes display state and update notifications. |
| `interface_adapter/<feature>/` | `<Feature>State.java` | Holds fields displayed by the View. |

#### Implemented shared files

| File / location | What already works and what it is for |
|---|---|
| `app/Main.java` | Starts the application on the Swing EDT. |
| `app/AppBuilder.java` | Creates the JFrame and opening Home layout. Add completed feature wiring here later. |
| `view/browse/MainPageView.java` | Displays the Home title, search area, inactive Profile button, and catalog placeholder. No catalog behaviour. |
| `view/search/SearchView.java` | Displays name input and disabled search controls. No matching or event handling. |
| `entity/Cocktail.java` | Stores ID, name, and ingredients. |
| `entity/RecipeIngredient.java` | Stores ingredient name, amount (currently String), and unit. |
| `data_access/CocktailJsonReader.java` | Reads one recipe or the indexed dataset into shared Entities; no searching, caching, or saving. |
| `app/DataReadCheck.java` | Loads 663 recipes and prints Daiquiri as a smoke check. It is not Search. |
| `src/main/resources/bar-assistant/` | Bundled JSON dataset, resource index, license, and source/version information. |
| `src/test/java/` and `src/test/resources/` | Check Reader behaviour, basic dependency direction, and the opening window. Individual feature tests still need to be written. |
| `pom.xml` and `config/checkstyle/checkstyle.xml` | Shared dependencies, Java 11 compilation, JUnit execution, and style checks. |

#### Coordinate before changing shared code

- **Entities and dataset access — everyone:** agree before changing Entity fields,
  constructors, quantity types, or the Reader. Reuse the existing dataset; decide
  together how completed features will access the loaded catalog.
- **Home and result cards — Qinli and Wenqian:** coordinate changes to the opening
  Home/search area, shared result display, and passing a cocktail ID to Details.
- **Personal collections — Jiaqi, Enhao, and Wenqian:** agree on user identity and
  Save/Load contracts. Jiaqi owns Profile composition, Enhao owns Custom Recipes,
  and Wenqian owns Favourites.
- **Application wiring and build — everyone:** coordinate AppBuilder, dependency,
  and Checkstyle changes. Runtime API integration and user-data persistence are
  not implemented. Each owner adds their own tests and submits reviewed PRs.

**End of temporary guide — remove before final submission.**
