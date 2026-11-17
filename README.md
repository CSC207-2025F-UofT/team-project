# Family Feud
## Team Information
### Team Members
- Charles Neale
- Caleb Gawthroupe
- Kevin Liu
- Jacob Chislett
- Eric Wu

---
## Project Description
- We are creating a Family Feud game show app that interacts with an external API to store and fetch questions.
- We are also storing teams that have played along with full game history of their game.

---
### Domain
- Entertainment

---
## User Stories
### As a host, I want to create custom questions and answers, and save them, so that players can play the game in the future.
### As a player, I want each round's question displayed with the correct number of hidden answers, so that I know how many answers there are to find.
### As a host, I want to be able to type in a guess and see if it on the board, so that teams can earn points.
### As a player, I want my team score tracked and displayed throughout the game and saved between sessions, so we can track our teams history.
### As a user, I want a main menu that can start a game, manage questions, check history and exit, so that I can move through the app easily.

---
## Use Cases
### Use Case 1: Add and store questions
#### Primary actor:
* Game host

#### Preconditions:
* The game is running and the user has navigated to the "Add Question" screen.

#### Trigger:
* The user selects the "Add Question" option from the menu and submits a new question with its answers.
#### Main success scenario:
1. The system displays the question creation frame.
2. The user enters the question and answers.
3. The system validates the input.
4. The system creates the Question entity.
5. The system saves the entity into the database.
#### Extensions:
* #### 3a: empty question or missing answers
  * The system displays an error message. 
* #### 3b: Duplicate Question
  * The system warns the user about the duplicate.

### Use Case 2: Display a new question
#### Primary actor: 
* Game show host

#### Preconditions:
* The game is currently running
* There is no active question, i.e. the game is just starting or the previous question has been completed

#### Trigger:
* The host asks the system to randomly select a new question to display on the board
#### Main success scenario:
1. The host asks for a new question to be displayed.
2. The system checks that there is an unused question that is available.
3. The system randomly selects a question from the set of available questions.
4. The system changes the active display to accommodate the new question and displays the question on the game board.
5. The system starts a new round of play with the selected question.
#### Extensions:
 * #### 2a. There are no unused questions available to be played
    i. The system explains that a new question cannot be loaded.\
    ii. The system does not load a new question, and the use case ends.

### Use Case 3: Guess Input
#### Preconditions:
* The game is currently running
* There is an active question, i.e. a current question board is loaded.

#### Trigger:
* The host inputs to the system to check if the contestants answer is valid.
#### Main success scenario:
1. The host inputs the answer to the system.
2. The system checks that the answer is in the list of correct answers.
3. The system displays where that answer was along with the points for the answer.
4. The system changes the active display to accommodate the correct and displays the answer on the game board.
5. The system updates the teams score with their answers score.
6. The system starts a new round of play with the selected question.
#### Extensions:
* #### 2a. The answer is not in the list of correct answers
  i. The system does not reward any points.
  ii. If the contestants now have 3 wrong answers the team swaps.

### Use Case 4
### Use Case 5: Start a New Game
#### Primary actor:
* Game show host

#### Preconditions:
* Application is open and the main menu is display
* No active game is being played

#### Trigger:
* The host asks the system to start a new game
#### Main success scenario:
1. The host asks to start a new game.
2. The system checks that there are questions available to play.
3. The system asks the host for the names of the two teams.
4. The host enters a name for each team.
5. The system checks that both team names are valid.
6. The system creates the new game with both teams starting at zero points.
7. The system changes the active display from the main menu to the game board, showing both team names and scores.

#### Extensions:
* #### 2a. There are no questions available to be played
  i. The system explains that a game cannot be started until questions have been added.\
  ii. The system remains on the main menu, and the use case ends.
* #### 3a. The host decides not to start a game
  i. The system discards any entered team names.\
  ii. The system returns to the main menu, and the use case ends with no game started.
* #### 4a. A team name is empty, or both teams have the same name
  i. The system explains which team name needs to be changed and keeps the names that were entered.\
  ii. The use case continues at step 4.
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Charlie Neale | Guess Input |
| Caleb Gawthroupe | Start a New Game |
| Kevin Liu | Use Case |
| Jacob Chislett | Display a new question |
| Eric Wu | Use Case |
---
## Entities
### Entity 1: Answer
#### Instance Variables:
* -primaryAnswer: String → The main representative answer.
* -acceptedVariants: List\<String> → A collection of additional acceptable answers / variants on the primary answer.
* -score: int → The number of points allocated to a team for getting a given answer correct.
#### Relevant Methods:
* +Answer(primaryAnswer: String, acceptedVariants: List\<String>, score: int)
* +isAccepted(potentialAnswer: String): int
* +getScore(): int
### Entity 2: Question
#### Instance Variables:
* -questionText: String → The main text of the question 
* -correctAnswers: List\<Answer> → The acceptable answers to the question
#### Relevant Methods:
* +Question(questionText: String, correctAnswers: List\<Answer>)
* +getQuestionText(): String → Return the main text of the question
* +verifyAnswer(potentialAnswer: String): Integer → Return the number of points associated with a given answer, with 0 if the answer is not correct
### Entity 3: Team
#### Instance Variables:
* -name: String → the team's name
* -score: int → the total number of points the team has earned in this current game
#### Relevant Methods:
* +Team(name: String)
* +getName(): String → return the team's name
* +addPoints(pointsToAdd: int): void → increment the team's score by the provided number of points
* +getScore(): int → return the team's current score
---
## API Usage
**API Name: MongoDB**
**Documentation:** [Link](https://www.mongodb.com/docs/languages/java/)
**How it is used in the program:**
Stores and fetches team, question and answer data in remote database. 
---
## Design Diagrams
### UML Class Diagram
### Sequence Diagrams
---
## How to Run
[Instructions for running the program.]
Click run
---
## Notes
[Any additional project information.]
