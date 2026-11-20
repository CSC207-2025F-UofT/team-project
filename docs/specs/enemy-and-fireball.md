# SpellBook: Enemy, Spell & Map Spec

2026-11-16 · Jordan Lin

## Overview

This milestone adds the first enemy (Aayaan), the first spell, a fireball (Omer), and a tile map loaded from a text file (Seran), on top of the core loop that already moves the player with WASD. Everything below is a starting proposal: numbers and designs marked *proposed* are defaults to confirm or change when we talk them through (see Open questions).

| Owner | Feature | Branch (proposed) | Done when |
| --- | --- | --- | --- |
| Aayaan | Enemy that chases the player | `feat/enemy` | An animated enemy walks to the player and stops on contact |
| Omer | Fireball spell | `feat/fireball-spell` | Drawing a circle, then clicking, launches an animated fireball toward the click |
| Seran | Tile map from a text file | `feat/tile-map` | The game draws a dirt-and-water map read from a file of tile numbers |

All three features build on `feature/core-loop` (the movable player), which is open as PR #2 and not on `main` yet. Branch from it, or wait until it merges. Commits follow Conventional Commits, and each feature goes in by pull request with one teammate reviewing.

## How new features plug in

No feature needs a new architecture. The enemy and the fireball add to the same once-per-tick path the player already uses, so work through it in the order shown. The map is the exception: it loads once at startup through a new `data_access` package, and the tick path then reads it. Lines starting with `+` are new this milestone (proposed).

```mermaid
flowchart LR
    Input["<b>KeyboardInput</b><br/>view<br/>WASD held flags<br/>+ MouseInput (Omer)"]
    Loop["<b>GameLoop</b><br/>app<br/>60 ticks/s, polls input<br/>+ passes strokes, clicks"]
    Controller["<b>GameController</b><br/>interface adapter<br/>keys to UpdateGameInputData<br/>+ stroke and aim point"]
    Interactor["<b>UpdateGameInteractor</b><br/>use case<br/>moves and clamps player<br/>+ enemy chase (Aayaan)<br/>+ fireball flight (Omer)"]
    Entities["<b>Entities</b><br/>entity<br/>Player<br/>+ Enemy (Aayaan)<br/>+ Fireball (Omer)"]
    Presenter["<b>GamePresenter</b><br/>interface adapter<br/>OutputData to GameState<br/>+ enemy, fireball data"]
    ViewModel["<b>GameViewModel</b><br/>interface adapter<br/>holds latest GameState<br/>+ enemy, fireball lists"]
    Panel["<b>GamePanel</b><br/>view<br/>draws the player box<br/>+ enemy and fire sprites"]

    Input --> Loop --> Controller --> Interactor
    Interactor --> Entities
    Interactor --> Presenter --> ViewModel --> Panel
    Loop -. "repaint() every tick" .-> Panel
```

Key presses and mouse strokes are only recorded by the `view` layer. `GameLoop` reads them 60 times a second and calls the controller. The interactor applies the game rules to the entities, and the presenter turns the result into a `GameState` that `GamePanel` draws. Imports only point inward (`view` → `interface_adapter` → `use_case` → `entity`). The use case reaches the presenter only through the `UpdateGameOutputBoundary` interface.

## Aayaan: first enemy

One enemy appears, walks toward the player using an animated sprite, and stops when it reaches them. It has no HP, does no damage and cannot die yet.

**Behaviour (proposed)**

- Spawns once at game start, at a fixed point near a screen edge, so it is easy to test.
- Moves toward the player every tick at 2 px per tick. That is half the player's 4 px, so the player can always outrun it.
- Stops when its hitbox touches the player's hitbox, and starts chasing again when the player moves away.
- Faces the direction it is moving (sprite flips left/right) and plays an idle animation while stopped.

**Pathfinding**

The world is currently an empty 960 × 640 px box with no walls. With nothing in the way, moving in a straight line toward the player is the shortest path. Step one is that straight-line chase. Real pathfinding, such as A\* over the 30 × 20 grid of 32 px tiles in Seran's `TileMap`, only matters once tiles block movement. If water blocks movement this milestone, the enemy needs it to get around lakes. That is an open question.

