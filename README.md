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

# Project Name
- SpellBook

## Team Information

### Team Members
- Aayaan Singal

- Omer Sheaffer
  
- Jordan Lin
  
- Seran Naventhan


## Project Description
A wave based game where the user can draw patterns to cast spells onto waves of enemies. Throughout the game, the user can get upgrades and get more spells through chests, to fight more difficult enemies until all the waves are defeated, or the user loses all their life. 

### Domain

Game, RPG, Single Player, Offline, RogueLike

---

## User Stories

### User Story 1
As a player, I want to be able have a character that can move, interact and get stronger so that I’m able to fight enemies
### User Story 2
As a player, I want to be able to fight mobs (monsters) as well as traverse and explore the world so that they can progress in our game

### User Story 3
As a player, I want to be able use spells by drawing them so that I can fight enemies conveniently

### User Story 4
As a player, I want to be able to explore the map so I can fight new enemies, and get more rewards

#### Current map MVP — Seran Naventhan
As a player, I want to enter a world with visible dirt and water terrain so that I can see the arena where I will play.

### User Story 5
As a user, I want to be able to log in so that I can save my data across multiple devices

### User Story 6
As a player, I want to save a run card showing my stats and a snapshot of the dungeon after each run so I can keep it or share it with friends.

---

## Use Cases

### Use Case 1
Name: Move the Player

Primary actor: User

Preconditions: 
The user is in the game loop, and not the login screen
The player is alive (hp is not less than or equal to 0)
Where the user is walking into is not a wall, or a blocked square

Trigger: The user presses the movement keys, which is w to move up, a to move left, s to move down, and d to move right

Success Outcome: The player moves to the tile corresponding to the key they pressed

Main success scenario: 
The user presses one of the movement keys
The system checks that the tile they are moving to is an allowed tile to move to
The player’s coordinate is updated
The player sprite is updated and displayed to the user 

Extensions: 
2a: The tile they are moving to is a wall
The system recognizes that its a wall
The player stays in place

### Use Case 2
Name: Generate Mob Waves

Primary Actor: Player

Preconditions:
The user is in the game loop
The user has made some initial action or movement
There is not already a pre-existing mob wave active

Trigger: The player has started moving or interacting with the world

Success outcome: The player has defeated the mob wave

Main Success Scenario:
The user starts moving or interacting with the world
A wave of mobs spawn around the player
The player fights and defeats the mobs
A new wave of mobs spawns in

Extensions: 
2a: The player is defeated by the mobs
The player is shown the death screen
The player has the option to return to restart the game

### Use Case 3

Name: Prepare a Spell

Primary actor: Player

Preconditions: The player is in an active game

Trigger: The player begins drawing one of the spell symbols anywhere on the map

Success outcome: The game shows that that specific spell is ready to cast.

Main success scenario:
1. The player draws and submits a specific spell symbol.
2. The game recognizes the drawing as a specific spell.
3. The game makes that spell available to cast for Use Case 3

Extensions:
 2a - The drawing is not recognized
 
  1. The game tells the player it did not recognize the drawing.
  2. The failed attempt adds no fireball casts. The player can try again from step 1.

### Use Case 4

Name: Cast a spell

Primary Actor: Player

Preconditions: The player has prepared a spell (Use Case 3)

Trigger: The player has stopped drawing, and releases the spell

Success Outcome: The specific spell that the player has drawn is released from the player, in the direction of where they drew.

Main Success Scenario:
The program checks which spell to output given Use Case 2
The program determines the path for which to launch the spell (towards the cursor)
That specific spell is launched from the player towards the cursor

### Use Case 5
Name: Enter the world

Primary Actor: Player

Owner of the current map MVP: Seran Naventhan

Preconditions: The game is available to start, and the player is not in an active run.

Trigger: The player starts a game.

Success Outcome: The player sees the complete dirt-and-water arena at its original tile size.

Main Success Scenario:
1. The player starts a game.
2. The game obtains the terrain for the starting arena.
3. The player sees the complete arena, ready for play.

