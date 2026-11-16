# CityGuesser

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

## Team Information
### Team Members
- Regan Alexandra Medcof
- Nikola Kraincanic
- Elliot Gamache
- Felix Hansen
- Ingrid Wilkinson
---
## Project Description
### Entertainment
Guess the city game where a player is shown a satellite image of a city. They have 5 attempts to correctly guess what city it is. The satellite image slightly zooms out with each wrong answer, revealing more of the city to the player.

---
## User Stories
### User Story 1
As a player, I want it to be easy to start a new game, so that I can begin guessing right away.
### User Story 2
As a player, I want to receive a confirmation when I guess the correct answer, so that I can see my points and stats based on the number of guesses it took me.
### User Story 3
As a player, I want the game to get easier with every wrong answer that I make, so that I have more information to base my guesses off of.
### User Story 4
As a player, I want cityGuesser to know my name so that I can be personally called by my name.
### User Story 5
As a player, I want to see how well I am doing at the game by being able to see how many points I have and how many guesses I usually make.

---
## Use Cases

### Use Case 1 - Enter Name (Regan)
Primary Actor: Player
Preconditions:
- Player has opened the app and is on the welcome screen
  
Trigger:
- Player types a username and selects 'Continue'

Success Outcome:
- The system knows who is playing and the main menu greets the player by name
  
Main Success Scenario:
- Player types a username and submits it
- System checks the database for a player with that name
- No player is found, so the system creates a new player with all stats at 0 and saves it
- System sets them as the current player
- System shows the main menu with "Welcome, [name]!" and the 'Play' and 'Statistics' buttons
  
Extensions:
- Name already exists: system loads that player's saved stats instead of creating a new player, and shows "Welcome back, [name]!"
- Empty name or only spaces: system asks for a name and stays on the welcome screen
- Name is longer than 20 characters: system asks for a shorter name
- Database can't be read: system shows an error message and stays on the welcome screen

### Use Case 2 - Start Round (Felix)
Primary Actor: Player

Preconditions:
- Player has entered a name (see Use Case 1)
- Player is on the main menu, or has just finished a round
  
Trigger:
- Player selects 'Play' on the main menu, or 'Play again' after a round
  
Success Outcome:
- A new round starts and the player sees an unlabeled satellite image of a city, fully zoomed in, with 5 attempts remaining
  
Main Success Scenario:
- System picks a random city from the JSON dataset
- System creates a new round with that city as the target and no guesses made
- System gets the city's latitude and longitude from the dataset
- System builds a Mapbox Static Images API URL using the coordinates, the most zoomed-in level, the image size and the satellite style
- System sends the request to Mapbox and receives the image
- System shows the game screen with the image, an empty guess box and 5 attempts remaining
  
Extensions:
- Request fails or times out: system retries once, then picks a different city
- Invalid or missing API key: system shows an error message and returns to the main menu
- Dataset can't be loaded: system shows an error message and returns to the main menu
- Same city as the previous round is picked: system picks again

### Use Case 3 - Submit Guess (Elliot)
Primary Actor: Player

Preconditions:
- A round is in progress (see Use Case 2)
- Player has at least one attempt remaining
  
Trigger:
- Player types a city name and selects 'Guess'
  
Success Outcome:
- Player finds out if the guess was right. A wrong guess gives them a hint, and a correct guess or a fifth wrong guess ends the round
  
Main Success Scenario:
- Player submits a city name
- System finds the city in the dataset, ignoring capitalization and extra spaces
- System records the guess and uses up one attempt
- System compares the guessed city to the target city
- The guess is correct, so the system marks the round as won
- The round ends (see Use Case 4)
  