**Sprites**

- A walk animation and an idle animation, as PNG sprite sheets in `src/main/resources/sprites/enemy/`.
- Images are loaded and drawn only in the `view` layer. The `Enemy` entity knows nothing about images. It only exposes state the view needs: position, facing direction, and whether it is moving.
- The view picks the animation frame from that state plus a tick counter. For example, it could change frames every 8 ticks, which is about 7.5 frames per second.

**Where the code goes**

| Layer | Change |
| --- | --- |
| `entity` | New `Enemy` with position, size, speed, `moveToward(player)` and an overlap check |
| `use_case/update_game` | `UpdateGameInteractor` moves the enemy after the player each tick. `UpdateGameOutputData` gains the enemy's state |
| `interface_adapter/game` | `GameState` gains an enemy snapshot. `GamePresenter` fills it |
| `view` | `GamePanel` draws the enemy sprite. A small sprite-sheet loader is shared with Omer |
| `app` | `Main` creates the enemy (or a `GameWorld` that holds it) |

**Done when**

- [ ] The enemy spawns, walks to the player and stops on contact
- [ ] Walk and idle animations play, facing the right way
- [ ] Diagonal movement is not faster than straight movement
- [ ] A unit test drives `UpdateGameInteractor` with a fake presenter and checks that the enemy gets closer each tick and stops at contact

## Omer: first spell (fireball)

The player draws a circle with the mouse to ready a fireball, then clicks to launch an animated fire sprite toward the cursor. This is the game's core mechanic, so the stroke-recognition design should leave room for more spells later.

**Player flow (proposed)**

1. The player holds the left mouse button and draws. A faint trail shows the stroke as they draw.
2. When they release the button, the stroke is checked. If it is a circle, the fireball becomes *ready* (shown with a small icon or glow on the player). If not, nothing happens and the trail fades.
3. While a fireball is ready, the player clicks anywhere. A fireball spawns at the player's centre and flies in a straight line toward the click point.
4. The fireball disappears when it hits the enemy or leaves the screen. It deals no damage yet.

**Recognising a circle**

There are two options to choose between:

- **Simple circle check.** Find the stroke's centre point, then measure each point's distance from it. Accept the stroke if those distances vary by less than about 25%, the end is close to the start, and the radius is at least 20 px. This is quick to write but only knows circles.
- **$1 Unistroke Recognizer.** This is a well-known algorithm of about 200 lines that compares a stroke against saved templates. It takes longer to write, but each later spell (lightning zigzag, shield square) is just a new template.

The rule "a circle means fireball" is game logic, so the recogniser lives in `use_case` or `entity`, never in `view`.

**Where the code goes**

| Layer | Change |
| --- | --- |
| `view` | New `MouseInput` (like `KeyboardInput`) records the stroke points, the release and the click point. `GamePanel` draws the trail and the fire sprite |
| `app` | `GameLoop` passes any finished stroke and click into the controller each tick |
| `interface_adapter/game` | `GameController` adds the stroke and aim point to the input data. `GameState` gains the fireball list and a "spell ready" flag |
| `use_case` | Proposed: a new `cast_spell` use case turns a stroke into a ready spell. `UpdateGameInteractor` spawns and moves fireballs |
| `entity` | New `Fireball` with position, direction and speed (proposed 10 px per tick) |

**Threading**

Mouse events arrive on Swing's thread, but the game loop runs on its own thread. A finished stroke is a list of points, so it can't be shared through a `volatile` boolean the way WASD keys are. Hand it over through a thread-safe queue such as `ConcurrentLinkedQueue`, so a stroke is never read while it is still being built and never lost between ticks.

**Done when**

- [ ] Drawing a rough circle readies a fireball. A line or scribble does not
- [ ] Clicking while ready launches a fireball toward the cursor, and readiness is used up
- [ ] The fire sprite animates and points the way it travels
- [ ] The fireball disappears on hitting the enemy or leaving the screen
- [ ] Unit tests feed recorded strokes into the recogniser: several circles pass, and a line and a zigzag fail

## Seran: tile map