Extensions:
- 2a. No arena is identified: the player sees an explanation that an arena is required. No new arena is shown.
- 2b. The arena is unavailable or cannot be read: the player sees a loading error. No new arena is shown.
- 2c. The arena is empty or contains invalid terrain: the player sees an error explaining the problem,
  including its location when available. No partial arena is shown.
- 3a. The arena cannot be displayed: the player sees an explanation, and any previous complete
  arena stays visible. A partially drawn replacement is not shown.

On any failure, an already displayed arena remains unchanged. A later successful attempt replaces
it and clears the error.

Implementation status: the loading flow, production map, placeholder tile images and cached
renderer are connected to startup and the game panel. Startup failures show an error dialog
without opening a playable game; later map errors appear above the last complete background.
Automated tests cover the startup builder, panel drawing and existing movement controls.
Enemy spawning, player collision and camera behaviour are outside this map MVP.


### User Story 6
Name: Save Run Card

Primary Actor: Player

Preconditions: The player has finished a dungeon run. The game has captured the dungeon snapshot and logged the final run stats.

Trigger: The player clicks on “Save Run Card”

Main Success Scenario:
The game opens a file save dialogue, suggests ‘dungeon-run.png’

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |

|-------------|--------------|

| Jordan Lin | Generate the World |

| Seran Naventhan | Move the Player |

| Seran Naventhan | Cast a Spell |

| Aayaan Singal  | Generate Mob Waves |

| Omer Sheaffer | Prepare a Spell |

| Omer Sheaffer | Save Run Card |

Current milestone ownership is recorded in `docs/specs/enemy-and-fireball.md`: Seran owns the
tile-map portion of Enter the World. The table above records the earlier project allocation.

---

## Entities

### Entity 1: Player
Represents the person-controlled character.

Instance variables:
- int health — current health.
- Position position — current location in the world.
- List<Spell> unlockedSpells — spells the player can cast.

Relevant methods:
- move(Position destination) — changes the player’s position.
- castSpell(Spell spell, Position position) — Release a spell towards the given position
- takeDamage(int amount) — reduces the player’s health.
- isAlive() — returns whether the player has health remaining.

### Entity 2: Spell
Represents a spell and the drawing pattern used to activate it.

Instance variables:
- String name — spell’s name.
- int damage — damage dealt to affected enemies.

Relevant methods:
- cast(Position location, World world) — activates the spell at the specified location.
- applyEffect(Enemy enemy) — applies the spell’s damage or other effects to an enemy.

### Entity 3: Enemy
Represents an enemy spawned during a wave.

Instance variables:
- String enemyId — unique identifier for the enemy.
- String enemyType — enemy category, such as basic, fast, or boss.
- int health — current health.
- int attackDamage — damage dealt by an attack.
- double movementSpeed — speed at which the enemy moves.
- double attackRange — maximum distance at which the enemy can attack.
- Position position — current location in the world.
Relevant methods:
- moveToward(Position target, double deltaTime) — moves toward a target based on elapsed time.
- canAttack(Player player) — checks whether the player is within attack range.
- attack(Player player) — damages the player.
- takeDamage(int amount) — reduces the enemy’s health.
- isAlive() — returns whether the enemy has health remaining.

Entity 4: World
Represents the game map and manages enemies, spell placement, and wave progression.

Instance variables:
- double width — map width.
- double height — map height.
- Player player — player currently in the world.
- List<Enemy> activeEnemies — enemies currently on the map.
- List<Position> spawnLocations — possible enemy spawn locations.
- int currentWave — current wave number.
- boolean encounterStarted — whether the player has started the first wave.

Relevant methods:
- isWaveComplete() — checks whether the current wave’s completion condition has been met.
- getEnemiesInRange(Position location, double radius) — finds enemies affected by a spell.
- isWithinBounds(Position location) — checks whether a location is inside the map.

Position is a custom class describing their x and y coordinate.
(Add additional entities as needed.)

---


