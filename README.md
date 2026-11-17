D&D Dungeon Crawler Game
## Team Information
### Team Members
- Carter Boyce
- Yulu (Emily) Ye
- Valerie Huang
- Darius Fleischmann
---
## Project Description
### Domain

Games & Entertainment

It's an adventure game application based on the Dungeons & Dragons (D&D) game where players build a character, explore a dungeon room by room, fight monsters in simulated turn-based combat, loot items, and see how deep they can get. Progress in the game is saved to players' accounts and a leaderboard ranking all the players.

---
## User Stories
### User Story 1
Create a character: As a player, I want to choose a race and class and create my starting stats, so I can build my own hero.
### User Story 2
Explore: As a player, I want to move between rooms, read descriptions, and pick up items into an inventory, so I can find loot.
### User Story 3
Combat: As a player, I want to fight monsters turn by turn using dice rolls, weapons, and other items in the inventory, so I can survive and progress.
### User Story 4
Save and Complete: As a player, I want to save and load my game and see a leaderboard of deepest floors, so I can come back and compete.

Add login as a shared use case based on the course's login example.

(Add additional user stories as needed.)
---
## Use Cases
### Use Case 1
Choose race and class.
### Use Case 2
• Name: Explore the Dungeon room by room

• Primary actor: Player

• Preconditions: The player has a character that is standing in a dungeon room and is not in an encounter with monster. The room has one or several items in it that has magical power and is useful in either fighting dragon or improving the character's traits.

• Trigger: The player chooses a direction to move towards a room or an item.

• Main success scenario:

    1.  Player chooses a direction to move.
    
    2.  System finds the room connected in that direction.
    
    3.  System moves the character into that room.
    
    4.  System presents the room's description, its items and its exits.
    
    5.  Player move and chooses to pick up an item.
    
    6.  System shows the description of the item, player choose to save it to their inventory or to drop it.
    
    7.  If the player chooses to save it, system adds the item to player's inventory and removes it from the room.
    
    8.  System presents the updated inventory.
    
    9.  If the player chooses to drop it, nothing happens.
    
• Extensions:

    ◦ 2a. There is no exit in that direction: System shows a message and the character stays in the current room.
    
    ◦ 4a. The room contains a monster: System starts the Combat use case (Use Case 3) and this use case ends.
    
    ◦ 7a. The inventory is full: System tells the player and the item stays in the room. The player can also choose to drop one of the items in their inventory to have this item.
    
• Success outcome: The character is in the chosen room, and any item picked up is in the inventory and no longer in the room.

### Use Case 3
• Name: Fight an Enemy

• Primary actor: Player

• Preconditions:
    - The player has logged in and created a character
    - The character is currently in a dungeon room containing a monster
    - The character and monster both have their healths greater than zero 

• Trigger: The player encounters a monster and chooses to engage in combat.

• Success outcome: The monster is defeated, its defeat is recorded, and the player can continue exploring the dungeon. The character's remaining health and any changes to their inventory are preserved.

• Main success scenario:
```text
  1. The player enters a room with a monster

  2. The system displays the monster's description, health, and combat information.

  3. The system determines which character or monster takes the first turn based on relevant stats of either.

  4. If player is first, the system indicates that it is the player's turn (otherwise refer to relevant steps from 8.).

  5. The player chooses an action, such as attacking with a weapon or using an item from their inventory.

  6. The system calculates the outcome of the player's action using the appropriate dice rolls and character or item stats.

  7. The system updates the monster's health and displays the result of the player's action.

  8. If the monster is still alive, the system begins the monster's turn).

  9. The system determines the outcome of the monster's attack and updates the character's health.

  10. The system displays the result of the monster's action.

  11. The system repeats steps 4–10 until either the monster or the character is defeated.

  12. The system informs the player that the monster has been defeated and allows the player to continue exploring.
```
• Extensions:
```text
  5a: The player chooses to use an item:
      1. The system displays the available items from the player's current inventory.
      2. The player selects an item from the list.
      3. The system applies the item's effect.
      4. The system updates the character's current inventory and relevant attributes.
      5. Combat continues

  6a: The player's attack misses:
      1. The system informs the player that the attack was unsuccessful.
      2. The monster's health remains unchanged and combat continues.

  8a: The monster's attack misses:
      1. The system informs the player that the attack was unsuccessful.
      2. The character's health remains unchanged and combat continues.

  11a: The character's health reaches <=0:
      1. The system informs the player that their character has been defeated.
      2. The system ends the combat encounter.
      3. The system records the character's final progress as a numerical score.
      4. The use case ends.

  12a. The monster has loot that is dropped:
      1. The system displays what loot was dropped by the monster.
      2. The system adds the selected items if sufficient inventory space is available.
      3. Any uncollected items remain available.
```
(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Valerie | Use Case 1 |
| Emily | Use Case 2 |
| Carter | Use Case 3 |
| Darius | Use Case 4 |
---
## Entities
### Entity 1
### Entity 2
Direction 

• Values: NORTH, SOUTH, EAST, WEST

Dungeon

• Instance variables: floorNumber, rooms, currentRoom

• Methods: moveTo(direction), isFloorFinished()

Item

• Instance variables: name, description, type (weapon, armor, potion), value

• Methods: getDescription(), getValue()

Inventory

• Instance variables: items, maxCapacity

• Methods: addItem(Item), removeItem(Item), isFull(), getItems()

Room

• Instance variables: description, exits (directions), item, monster (Not in use case 2 but a room can have monster)

• Methods: getExit(direction), removeItem(Item), monsterPresent()

Monster

• Instance variables: name, description, currentHealth, maxHealth, loot

• Methods: takeDamage(int), isAlive(), attack(Character)

Character

• Instance variables: TODO ADD THESE

• Methods: takeDamage(int), isAlive()

Combat

• Instance variables: character, monster, currentTurn, isDone

• Methods: startCombat(), executeTurn(), update(), endCombat

(Add additional entities as needed.)
---
## API Usage

**API Name:** https://www.dnd5eapi.co/

**Documentation:** [Link]

**How it is used in the program:**

• Used to fetch races, classes, monsters and equipment, which form the contents of the game.

• Invoked from a Data Access Object in the outermost layer, so that the use case interactors do not depend on the API but on an interface only.

---
## Design Diagrams
### UML Class Diagram
### Sequence Diagrams
---
## How to Run
[Instructions for running the program.]
---
## Notes
[Any additional project information.]
