# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
    - A study app that tracks assignments todo tasks. Our app provides a calming relaxing environment for studying by having study sessions and breaks. 
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses
  - RANDOM.ORG: random number generation https://api.random.org/dashboard
  - CountdownShare: countdown timer https://countdownshare.com/api/v1
  - Figma: Images https://developers.figma.com/docs/rest-api/
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.

# Project Name
## Team Information
### Team Members
- Ava
- Ema
- Grace
- Miray
- Sophia
---
## Project Description
### Domain
[Project description]
-

## User Stories (For each group member):

### User Story 1 - Ema:
As a student, I want to generate flashcards in a timely manner so that I could study for my classes 

### User Story 2 - Grace:
As a student, I want to start a focus timer so that I can study without any distractions 

### User Story 3 - Ava:
As a student, I want to start a break timer so that I can take periodic breaks after studying. 
As a student playing the break game, I want to receive feedback on my submitted code so that I figure out the secret code. 

### User Story 4 - Miray:
As a student, I want to play a game during my break so that I can wind down and relax after my study session. 

### User Story 5 - Sophia:
As a student, I want to review my flashcards so I can remember the material. 

## Use Cases
### Use Case 1
### Use Case 2 
Name: Generating flashcards
Primary Actor: Student

Preconditions: Student has text formatted correctly (with titled bolded/underlined and body for flashcard DIRECTLY underneath)

Trigger: Clicking the generate button

Main Success Scenario:
1. Student goes to flashcard page
2. Student copies and pastes text into textbox meant for where you insert flashcards
3. Student formats accordingly 
4. Student hits generate flashcards and program generates and shows flashcards to students 
5. Student now has access to flashcards as needed

    
Extensions:
If user enters no text, message will prompt them to enter text first 
Success outcome: Student can generate flashcards successfully

### Use Case 3 
Name: Evaluate Guess and Generate Feedback 

Primary Actor: Student

Preconditions: Student is playing CandleGame, Break Timer is still ongoing

Trigger: The student enters a guess and clicks the submit button.

Success Outcome: The student's guess is checked with secret code, candle feedback is generated for each guessed digit, game condition and guess are updated, and the candle feedback is displayed to student.

Main Success Scenario:
1. Student enters 5 digit guessed code in game and displayed in real time
2. Student submits the code via Submit button. (Invalid inputs are ignored and not written)
3. The system receives the guessed code.
4. The system compares the guessed code to secret code.
5. The system then generates candle colour feedback based on the placement and correctness of each digit (Unlit: Digit not in secret code, Yellow: Digit is in secret code but in wrong place, Orange: Digit is in secret code and in the right place)
6. The system records the guess and feedback.
7. The system checks game conditions (Win, Loss, Ongoing) and updates it.
8. The system uses feedback to display the candle colours, remaining attempts, and status to the student.

Extensions: Student guesses the secret code correctly
1. The system updates the game condition to WON 
2. The system displays a winning message and displays candle colours to the student.
3. The student can quit the game and return to break screen.

Student's guess is invalid 
1. The system detects that the guess has invalid characters.
2. The submit button is disabled until a valid guess is detected. 
### Use Case 4 
### Use Case 5   

---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |
--

## Entities
### Entity 1
### Entity 2
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
[Any additional project information.]
