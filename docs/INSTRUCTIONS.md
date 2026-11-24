# Recipe instructions

Cocktail Details displays the original preparation steps. Customize includes a
multiline Instructions editor below the ingredients table. Opening an original
recipe copies its steps into the editor. Users can change those steps when they
change ingredients; the application does not generate new steps automatically.

Save passes the edited steps through the Customize controller, input data,
interactor, entity, output data, presenter, and view model. File storage writes the
`instructions` string alongside each Custom Recipe. Reading the recipe again
restores its steps. Custom changes never modify the bundled original JSON files.
Validation and save failures keep the entered steps so the user can retry. Cancel
returns Home and discards unsaved edits.

## Compatibility

- Original constructors and the original CustomizeController.execute overload
  remain available. They default instructions to an empty string.
- Null instructions are treated as an empty string. The field is optional.
- Bundled source records and older Custom Recipe JSON without instructions remain
  readable. They display an empty editor. New saves include the field.
- New overloads append `String instructions` after the existing parameters.
- A future My Custom Cocktails editor must initialize CustomizeState with
  `state.setInstructions(savedRecipe.getInstructions())`, together with the ID,
  source ID, name, and ingredients.

## Account integration

Main currently passes `() -> null` to addCustomizeUseCase, as in the UI integration
work. This represents a guest. It must be replaced by the Account session provider
before normal users can save. No production test user or bypass was introduced.
The storage and UI integration tests use a test user only within tests.

## Verification

Production and test source files compile. The headless JUnit run reports 33 passing
tests and one aborted test requiring a graphical display. The changed Java files
pass the repository's Checkstyle configuration using Checkstyle 10.21.4.

Tests cover original instruction loading, missing/null source fields, legacy
constructors and controller calls, Details display, edited instructions travelling
through Save, validation failure and retry, Cancel, JSON reload/update and migration,
and keeping the original recipe unchanged.

The AppBuilder graphical navigation test is updated but requires a desktop to run.
The full repository still has 19 Checkstyle violations in unchanged files. Maven
validate could not resolve its plugin in this environment; compilation and JUnit
were run directly with the Java compiler and JUnit Platform instead.

Suggested focused Maven command on a configured development machine:

```bash
mvn -Dcheckstyle.skip=true -Dtest=CustomizeInteractorTest,CustomizePresenterTest,CustomizeInstructionsViewTest,InstructionsCompatibilityTest,InstructionsDataAccessTest,ClickInstructionsTest,CocktailJsonReaderTest test
```

The Checkstyle skip above isolates functional tests from the existing style issues.
Run the normal Maven checks as well before merging after those issues are addressed.
