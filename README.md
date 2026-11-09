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
## Team Information
### Team Members
- Yuxiang Yuan
- Xinjia Shen
- Zhenbin An
- Lanxin Fu

---
## Project Description
### Domain
[University life simulation, presented as a casual top-down game]
---
## User Stories
### User Story 1
### User Story 2
### User Story 3 — Food and Work (US3)
**Lead:** Zhenbin An (UC3)

As a player, I want to spend Money on food at the food truck or café to restore Energy, and work café shifts to earn Money, so that I can balance my resources throughout the school day.

**Responsibilities within UC3:**
1. Implement food and eating: show available meals, let the player buy food, deduct Money, and restore Energy.
2. Implement café work: let the player complete a shift, earn Money, and spend Energy and time.

**Acceptance criteria:**
- The player can view the day's menu, including each item's price and Energy gain.
- Buying an affordable item deducts its price, restores Energy, and uses part of the current time slot.
- Working a café shift pays Money based on correctly served orders, deducts Energy, and uses the time slot.
- Unaffordable meals, insufficient Energy for a shift, and closed locations give clear feedback without applying an invalid action.
- If the meal API is unavailable, the player can still use a bundled fallback menu.

### User Story 4
(Add additional user stories as needed.)
---
## Use Cases
### Use Case 1
### Use Case 2
### Use Case 3A — Buy Food and Eat (UC3A)

**Primary actor:** Player

**Trigger:** The player interacts with the food truck or café counter and chooses to eat.

**Preconditions:** A game is in progress, and the location is open in the current time slot.

**Main success flow:**
1. The game obtains today's menu through the menu gateway and shows meal prices and Energy gains.
2. The player selects a meal.
3. The game checks the player's Money, then deducts the price, restores Energy, and uses part of the time slot.
4. The HUD shows the updated Money, Energy, and time.

**Alternatives:** If the meal API fails, use the bundled fallback menu. If the player cannot afford a meal, show the shortfall and do not deduct Money or advance time. If the location is closed, show its opening hours.

**Success outcome:** The player has eaten; Money decreases by the meal price, Energy increases, and time advances.

### Use Case 3B — Work a Café Shift (UC3B)

**Primary actor:** Player

**Trigger:** The player interacts with the café counter and chooses to work a shift.

**Preconditions:** A game is in progress, and the café is open in the current time slot.

**Main success flow:**
1. The game checks that the player has enough Energy and whether the shift overlaps a scheduled class.
2. Customers order menu items; the player chooses the matching items.
3. The game pays Money based on correct orders, deducts Energy, uses the time slot, and updates the HUD.

**Alternatives:** If the player lacks Energy, explain why the shift cannot start. If a shift overlaps a class, warn that the class will be missed and ask for confirmation; if confirmed, UC1 handles the absence. If the café is closed, show its opening hours. If meal data is unavailable, use the bundled fallback menu for customer orders.

**Success outcome:** The completed shift increases Money according to correct orders, decreases Energy, and advances time.

### Use Case 4
(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Zhenbin An | UC3A — Buy food and eat (spend Money); UC3B — Work a café shift (earn Money) |
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |
---
## Entities
### MenuItem (UC3)
Represents a meal offered on the menu, with a name, price, and Energy gain. The game sets prices and Energy values; provider-specific API data stays outside the entity.

### Entity 1
### Entity 2
### Entity 3
(Add additional entities as needed.)
---
## API Usage
**Planned UC3 API:** [TheMealDB](https://www.themealdb.com/api.php)

**How it will be used:** A menu gateway obtains meal data for the food truck and café and converts selected meals into `MenuItem` values. A bundled menu keeps UC3 playable when the service is unavailable. Access requirements and a successful Java request still need to be verified before integration.

**API Name:**
**Documentation:** [Link]
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
[Any addi
