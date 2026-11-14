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

entertainment

---

## User Stories

### 1. As a player, I want to create and log in to my account to save my progress on the game. 

### 2. As a player, I want to plant, grow, and harvest plants for my farm. [UPDATE: PLANT FLOWERS]

### 3. As a player, I want to grow, raise, and process livestock for my farm. [UPDATE: GROW FLOWERS]

### 4. As a player, I want to trade resources with NPCs. [UPDATE: HARVEST FLOWERS]

 

---

## Use Cases

### User story 1: Login
Use Case 1
- Name: Log in to game
- Primary actor: Player
- Precondition: Game is initially loaded
- Trigger: The player inputs username and password and clicks the "login" button
- Main success scenario:
  1. Player inputs username and password.
  2. Player clicks "login" button.
  3. System checks that an account with that information exists.
  4. System finds user information.
  5. System loads main page updated with user information.
- Extension:
  - Player inputs username and password and clicks login, but there is no user with that information so an error shows.
  - Player inputs username and password and clicks signup, but there is already a user with the inputted username, so the system shows error message.
  - Player inputs a unique username and password and clicks signup, so the system creates an account with that information and displays the login page again.
- Success outcome: The main page is displayed with user information.

### User story 2: Plant flower
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

### User Story 3: Grow flower

Use Case 2.0
- Name: Add Livestock to a Pen
- Primary actor: Player
- Precondition: Selected livestock pen is empty and Player has 20 coins
- Trigger: The player clicks an empty pen.
- Main success scenario:
  1. Player clicks an empty livestock pen.
  2. System shows available animal choices
  3. Player chooses an animal
  4. System shows the pen occupied with a baby animal
- Extensions:
  - The Player does not have >= 20 coins, so the system tells the player and the pen remains empty.
  - Player cancels, so the pen remains empty
- Success outcome: The pen contains a baby animal, and the Player has 20 coins fewer.

Use Case 2.1
- Name: Grow Livestock Up
- Primary actor: Player
- Precondition: The pen contains a baby animal, and the player has food item in backpack.
- Trigger: The player clicks a baby animal in a pen
- Main success scenario:
  1. Player clicks an occupied pen with baby animal in it
  2. System offers the option to feed the animal.
  3. Player chooses to feed the animal.
  4. System removes one unit of feed from the backpack.
  5. the animal in the pen is now an adult version
- Extensions:
  - No feed is available, so the system tells the player and the animal remains a baby.
  - Player cancels, so the animal remains a baby.
- Success outcome: The animal is now an adult

### for user story handling trading resources with NPCs:

Use Case 3.1
- Name: Buy Item
- Primary Actor: Player
- Precondition: The store has a catalog of items available for purchase.
- Trigger: The player chooses to buy items in the store.
- Main Success Scenario:
  1. Player enters the store and opens the buying interface.
  2. System displays the available items, their purchase prices, and the player's current balance.
  3. Player selects an item and enters the quantity to purchase.
  4. System validates the quantity in stock and displays the total cost.
  5. Player confirms the purchase.
  6. System checks that the player's current balance is sufficient.
  7. System deducts the total cost from player's balance and adds the purchased quantity to player's inventory
  8. System displays a success message, the updated balance, and updated quantity of purchased item.
- Extensions:
  - 3a. player cancel select → item remain unchanged → use case returns to step 2.
  - 4a. quantity not valid → system displays an error message → use case returns to step 3.
  - 5a. player cancels purchase → the balance and inventory remain unchanged → use case returns to step 2.
  - 6a. player's balance is less that the total cost → system displays an error message → the balance and inventory 
remain unchanged → use case returns to step 3.
- Success Outcome: The player's balance decreases by the total purchase cost, and the inventory contains the purchased
items.

Use Case 3.2
- Name: Sell Item
- Primary Actor: Player
- Precondition: The item to be sold exists in the player's inventory, and the store has a defined price for the item.
- Trigger: The player chooses to sell items in the store.
- Main Success Scenario:
  1. Player enters the store and opens the selling interface.
  2. System displays player's sellable inventory items, their owned quantities, their selling prices, and player's
current balance.
  3. Player selects and item and enters the quantity to sell.
  4. System validates the quantity and display the total payment the player will receive.
  5. Player confirm the sale.
  6. System checks that the player's inventory contains the request quantity.
  7. System removes the sold quantity from player's inventory, and adds the total payment to the player's balance.
  8. System displays success message, the updated balance, and the updated quantity of the sold item.
- Extensions:
  - 2a. player has no items in the inventory → system displays an error message → use case ends.
  - 3a. player cancel select → item remain unchanged → use case returns to step 2.
  - 4a. quantity not valid, system displays an error message → use case returns to step 3.
  - 5a. player cancel the sale → the balance and inventory remain unchanged → use case returns to step 2.
  - 6a. player owns fewer items than the requested quantity → system displays an error message → the balance and 
inventory remain unchanged → use case returns to step 2.
- Success Outcome: The player's inventory decreases by the sold quantity, and the balance increases by the total sale
payment.

### User Story 4: Harvest flower


---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |

| Kaho | Use Case 1 |

| Bella | Use Case 2 |

| Angela | Use Case 3 |

| Tiffany | Use Case 4 |

---

## Entities

### Plot
- Instance variables:
  - state: EMPTY, PLANTED, or TENDED
  - crop: the Crop planted here (none when EMPTY)
- Methods:
  - isEmpty(): boolean
  - plant(crop): sets crop and state to PLANTED
  - tend(): sets state to TENDED (only allowed when PLANTED)
  - harvest(): returns the crop and resets the plot to EMPTY (only allowed when TENDED)
  
### Crop
- Instance variables:
  - name: String
- Methods:
  - getName(): String

### Entities

(Add additional entities as needed.)

---

## API Usage

**API Name:** 

**Documentation:** 

**How it is used in the program:** 

---

## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]

---

## Notes

### Visual Project Outline
![Project Outline Diagram](images/project_outline.png)

