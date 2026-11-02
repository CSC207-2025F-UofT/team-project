# Farming Game

## Team Information

### Team Members

- Kaho Yamamoto

- Shuting Wu

- Angela Peng

- Bella Patel


---

## Project Description

### Domain

[Project description]

---

## User Stories

### As a player, I want to plant, grow, and harvest plants for my farm. 

### As a player, I want to grow, raise, and process livestock for my farm. 

### As a player, I want to trade resources with NPCs. 

### As a player, I want to know what activities I can do under certain weather conditions. 

---

## Use Cases

### for user story handling planting, growing, and harvesting plants:
Use Case 1.1
- Name: Plant a Crop
- Primary actor: Player
- Precondition: The player has at least one empty plot and at least one seed in the backpack.
- Trigger: The player clicks an empty plot.
- Main success scenario:
    1. Player clicks an empty plot.
    2. System shows the seeds in the backpack.
    3. Player chooses a seed.
    4. System plants the seed in the plot and removes one seed from the backpack.
    5. System shows the plot as planted.
- Extensions:
    - 2a. The backpack has no seeds, so the system tells the player.
    - 3a. Player cancels, so the plot stays empty.
- Success outcome: The plot contains a growing crop, and the backpack has one fewer seed.

Use Case 1.2
- Name: Tend a Crop
- Primary actor: Player
- Precondition: The plot has a planted crop that has not been tended.
- Trigger: The player clicks a planted plot.
- Main success scenario:
    1. Player clicks a planted plot.
    2. System checks the current weather.
    3. System offers to water the crop (weather is Clear).
    4. Player chooses to water.
    5. System marks the crop as tended and shows the updated plot.
- Extensions:
    - 3a. Weather is Rain, so the system offers to fertilize instead of water. Continue at step 4.
    - 4a. Player cancels, so the crop stays untended.
- Success outcome: The crop is marked as tended and ready to harvest.

Use Case 1.3
- Name: Harvest a Crop
- Primary actor: Player
- Precondition: The plot has a tended crop.
- Trigger: The player clicks a tended plot.
- Main success scenario:
    1. Player clicks a tended plot.
    2. System removes the crop from the plot and adds the harvested crop to the backpack.
    3. System shows the plot as empty.
- Extensions:
    - 1a. The crop has not been tended, so the system tells the player it is not ready.
- Success outcome: The plot is empty and the backpack has the harvested crop.

### for user story handling growing, raising, and processing livestock:

### for user story handling trading resources with other players:

### for user story handling weather on the farm

Use case 4.1
- Name: Choose city 
- Primary Actor: Player
- Preconditions: There is a newly opened game.
- Trigger: The player starts the game.
- Main Success Scenario: 
  1. Player opens a new game.
  2. System provides a list of cities to choose from.
  3. Player selects their preferred city.
  4. System pulls the historical weather from the chosen city using the API.
  5. System calculates and records the chances of each weather pattern occurring.
  6. System opens the game screen
- Extensions: 
- Success Outcome: The chances of each weather pattern occurring is recorded for the rest of
the session. 

Use case 4.2
- Name: Change weather
- Primary Actor: System
- Preconditions: There is an active game and a record of weather pattern chances exists
- Trigger: It has been 5 minutes since the game started or the weather last changed
- Main Success Scenario:
 1. System confirms that it has been 5 minutes since the game started or the weather last changed
 2. System generates the weather from the weather pattern chances
 3. System updates weather on the game screen
 4. System updates actions options based on the weather
 5. System starts a new timer for the next weather change
- Extensions:
- Success Outcome: The weather displayed on the screen and the actions affected by it are updated. 

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |

| Tiffany | Use Case 1 |

| Bella | Use Case 2 |

| Angela | Use Case 3 |

| Kaho | Use Case 4 |

---

## Entities

### Entity 1
### Entity 2

(Add additional entities as needed.)

---

## API Usage

**API Name:** weatherapi - JAVA

**Documentation:** 

**How it is used in the program:** When starting a new game, the user chooses a city to place 
the farm in. The system then calculates the chance of each weather pattern occurring in that city
based on the actual city's historical weather, which will be used to determine the weather pattern
for the rest of the game session. 

---

## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]

---

## Notes

[Any addi