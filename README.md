# Linked Out

## Team Information
### Team Members
- Pauline Nguyen
  - GitHub: https://github.com/papql 
  - Email: pauline.nguyen@mail.utoronto.ca
  - Message for Your Future Self: kinda want molly tea
    
- Lucy Liu
  - GitHub: https://github.com/NOVAx1216-coder
  - Email: lucymeichen.liu@mail.utoronto.ca
  - Message for Your Futures Self: Locked in and Linked Out!
    
- Joon Park
  - GitHub: https://github.com/kdaster
  - Email: jooner.park@mail.utoronto.ca
  - Message for Your Futures Self:
    
- Serena Ganbaatar
  - GitHub: https://github.com/serena-gb
  - Email: saruul.ganbaatar@mail.utoronto.ca
  - Message for Your Futures Self: This too shall pass.
    
- Issac Wan
  - GitHub: https://github.com/isaacwan-dev
  - Email: isaac.wan@mail.utoronto.ca
  - Message for Your Futures Self: We can do this
    
- Daniel Park
  - GitHub:
  - Email:
  - Message for Your Futures Self:
---
## Project Description
Turn everyday life into an adventure with Linked Out, a gamified productivity app that encourages users to explore new interests, step outside their comfort zones, and take on exciting side quests! From earning XP and leveling up to capturing memories and tracking accomplishments, Linked Out makes trying new things feel like a game.

### Domain
Side Quest App focused on Productivity & Entertainment

[Project description]
---

## User Stories
### We will remove this later but here is a template for how you can write your user stories: As a [kind of user] I want to [accomplish a goal] so that [I receive some benefit] -Pauline
### User Story 1: As a user, I want to login and set up my personal info (username, profile picture) and check out my current level so that I can visually personalize my account page.
### User Story 2: <Progress>
### User Story 3: As a user, I want to document my completed side quests through journal entries and photos (either taken with my camera or uploaded from my gallery) so that I can look back on my experiences, relive fun memories, and celebrate my achievements.
### User Story 4: <Map>
### User Story 5: <Recommendations Quests> As a user, I want to receive a list of recommended side quests so that I can choose side quests I am interested in.
### User Story 6: <Defaults/Adding Quests> As a user, I want to be able to log a side quest I completed as an entry in the app for other people to see, as well as browse a collection of default side quests to complete.
---
## Use Cases
### Use Case 1: LogIn, SetUsername, SetProfilePicture, CheckLevel
### Use Case 2: <Progress>
### Use Case 3: Create a Journal Entry
Lead: Pauline Nguyen
Primary actor: App user
Goal: Save a journal entry containing a written reflection and optional photos, associated with a selected quest.
Preconditions:
The user is logged in.
The user has a quest available (quest has status of completed, in progress or cancelled) that they can journal about.
Trigger: The user selects “Add Journal Entry” for a quest.
Main success scenario:
1. The system displays a journal entry form showing the selected quest.
2. The user enters a written reflection.
3. The user optionally imports photos.
4. The system displays the selected photos alongside the reflection.
5. The user selects “Save".
6. The system validates the entry.
7. The system saves the entry with its associated user and quest.
8. The system displays the saved entry and confirms that it was saved successfully.

Alternative flows and exceptions:
* Empty reflection: The system displays a validation message and allows the user to enter text.
* Unsupported or unreadable photo: The system displays an error and allows the user to select another photo or continue without it.
* Camera unavailable or permission denied: The user can import an existing photo or continue without one.
* Save fails: The system displays an error, retains the entered content, and allows the user to retry.
* User cancels: The system asks whether to discard unsaved changes. If confirmed, it exits without creating an entry.

Success postcondition: The journal entry is saved and available for the user to revisit.
Failure postcondition:No new journal entry is saved.

Features that can be added later:
* Editing and deleting existing entries.
* Searching or filtering entries.

### Use Case 4: <Map>
### Use Case 5: <Recommendations Quests>
### Use Case 6: <Defaults/Adding Quests>
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Pauline | Create a Text Journal Entry

Lead: Pauline Nguyen
Primary actor: App user

Related user story:
As a user, I want to save a written reflection about a side quest so that I can remember my experience.

Goal: Create and a text journal entry linked to a quest.

Preconditions:
The user is logged in.
The user has selected “Add Journal Entry”
The user has a quest available to journal about.

Trigger: The user taps on the any text block on the journal page for the quest.

Main success scenario:
1. The system displays the selected quest and a text field for the reflection.
2. The user enters a reflection.
3. The user selects “Save.”
4. The system checks that the reflection is not blank.
5. The system saves the entry with its user, and quest.
6. The system displays the saved entry.

Alternative flows and exceptions:
1. Blank reflection: The system displays “Please enter a reflection” and keeps the form open.
2. Save fails: The system displays an error and retains the reflection so the user can retry.
3. User cancels: The system exits without saving the entry.

Success postcondition:The entry is stored persistently and can be retrieved after restarting the application.
Failure postcondition: No new entry is saved.
 |
| Lucy | Use Case 1: LogIn, SetUsername |
| Joon | Use Case |
| Serena | Use Case |
| Issac | Use Case 6: Create Side Quests |
| Daniel | Use Case |

---
## Entities
### Entity 1: <Quest Entity>
### Entity 2: <Profile/Person Entity> User
### Entity 3
---
## API Usage
**API Name:**
**Documentation:** [Link]
**How it is used in the program:**

[Complete Later, Map API & Camera API]
---
## Design Diagrams
### UML Class Diagram 

### Sequence Diagrams


---
## How to Run <Complete Later After Implementation>
[Instructions for running the program.]
---
## Notes
Why We Chose The Name "Linked Out":

