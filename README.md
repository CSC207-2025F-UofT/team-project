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
University life simulation, presented as a casual top-down game

---
## User Stories
### User Story 1
### User Story 2
As a player, I want to choose where I live and have each day, the weather, and the terms progress, with my progress
saved, so that my life feels continuous and real.

### User Story 3
### User Story 4
(Add additional user stories as needed.)
---
## Use Cases
### Use Case 1
### Use Case 2.1: End the day
Primary actor: Player

Preconditions: 
- A game is in progress
- The player is at home (their residence room or off-campus apartment).

Trigger: The player asks to sleep.

Success outcome: The game moves to the next day with the correct day type, real historical weather, and updated stats,
and progress is saved.

Main success scenario:
1. The player asks to sleep. 
2. The system checks that the player is at home. 
3. The system advances the calendar to the next day and determines whether it is an A day or a B day. 
4. The system restores the player’s Energy according to when the player went to sleep. 
5. The system retrieves the historical weather for the real date that corresponds to the new day and converts it into a
game condition: clear, cloudy, rain, or snow. 
6. The system applies the morning commute cost for the player’s housing and the day’s weather. 
7. The system saves the game. 
8. The system shows the player waking up on the new day, with the day number, day type, weather, any exam scheduled 
today, and updated stats.

Extensions:
- 2a. The player is not at home.
1. The system explains that the player must go home to sleep. 
2. The use case ends and the day does not change. 

- 3a. The day that just ended was the last day of a week, but not of the term. 
1. The system shows the weekend card, and the player chooses an activity. 
2. The system applies the activity’s effect, and the use case continues at step 3.

- 3b. The day that just ended was the last day of the term. 
1. The system charges rent and shows the break card, or the ending if this was the final term. 
2. The system starts the next term on day 1, and the use case continues at step 4.

- 5a. The weather source is unavailable.
1. The system uses its bundled copy of the same historical weather data. 
2. The use case continues at step 6.

- 7a. The system cannot save the game. 
1. The system explains that progress was not saved. 
2. The use case continues at step 8.

### Use Case 2.2：Choose where to live
Primary actor: Player

Preconditions:
- The player is creating a new game.
- The setup wizard has reached the housing step.

Trigger: The player asks to choose their housing.

Success outcome: The player’s housing is recorded and their starting location is set.

Main success scenario:
1. The system shows the housing options (Residence and Off-campus), each with its rent per term, daily commute cost, 
and a short description. 
2. The player selects an option. 
3. The system records the player’s housing. 
4. The system sets the player’s starting location: the residence building, or the transit stop for off-campus players. 
5. The system moves the wizard to the next step.

Extensions:
- 2a. 
1. The player asks to go back to the previous step. 
2. The system returns to the previous step without recording a choice. 
3. The use case ends.

### Use Case 2.3: load a saved game
Primary actor: Player

Preconditions:
- The application is running and the title screen is displayed.

Trigger: The player asks to continue a saved game.

Success outcome: The game is restored exactly as it was saved, and the player is at home.

Main success scenario:
1. The player asks to continue. 
2. The system finds the save file. 
3. The system reads the save file and checks that it is valid. 
4. The system restores the game state: player profile, stats, timetable, calendar, friendships, and the 
term’s stored weather. 
5. The system shows the player at home on the saved day.

Extensions:
- 2a. No save file exists.
1. The system explains that there is no saved game and offers to start a new game. 
2. The use case ends.
- 3a. The save file is damaged or comes from an unsupported version.
1. The system explains that the save cannot be loaded. 
2. The system does not change the file, and the use case ends.

### Use Case 3
### Use Case 4
(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |
---
## Entities
### Entity 1
### Entity 2
### Entity 3
(Add additional entities as needed.)
---
## API Usage
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