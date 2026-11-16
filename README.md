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
### User Story 1 - Regan
As a player, I want it to be easy to start a new game, so that I can begin guessing right away.
### User Story 2 - Elliot
As a player, I want to receive a confirmation when I guess the correct answer, so that I can see my points and stats based on the number of guesses it took me.
### User Story 3 - Nikola
As a player, I want the game to get easier with every wrong answer that I make, so that I have more information to base my guesses off of.
### User Story 4 - Regan
As a player, I want cityGuesser to know my name so that I can be personally called by my name.
### User Story 5 - Ingrid
As a player, I want to be able to compare myself to top players of the cityGuesser to see how good I am, relatively, at the game.

---
## Use Cases
### Use Case 1 - Play game
Primary Actor: Player
Preconditions:
Player is on the main menu
Trigger:
Player selects ‘Play’ button
Success Outcome: 
A round starts and the player is shown an unlabeled satellite image of a city to guess 
Main Success Scenario: 
System picks a random city from the dataset
System displays its satellite image, fully zoomed in, with 5 attempts remaining
Player submits a guess (see Use Case 2 and Use Case 3)
Round ends and the player can play again or return to the menu 
Extensions:
Image fails to load: system picks a different city
Empty guess: system asks for a city name and doesn't use up an attempt

### Use Case 2 - Correct Answer
Primary Actor: Player
Preconditions: 
Player has started a game
Player input the correct city name
Player has not exceeded allotted guesses (5)
Trigger:
Player guess matches city in dataset
Success Outcome:
A message outputs that the 
Points are accumulated based on the number of guesses it took (100 for 1, 50 for 2, 25 for 3, 10 for 4, 0 for 5, negative amount for not guessed)
Main Success Scenario:
Display that 
Increase player rating
Extensions:
Accumulate points to my profile if I am signed in.

### Use Case 3 - Incorrect Answer
Primary Actor:
Preconditions: 
Player has started a game
Player has not guessed correctly yet
Trigger:
Player makes an incorrect city guess
Success Outcome: 
User is given more information about the city
If the fifth guess is guessed incorrectly, 
Main Success Scenario: 
Zooms out on the satellite image by a constant amount.
Shows the distance from the guessed city to the target city, with a direction.
Extensions:


### Use Case 4 - View Statistics
Primary Actor: Player
Preconditions:
Player is on the main menu, or has just finished a round
Trigger:
Player selects the "Statistics" button, or a round ends
Success Outcome:
A statistics window shows the player's game history
Main Success Scenario:
System loads the player's saved stats
System displays games played, win %, current streak and longest streak
System displays a guess distribution: how many games were won on attempt 1, 2, 3, 4 and 5
If the window opened after a round, the bar for that round's attempt number is highlighted
Player closes the window and returns to the previous screen
Extensions:
No games played yet: all stats show 0 and the distribution bars are empty
Player lost the round (5 wrong guesses): games played goes up, current streak resets to 0, and no distribution bar is highlighted

### Use Case 5 - Load and Zoom Satellite Image
Primary Actor: Player
Preconditions:
Player has started a game (see Use Case 1)
A city has been picked from the JSON dataset
Trigger:
A new round starts, or the player makes an incorrect guess (see Use Case 3)
Success Outcome:
The satellite image of the city is displayed at the correct zoom level for the current attempt
Main Success Scenario:
System gets the city's latitude and longitude from the JSON dataset
System works out the zoom level from the attempt number (e.g. attempt 1 = most zoomed in, each wrong guess zooms out one step)
System builds a Mapbox Static Images API URL using the coordinates, zoom level, image size and satellite style
System sends the request to Mapbox and receives the image
System displays the image with no labels
Extensions:
Request fails or times out: system retries once, then shows an error and picks a different city
Invalid or missing API key: system shows an error message and returns to the main menu
Player is on the last attempt: image is shown at the most zoomed-out level

### Use Case 6 - Save data to the database
Primary Actor:

Preconditions:

Trigger:
A player completes a game.
Success Outcome:
The player’s data is saved to the data base
Main Success Scenario:
The player’s stats are saved to the data base
Extensions: 
Failed to save data

### Use Case 7 - Retrieve user data from the database
Primary Actor:

Preconditions:

Trigger:
The program starts running.
Success Outcome:
The player’s data is retrieved from the data base
Main Success Scenario:
The player’s stats are retrieved from the data base
Extensions: 
Failed to retrieve data

(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Felix | Use Case  5 | 
| Elliot | Use Case 3 | 
| Nikola | Use Case 4,6 | 
| Regan | Use Case 1,7 | 
| Ingrid | Use Case 2 | 


---
## Entities
### Entity 1 
Player
### Entity 2
Location (image they’re trying to guess)
### Entity 3
Guess box (where you input answers)

---
## API Usage

External database for leaderboard data, to send the data to every user’s device.

**API Name: Mapbox**
Mongo db could be used for the implementation of the leaderboard.


**Documentation:** [https://docs.mapbox.com/api/maps/static-images/]

**How it is used in the program:** 
This API will be used in addition to a JSON dataset containing popular cities longitude and latitude. Then those coordinates will help build a URL query to get the satellite image.

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

---
## Project Updates
**2026/11/15** - Elliot
First iteration of view


---
## MVP Description
Users are greeted with a home page where they click a button to start the game. It’s a single player game where the user guesses the city and is scored based on how many guesses it takes them to guess the correct city. The user gets 5 tries to guess the correct city. Every incorrect guess provides the user with a clue, by zooming out on the satellite image and listing the distance from their guessed city to the target city, with a direction. The user doesn’t have an account. A correct answer opens up a window showing the user their final score, with a button to play again and a button to return home.