API ideas: 
https://goqr.me/api/doc/create-qr-code/ (share good run score as qr code)
https://wiki.openstreetmap.org/wiki/Overpass_API? (gen new random world each game using irl places and mapping irl structures to in-game obstacles)
https://pageshot.site/docs (share dungeon run png. We give it a snapshot of the dungeon + the run stats as text + a color gradient to overlay above the snapshot and below the text. It formats it as a single png image and returns it)

## API Usage
**API Name**: goQR.me QR Code API
**Documentation**: https://goqr.me/api/doc/create-qr-code/
**How it is used in the program**:
After completing a run, the player can generate a QR code to share their results. The program sends a short text summary containing the player’s username, final score, and number of waves survived to the API. The API returns a QR code image, which the game displays and allows the player to save to clipboard. Other people can scan the code to read the run statistics.
**API Name:** PageShot API
**Documentation**: PageShot API documentation
**How it is used in the program**:
The player can generate a shareable PNG image summarizing their dungeon run. The program captures a snapshot of the dungeon and builds an HTML/CSS template containing the snapshot, a colour gradient overlay, and text showing the player’s username, score, and waves survived. It sends this template to PageShot’s /v1/html endpoint, which renders the layout into a single PNG image. The game then displays the image and allows the player to save it for sharing.

---

## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

---

## How to Run

Requires JDK 11+ (17 recommended).

- IntelliJ: open the project and run `app.Main`.
- Maven: `mvn compile exec:java -Dexec.mainClass=app.Main`, or `mvn compile` then `java -cp target/classes app.Main`.

Move the white box with WASD or the arrow keys over the green dirt and blue water map.
Player-water collision is deferred, so the player can still move across water.

Run unit tests with `mvn test` (or the Maven `test` lifecycle in IntelliJ).
For environments without a desktop display, use `mvn -Djava.awt.headless=true test`.


### Architecture (core loop)

- `entity/Player`: position, size and speed (no Swing).
- `use_case/update_game`: one tick of game rules (`UpdateGameInteractor`) with input/output boundaries.
- `interface_adapter/game`: `GameController` (key state -> request), `GamePresenter` + `GameViewModel` (result -> what to draw).
- `view`: `GameWindow` (JFrame), `GamePanel` (draws), `KeyboardInput` (records held keys).
- `app`: `GameLoop` (60 ticks/s thread: input -> player update -> mob-wave update -> repaint) and `Main` (builds and wires everything).

### Architecture (Load Map, Seran)

Loads the terrain once at startup and prepares a cached map image, separately from the per-tick use cases.
Startup and game-panel integration are implemented. Final running-game verification remains a separate checkpoint.

- `entity/TileType`: interface for immutable terrain types. `WaterTileType` has ID 0 and blocks walking; `DirtTileType` has ID 1 and permits walking.
- `entity/TileTypeRegistry`: resolves IDs through registered terrain objects and rejects duplicate or unknown IDs.
- `entity/TileMap`: an immutable grid of terrain objects, pixel-coordinate lookup and walkability (no Swing or file access).
- `use_case/load_map`: `LoadMapInputBoundary` and immutable `LoadMapInputData` accept a map name. `LoadMapInteractor` loads through `MapDataAccessInterface`, then sends an immutable `LoadMapOutputData` snapshot of tile IDs and tile size through `LoadMapOutputBoundary`. Invalid names and `MapLoadingException` failures go through the fail output; the input boundary returns `void`.
- `data_access/FileMapDataAccessObject`: loads UTF-8 resources from `maps/`, resolves IDs through the supplied terrain registry, and validates the tile grid.
- `interface_adapter/map`: `MapController` wraps the map name; `MapPresenter` converts success or failure into immutable `MapState`. `MapViewModel` stores that state and fires a `state` property-change notification. The map state contains IDs, dimensions and error text, with no entities, images or file APIs. A failed load preserves the previous map; success clears the error.
- `view/TileImageSource`: the view-only contract for looking up already loaded images by terrain ID. This is a rendering helper interface, not a use-case boundary.
- `view/TileSprites`: loads an injected mapping of IDs to classpath image resources once and checks their dimensions. Missing, unreadable or incorrectly sized images produce an `IOException` with the resource path; `MapBuilder` reports it through the startup error callback.
- `view/MapRenderer`: depends on `TileImageSource` and reads only `MapState`. It builds a cached image at the map's pixel dimensions, rebuilds when the grid or tile size changes, and preserves the last complete image if rendering fails. Loading/rendering error text is available through `getErrorMessage()`.
- `view/MapLayer`: reads `MapViewModel`, draws the cached background at the top-left, and wraps error text into a banner. `GamePanel` draws the background before the player and the error banner afterward. It subscribes to map changes while displayed and removes its listener when detached; the original constructor without a map remains available.
- `view/GameWindow.showStartupError`: displays a failure dialog before a playable window is opened.
- `app/MapBuilder`: wires the map registry, file reader, use case, presenter and renderer; requests the starting map through `MapController`; and returns a prepared `MapLayer`. A map or image failure reports one error and returns no layer.
- `app/Main`: supplies `level1.txt`, tile size 32 and the image mappings to `MapBuilder`. It opens the window and starts the existing loop only after map preparation succeeds.