The world becomes a grid of 32 px tiles read from a text file where each number is one tile type: 0 = water, 1 = dirt. The 960 × 640 window holds exactly 30 × 20 tiles, so a screen-sized map is a file of 20 lines with 30 numbers each. The map loads once at startup. It is not part of the per-tick path.

**Map file format (proposed)**

- One file per map in `src/main/resources/maps/`, for example `level1.txt`.
- One line per row of tiles, with numbers separated by spaces. Spaces leave room for tile ids above 9 later.
- Lines starting with `#` are comments, and blank lines are skipped, so the file can carry notes.

Example: an 8 × 5 corner of a map with a lake on the right.

```
# 0 = water, 1 = dirt
1 1 1 1 1 0 0 0
1 1 1 1 0 0 0 0
1 1 1 1 1 0 0 0
1 1 1 1 1 1 0 0
1 1 1 1 1 1 1 1
```

**Tile design: keep images out of the tile**

An `image()` method on the tile class would mean the `entity` layer imports AWT image classes, which breaks the dependency rule. It would also stop tests from creating a map without the image files present. Instead, the entity says *what* a tile is, and the view decides *how it looks*:

```java
// entity/TileType.java
public interface TileType {
    int getId();
    boolean isWalkable();
}
```

`WaterTileType` implements ID 0 with walking blocked, and `DirtTileType` implements ID 1 with walking allowed. Implementations must be immutable so a grid can safely share terrain objects.

`TileTypeRegistry` receives a list of terrain objects and builds an ID lookup. Its `fromId(id)` returns the registered object or throws an error for an unknown ID; construction rejects duplicate IDs and null entries. Adding another terrain implementation does not require changing the registry or `TileMap`.

When map loading is wired into the application, `Main` will construct the registry with one water object and one dirt object and pass it to the file reader. The map can then share those two objects across all 600 cells. If a tile later needs its own state, such as a door that opens, a `Tile` class can wrap a `TileType` plus that state.

**Where the code goes**

| Layer | Change |
| --- | --- |
| `entity` | `TileType` interface, `WaterTileType`, `DirtTileType`, and `TileTypeRegistry`. `TileMap` holds a `TileType[][]` grid (rows × columns) and the tile size, with `getTile(col, row)`, `tileAtPixel(x, y)`, `isWalkable(x, y)` and width and height in pixels |
| `use_case/load_map` | `MapDataAccessInterface.load(mapName)` supplies a `TileMap` or throws `MapLoadingException`. The later `LoadMapInteractor` will handle this failure through its output boundary |
| `data_access` | `FileMapDataAccessObject` implements `MapDataAccessInterface`. It reads a UTF-8 classpath resource under `maps/` and parses it into a `TileMap` using the supplied terrain registry and tile size |
| `interface_adapter` | A presenter sends the tile grid to the view once, after loading |
| `view` | `TileSprites` maps terrain IDs supplied by the view model to images. `GamePanel` draws the map before the player and enemy |
| `app` | `Main` creates the terrain registry and file data-access object and runs the map load before starting the loop |

**Parsing rules**

- Every row must have the same number of tiles.
- An unknown number stops loading with an error that names the line and column.
- A missing or empty file is an error too, so a mistake shows up at startup instead of as a blank screen.
- Non-integer IDs and IDs outside the Java integer range are rejected. Error locations use the original file's one-based line number and one-based tile column, including when comments or blank lines precede the error.
- Loading failures use `MapLoadingException`, owned by `use_case/load_map`; file APIs stay in `data_access`. Wiring this failure into a presenter is part of the next implementation step.

**Drawing**

The map doesn't change between ticks, so don't copy it into `GameState` every tick. When the map loads, draw all its tiles once into a single 960 × 640 `BufferedImage`. Then `GamePanel` draws that one image each frame, which takes 1 draw call instead of 600.

**Walkable tiles (proposed second step)**

Once the map draws, stop the player from walking onto water. In `UpdateGameInteractor`, check the player's next position against `TileMap.isWalkable` at the four corners of its hitbox. If a corner lands on water, cancel the move on that axis. Checking x and y separately lets the player slide along a shoreline instead of sticking to it.

**Done when**

