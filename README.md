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
Name: Generate the world

Primary Actor: Player

Preconditions: The player is logged in

Trigger: The player clicks start game

Success Outcome: The world is created, with tiles and enemies spawning in

Main Success Scenario:
The user clicks start
The map is generated
Any entities that should be drawn are drawn
The player enters the game loop, and can start to control the player


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

Move the white box with WASD or the arrow keys.

### Architecture (core loop)

- `entity/Player`: position, size and speed (no Swing).
- `use_case/update_game`: one tick of game rules (`UpdateGameInteractor`) with input/output boundaries.
- `interface_adapter/game`: `GameController` (key state -> request), `GamePresenter` + `GameViewModel` (result -> what to draw).
- `view`: `GameWindow` (JFrame), `GamePanel` (draws), `KeyboardInput` (records held keys).
- `app`: `GameLoop` (60 ticks/s thread: input -> update -> repaint) and `Main` (builds and wires everything).

---

## Notes

[Any additional project information.]

