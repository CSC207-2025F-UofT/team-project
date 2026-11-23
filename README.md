# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.

## Outfit UI MVP (member 2)

A Java 11 Swing demo of outfit creation and user-defined tag filtering.
Run `app.Main` in IntelliJ. The demo uses sample clothing and in-memory
storage: outfits and tags disappear on exit. Sample photos are not bundled;
missing images are displayed as placeholders.

### Implemented behavior
- Create an outfit from existing clothing; a name and at least one item are required.
- Select any number of items per category (no mandatory category yet).
- Create named tags; names are trimmed and duplicates are rejected ignoring case.
- Assign multiple existing tags while creating an outfit.
- Click Filter to select a saved tag, or All outfits to clear the filter.
- Delete an outfit after confirmation; clothing and tags remain intact.
- Storage calls run off the Swing event thread; presenter updates run on it.

### Clean Architecture
`view -> controller -> input boundary/interactor -> output boundary/presenter -> view model -> view`

Entities and use cases have no Swing dependencies. The interactor depends on
`use_case.outfit.OutfitRepository`, not a concrete database class. The MVP groups
create/list/delete/tag operations in one interactor and passes immutable output
DTOs to the view model. They can be split into separate use-case packages if the
course template requires that granularity.

### Integration agreements
- Member 1: supply clothing IDs, names, `ClothingType`, image paths and extracted
  features; reuse the clothing card component if helpful.
- Member 3: implement `OutfitRepository` and inject it in `app.Main` in place of
  `InMemoryOutfitRepository`. Reads return current snapshots; failed writes must
  not partially mutate stored data. Persist clothing/outfit/tag IDs and associations.
- Member 4: provide recommendation use-case input/output contracts; suggested
  existing clothing IDs can populate the editor before the regular save flow.
  API integration is not implemented in this demo.
- Shared entities: `ClothingItem`, `Outfit`, `Tag`; categories are `HAT`, `TOP`,
  `BOTTOM`, `SHOES`. Tags are user-defined entities, not enum values.

Search, editing existing outfits, tag rename/delete, a real database, Wardrobe
navigation and API suggestions remain integration/future work. Agree on minimum
outfit contents and deletion rules before adding those features.

### Compile and regression checks (PowerShell)
```powershell
New-Item -ItemType Directory -Force target/classes, target/test-classes | Out-Null
$mainSources = @(Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
javac --release 11 -encoding UTF-8 -d target/classes $mainSources
$testSources = @(Get-ChildItem src/test/java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
javac --release 11 -encoding UTF-8 -cp target/classes -d target/test-classes $testSources
java -cp 'target/classes;target/test-classes' use_case.outfit.OutfitInteractorChecks
java -cp target/classes app.Main
```

The dependency-free regression checks are run explicitly; they are not JUnit tests
and are not discovered by Maven Surefire. They do not establish 100% coverage.