- [ ] `level1.txt` (30 × 20) loads, and dirt and water sprites draw behind the player
- [ ] Editing a number in the file changes the map on the next run, with no code change
- [ ] A bad file (uneven rows or an unknown number) stops with a clear error that names the line
- [ ] Unit tests parse a small map string into a `TileMap` and check `tileAtPixel` and `isWalkable`
- [ ] Second step: the player cannot walk onto water and slides along the shore

**Ideas for later**

- More tile types: 2 = stone wall, 3 = grass, 4 = sand
- Spawn markers in the file (for example 8 = player start, 9 = enemy spawn) that draw as dirt
- Maps bigger than the screen, with a camera that follows the player
- Randomly generated maps that write the same number format, so the loader doesn't change
- Shoreline edge tiles where dirt meets water (autotiling)

## Shared pieces and coordination

The enemy and the fireball both edit the same five files: `UpdateGameInteractor`, `UpdateGameOutputData`, `GameState`, `GamePresenter` and `GamePanel`. Agreeing on these shared pieces first avoids painful merge conflicts.

| Shared piece | Proposed owner | Why it is shared |
| --- | --- | --- |
| Hitbox overlap check (axis-aligned boxes) in `entity` | Aayaan | The enemy stops on contact, and the fireball disappears on hitting the enemy |
| Sprite-sheet loader in `view` (load PNG, cut frames, pick frame by tick) | Aayaan | Enemy and fireball are both animated |
| `GameState` shape: lists of enemy and fireball snapshots | Both, agreed up front | Both add fields to it and to `UpdateGameOutputData` |
| Order of steps inside one tick | Both, agreed up front | The fireball's hit check needs the enemy's position for that tick |
| Tile size (32 px) and `TileMap.isWalkable` | Seran | Player movement and the enemy's pathfinding both ask the map which tiles block them |

**Proposed tick order** inside `UpdateGameInteractor.execute`:

1. Move and clamp the player
2. Handle a finished stroke (ready a spell) and a click (launch a fireball)
3. Move the enemy toward the player, stopping on contact
4. Move fireballs and remove any that hit the enemy or left the screen
5. Send everything to the presenter

**Conventions to keep**

- Coordinates are pixels with the origin at the top-left, and y grows downward.
- Entities store positions as `double`. Only the presenter rounds to `int`.
- Entities never import Swing. The use case talks to the presenter only through `UpdateGameOutputBoundary`.

## Out of scope

These wait for a later milestone, so none of the three features should build them now:

- HP, damage and death, for both the player and enemies
- Waves, more than one enemy, and different enemy types
- Spells other than the fireball, plus mana and cooldowns
- Upgrades, chests and sound
- Maps bigger than one screen, a scrolling camera, and tile types beyond water and dirt

## Open questions

Settle these with Aayaan, Omer and Seran before coding starts, then tick each one off and update the sections above.

**For Aayaan**

- [ ] Does water block the enemy this milestone? If yes, it needs grid pathfinding (A\*) over Seran's `TileMap`. If no, a straight-line chase is enough.
- [ ] Where does the sprite art come from (made ourselves or a free asset pack), and what size are the frames?
- [ ] Is 2 px per tick the right enemy speed, and should it stop on touching or a few pixels before?

**For Omer**

- [ ] Simple circle check or the $1 Unistroke Recognizer?
- [ ] Does aiming fire on a separate click, or on the release of a second drag?
- [ ] Does a ready fireball expire after a few seconds if not used?
- [ ] A new `cast_spell` use case, or fold everything into `update_game`?

**For Seran**

- [ ] 32 px tiles, or 48 px to match the player? 640 px is not a whole number of 48 px rows (13.3).
- [ ] Does water block the player this milestone, or is the map only drawn for now?
- [ ] Do fireballs fly over water?
- [ ] Where does the tile art come from, and is each tile one 32 × 32 px image?
- [ ] Space-separated numbers, or one digit per tile with no spaces (shorter, but only 10 tile types)?

**For both**

- [ ] Does each person need their own use case for the course rubric? If yes, how do we split `update_game`?
- [ ] Should `Player`, `Enemy` and the fireballs move into a single `GameWorld` entity now, or later?
- [ ] Who writes the shared hitbox check and sprite loader, and by when?
