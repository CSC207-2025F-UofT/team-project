# Interface language selection

Based on main commit 35826db331f70f2cfb85eddec3ea36c8124745e3 (PR #14).

At startup, Main opens a language selector with English, 简体中文 and Français.
Closing or cancelling this selector exits startup. Main creates one AppBuilder
with the chosen language on the Swing event dispatch thread.

AppBuilder shares one immutable I18n object across the existing Home, Search,
Cocktail Details and Customize views. Labels, buttons, ingredient table headers,
base spirit options, validation messages and save feedback use the selected
language. A persistent footer displays the selection on both Home and Customize.
The selection lasts for the current run; restarting asks again.

UTF-8 property files live in src/main/resources/i18n. Add future UI text there.
For a new view, inject the existing I18n object rather than constructing an
English default. Existing view constructors and new AppBuilder() remain available
and use English for backward compatibility.

Entities and use cases have no localization dependencies. Existing interactor
messages are translated only where displayed, preserving existing contracts.
Unknown messages fall back to their original text. If an interactor message
changes, update its message.* resource entry and test its translation.

Cocktail names, ingredient names/units, original preparation steps and user
content remain unchanged. This is interface localization, not automatic recipe
translation. No translation service, API key, dependency or network call is added.

The main branch does not yet contain Account/Profile/Favourites views; their
future implementations will need to receive the same language context.
The existing guest session in Main remains unchanged; saving still requires the
Account integration described in INSTRUCTIONS.md.

## Verification

- Production and test sources compiled with Java 21 using source/target 11.
  This runtime lacks ct.sym, so --release 11 validation was not available.
- All JUnit tests were scanned: 35 passed, 0 failed, 2 window tests aborted
  under headless mode (including the added three-language navigation scenario).
- Headless tests instantiate all four existing panels in each language, check
  translations and error messages, preserve filter enum values and recipe text,
  verify complete translation bundles and independence from the OS locale.
- Checkstyle 10.21.4 passes on all changed Java files.
- A desktop test attempt could not load libawt_xawt.so. The two graphical
  navigation tests therefore still need to run on a full desktop JDK.
- Maven offline test could not resolve the uncached Checkstyle plugin.
  Tests above were compiled/run directly with Java and JUnit Platform.

On a machine with Maven dependencies available, run:

    mvn -Dcheckstyle.skip=true test

Also run normal mvn test; unchanged repository files may still have style issues.
Manual smoke check: choose each language, select a cocktail, open Customize,
edit instructions, check a validation message and Cancel back Home. Restart and
choose another language. Confirm the footer and buttons match throughout.