Extensions:
- Wrong guess with attempts remaining: system works out the distance and direction from the guessed city to the target city, adds them to the list of past guesses, requests the satellite image one zoom step further out, and updates the attempts remaining
- Wrong guess on the fifth attempt: system marks the round as lost and the round ends (see Use Case 4)
- Empty guess: system asks for a city name and doesn't use up an attempt
- City is not in the dataset: system shows "not a known city" and doesn't use up an attempt
- City was already guessed this round: system tells the player and doesn't use up an attempt
- Zoomed-out image fails to load: system keeps the current image and still shows the distance and direction

### Use Case 4 - Finish Round (Ingrid)
Primary Actor: Player

Preconditions:
- The current round has just been won or lost
- The result has not been saved yet
  
Trigger:
- The round ends, by a correct guess or a fifth wrong guess (see Use Case 3)
  
Success Outcome:
- Player sees the result and their points, and their updated stats are saved
  
Main Success Scenario:
- System works out the points from the number of guesses used (100 for 1, 50 for 2, 25 for 3, 10 for 4, 0 for 5)
- System adds the points to the player's total
- System increases games played and wins, extends the current streak, updates the longest streak if it was beaten, and adds one to the guess distribution for that attempt number
- System saves the player to the database
- System shows a result window with the city name and points earned, and 'Play again', 'View stats' and 'Main menu' buttons
  
Extensions:
- Player lost the round: system takes away 25 points, increases games played, resets the current streak to 0, leaves the guess distribution unchanged, and the result window reveals the correct city
- Save fails: system still shows the result window, with a warning that the result was not saved
- Player selects 'Play again': a new round starts (see Use Case 2)
- Player selects 'View stats': the statistics window opens (see Use Case 5)

### Use Case 5 - View Statistics (Nikola)
Primary Actor: Player

Preconditions:
- Player has entered a name (see Use Case 1)
- Player is on the main menu, or on the result window after a round
  
Trigger:
- Player selects the 'Statistics' button on the main menu, or 'View stats' on the result window
  
Success Outcome:
- A statistics window shows the player's game history
  
Main Success Scenario:
- System loads the current player's saved stats from the database
- System displays total points, games played, win %, current streak and longest streak
- System displays a guess distribution: how many games were won on attempt 1, 2, 3, 4 and 5
- If the window was opened after a won round, the bar for that round's attempt number is highlighted
- Player closes the window and returns to the previous screen
  
Extensions:
- No games played yet: all stats show 0 and the distribution bars are empty
- Window opened after a lost round: no distribution bar is highlighted
- Stats can't be loaded: system shows an error message and returns to the previous screen


(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Felix | Use Case  2 | 
| Elliot | Use Case 3 | 
| Nikola | Use Case 5 | 
| Regan | Use Case 1 | 
| Ingrid | Use Case 4 | 

### MVP Description
Users are greeted with a home page where they click a button to start the game. It’s a single player game where the user guesses the city and is scored based on how many guesses it takes them to guess the correct city. The user gets 5 tries to guess the correct city. Every incorrect guess provides the user with a clue, by zooming out on the satellite image and listing the distance from their guessed city to the target city, with a direction. The user doesn’t have an account. A correct answer opens up a window showing the user their final score, with a button to play again and a button to return home.

---
## Entities
### Entity 1: Player
ie username, points, current streak ...
### Entity 2: Location
ie city name, latitude, longitude, ...
### Entity 3: Round
ie the current city, attempts, guesses made so far etc

### UML Class Diagram
(add through PlantUML - shows the instance variables and methods)

---
## API Usage

External database for leaderboard data, to send the data to every user’s device.

**API Name: Mapbox**
Mongo db could be used for the implementation of the leaderboard.


**Documentation:** [https://docs.mapbox.com/api/maps/static-images/]

**How it is used in the program:** 
This API will be used in addition to a JSON dataset containing popular cities longitude and latitude. Then those coordinates will help build a URL query to get the satellite image.

---
## How to Run
[Instructions for running the program.]

---
## Notes
[Any additional project information.]

---
## Project Updates
**2026/11/15** - Elliot
First iteration of view

---

