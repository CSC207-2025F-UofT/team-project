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

• Methods: get

Inventory

• Instance variables: item, maxCapacity

• Methods: addItem(Item), removeItem(Item), isFull()

Room

• Instance variables: description, exits (directions), item, monster (Not in use case 2 but a room can have monster)

• Methods: getExit(direction), removeItem(Item), monsterPresent()


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
