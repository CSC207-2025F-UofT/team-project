# Project Structure – who works on what

We follow Clean Architecture (the CSC207 lab layout). Each user story / GitHub issue has its own
folder in every layer, so we can work at different times without editing the same files.

How one feature flows:
`View` (button click) → `Controller` → `Interactor` (the logic) → `Presenter` → `ViewModel` → `View` redraws.
The Interactor only talks to interfaces, so it can be unit-tested with fake data.

## Your files (one row per issue)

| Issue | Owner | Package name | Extra files only you own |
|---|---|---|---|
| #1 Wardrobe Management & Import | Ginni | `add_clothing` | `data_access/LocalImageStorage.java`, `use_case/add_clothing/ImageStorageInterface.java` |
| #2 Weather-Driven Outfit Recommendation | Zahraa | `generate_outfit` | `data_access/OpenWeatherMapDataAccessObject.java`, `use_case/generate_outfit/WeatherDataAccessInterface.java` |
| #3 Aesthetic & Style Preference Filtering | Sofiia | `set_style` | |
| #4 Outfit Save & Regeneration | Carmen | `save_outfit` | |
| #5 Closet Inventory Visualizer | Rie | `view_closet` | |

For your package `x` / class prefix `X`, you own:

```
src/main/java/use_case/x/          XInputData, XInputBoundary, XInteractor,
                                   XOutputData, XOutputBoundary, XDataAccessInterface
src/main/java/interface_adapter/x/ XController, XPresenter, XState, XViewModel
src/main/java/view/XView.java
src/test/java/use_case/x/XInteractorTest.java
```

## Shared files (tell the group chat before changing)

```
app/Main.java, app/AppBuilder.java        starts the app and wires every feature together
entity/                                   ClothingItem, Category, Outfit, WeatherData, UserProfile
interface_adapter/ViewModel.java, ViewManagerModel.java
view/ViewManager.java                     switches between screens
data_access/FileUserDataAccessObject.java saves/loads everything to data/user_closet.json
```

In `AppBuilder` and `FileUserDataAccessObject`, only edit the section marked with your issue number.

## Running it

- Main class: `app.Main`
- Weather API key: set the environment variable `OPENWEATHER_API_KEY` (never commit the key).

## Git workflow

1. Branch per issue: `git checkout -b issue-1-add-clothing`
2. Commit small, push often.
3. Open a PR that says `Closes #1`, get one teammate to review, then merge.
4. Pull `main` before starting work each day.
