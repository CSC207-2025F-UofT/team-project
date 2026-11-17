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
### As surveyor, I want to be able to store survey results, so that they can be accessed later.
### As a player, I want each round's question displayed with the correct number of hidden answers, so that I know how many answers there are to find.
### As a host, I want to be able to type in a guess and see if it on the board, so that teams can earn points.
### As a player, I want my team score tracked and displayed throughout the game and saved between sessions, so we can track our teams history.
### As a user, I want a main menu that can start a game, manage questions, check history and exit, so that I can move through the app easily.

---
## Use Cases
### Use Case 1
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
### Use Case 3
### Use Case 4
### Use Case 5

---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Charlie Neale | Use Case |
| Caleb Gawthroupe | Use Case |
| Kevin Liu | Use Case |
| Jacob Chislett | Display a new question |
| Eric Wu | Use Case |
---
## Entities
### Entity 1: Question object
#### Instance Variables:
* -questionText: String → The main text of the question 
* -correctAnswers: List<String> → The primary answers to the question
* -answerVariants: List<List\<String>> → A collection of additional acceptable answers / variants on the primary answers
* -points: List\<Integer> → The number of points allocated to a team for getting a given answer correct
#### Relevant Methods: TODO: DEPENDING ON WHICH DESIGN PATTERN WE GO WITH FOR INSTANTIATING THESE, I THINK THE RELEVANT SETUP METHODS (setQuestion, setAnswers, setAnswerVariants, etc.) WILL BE DIFFERENT, SO I LEFT THEM BLANK FOR NOW
* +verifyAnswer(answer: String): Integer → Return the number of points associated with a given answer, with 0 if the answer is not correct
* +getQuestionText(): String → Return the main text of the question
### Entity 2
(Add additional entities as needed.)
---
## API Usage
**API Name: MongoDB**
**Documentation:** [Link]
**How it is used in the program:**
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