The map flow is separate from per-tick player updates. Interactor tests use in-memory data
access; an integration test connects the real file reader, controller, interactor, presenter
and renderer using the shipped resources. Startup wiring lives in `app`, and `GamePanel`
draws the cached image through `MapLayer`. Property-change listeners only request repainting,
which is safe from another thread; Swing paints and uses the renderer on its event thread.
The first paint reads the current state, including when loading finished before subscription.
Cached tile and map images are shared within the view and must be treated as read-only.

#### Map resource format

The file reader loads names such as `level1.txt` from `maps/` on the classpath, so it does not
depend on the current working directory. The production arena is
`src/main/resources/maps/level1.txt`; small test maps live in `src/test/resources/maps/`.

Each non-comment line is one row of integer tile IDs, separated by whitespace. With the MVP
terrain registry, `0` is water and `1` is dirt. Blank lines and lines whose first non-whitespace
character is `#` are skipped. Every tile row must have the same number of columns. The tile size
is supplied when constructing the reader; the production arena uses 32-pixel tiles.

Missing, empty, unreadable, or malformed maps produce `MapLoadingException`. Format errors
identify the map name, original file line, and tile column, counted from one. Columns count
tile entries, not characters. `LoadMapInteractor` catches loading failures and sends their
explanation to the presenter, which adds "Unable to load map:" to the view-model error text.
Startup displays that error in a dialog and does not start the loop. If a future reload fails
after startup, the panel displays the error above the previously rendered map.

#### Map resources and rendering

`level1.txt` has 30 columns and 20 rows, producing a 960 × 640 map at a tile size of 32.
The centre and outer edges are dirt, with two water areas elsewhere. The placeholder sprites
are `src/main/resources/tiles/dirt.png` (solid green, `#00FF00`) and
`src/main/resources/tiles/water.png` (solid blue, `#0000FF`), both opaque 32 × 32 PNGs.
`Main` supplies the mapping `0 -> /tiles/water.png`, `1 -> /tiles/dirt.png`.

The renderer reads the IDs supplied by the map view model and places each image in its tile
cell. It uses the map's dimensions rather than fitting the map to the window. Repainting the
same state reuses the cached image; error-only changes also reuse it. The helpers do not know
about player movement, enemy behaviour, fireballs or a camera.

To edit the map, change the numbers in `level1.txt` and rebuild resources before the next run
(for example, with `mvn compile`). Player collision and detailed tile artwork are deferred.
The terrain data marks water as non-walkable for the enemy feature to use; integrating enemy
movement is separate work. Fireballs should cross water.

The combined resource/rendering checkpoint (original steps 5 and 6) and startup integration
(original step 7, renumbered step 6) are implemented. The remaining checkpoint is final
running-game verification and documentation (original step 8, renumbered step 7). Automated
checks cover successful startup preparation, invalid maps, missing/corrupt/incorrectly sized
images, missing image mappings, panel draw order, cache reuse, repaint subscriptions, WASD
and arrow-key movement, and deferred player-water collision.

---

## Notes

[Any additional project information.]

