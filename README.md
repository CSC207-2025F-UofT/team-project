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
  - GitHub: https://github.com/dannysauce25
  - Email: daniell.park@mail.utoronto.ca
  - Message for Your Futures Self: we got dis shi
---
## Project Description
Turn everyday life into an adventure with Linked Out, a gamified productivity app that encourages users to explore new interests, step outside their comfort zones, and take on exciting side quests! From earning XP and leveling up to capturing memories and tracking accomplishments, Linked Out makes trying new things feel like a game.

### Domain
Side Quest App focused on Productivity & Entertainment

---

## User Stories

We will remove this later but here is a template for how you can write your user stories: As a [kind of user] I want to [accomplish a goal] so that [I receive some benefit] -Pauline

* **User Story 1**: As a user, I want to set up my personal info (username, profile picture) and check out my current level so that I can visually personalize my account page.
* **User Story 2**: As a user, I want to accept side quests and track them on a dashboard, marking each one completed or abandoned and earning XP when I finish, so that I can see my progress and feel accomplished as my history grows.
* **User Story 3**: As a user, I want to document my completed side quests through journal entries and photos (either taken with my camera or uploaded from my gallery) so that I can look back on my experiences, relive fun memories, and celebrate my achievements.
* **User Story 4**: As a user, I want to be able to see nearby side quests.
* **User Story 5**: As a user, I want to receive a list of recommended side quests so that I can choose side quests I am interested in.
* **User Story 6**: As a user, I want to be able to log a side quest I completed as an entry in the app for other people to see, as well as browse a collection of default side quests to complete.

---
## Use Cases
### Use Case 1: LogIn, SetUsername, SetProfilePicture, CheckLevel
### Use Case 2: Progress
Lead: Daniel
2.1 Accept Quest

Main flow:
User clicks "Do this quest" on a quest (from the quest catalogue, recommendations, or a map pin).
System asks "Would you like to do this side quest?" and the user confirms.
System records the quest as PENDING for this user, with the current time as the start time.
System shows "Quest accepted!" and the quest appears under Pending on the dashboard.

Alternative flows:
The quest is already pending for this user: system shows "You're already doing this quest."
The user has already completed this quest: system shows "You've already completed this quest."
The quest doesn't exist: system shows an error.
The user cancels at step 2: nothing happens.

2.2 Complete Quest

Main flow:
User clicks "Complete" on a pending quest in the dashboard.
System checks that at least 10 minutes have passed since the quest was accepted and that the user has completed fewer than 5 quests today.
System marks the quest COMPLETED and adds its XP to the user.
System shows "+50 XP!" (and "Level up!" if the user's level went up).
System asks "Would you like to journal this?", which opens the journal entry (Use Case 3.x).

Alternative flows:
Less than 10 minutes since accepting: system shows "Too soon! Wait N more minutes before completing."
5 quests already completed today: system shows "You've hit today's limit of 5 quests. Come back tomorrow!"
The quest is not pending (already completed or abandoned): system shows an error.
The quest has been deleted: system shows "This quest no longer exists."

In every alternative flow, nothing is saved and no XP is given.

2.3 Abandon Quest

Main flow:
User clicks "Abandon" on a pending quest.
System asks "Are you sure? You won't get XP for this quest." and the user confirms.
System marks the quest ABANDONED and moves it to the Abandoned list.

Alternative flows:
The quest is not pending: system shows an error.
The user cancels at step 2: nothing happens.

2.4 View Dashboard 

Main flow:
User opens the Dashboard tab (it also refreshes after any of Use Cases 2.1 to 2.3).
System shows the user's quests in three lists, Pending, Completed, and Abandoned, newest first. Each row shows the title, theme, XP, and date.
System shows a summary: number of quests completed and total XP earned from quests.

Alternative flows:
The user has no quests: system shows "No quests yet!" with a button to the recommendations.

### Use Case 3: Create a Journal Entry
Lead: Pauline Nguyen <br>
Primary actor: App user <br>
Goal: Save a journal entry containing a written reflection and optional photos, associated with a selected quest.
Preconditions: <br>
The user is logged in. <br>
The user has a quest available (quest has status of completed, in progress or cancelled) that they can journal about. <br>
Trigger: The user selects “Add Journal Entry” for a quest. <br>
Main success scenario: <br>
1. The system displays a journal entry form showing the selected quest.
2. The user enters a written reflection.
3. The user optionally imports photos.
4. The system displays the selected photos alongside the reflection.
5. The user selects “Save".
6. The system validates the entry.
7. The system saves the entry with its associated user and quest.
8. The system displays the saved entry and confirms that it was saved successfully.

Alternative flows and exceptions:
1. Empty reflection: The system displays a validation message and allows the user to enter text.
2. Camera unavailable or permission denied: The user can import an existing photo or continue without one.
3. Save fails: The system displays an error, retains the entered content, and allows the user to retry.
4. User cancels: The system asks whether to discard unsaved changes. If confirmed, it exits without creating an entry.

Success postcondition: The journal entry is saved and available for the user to revisit. <br>
Failure postcondition:No new journal entry is saved. <br>

Features that can be added later: <br>
1. Editing and deleting existing entries.
2. Searching or filtering entries.

### Use Case 4: Open Map tab, select side quest pins that open up that particular quests page.
### Use Case 5: <Recommendations Quests>
### Use Case 6: <Defaults/Adding Quests>

---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Pauline |**Create a Text Journal Entry**<br><br>**Lead:** Pauline Nguyen<br>**Primary actor:** App user<br><br>**Goal:** Create and save a text journal entry linked to a quest.<br><br>**Preconditions:**<br>1. The user is logged in.<br>2. The user has a quest available to journal about.<br>3. The user is on the journal page for that quest.<br><br>**Trigger:** The user clicks a text block on the journal page for the quest.<br><br>**Main success scenario:**<br>1. The system displays the selected quest and a text field for the reflection.<br>2. The user enters a reflection.<br>3. The user selects “Save.”<br>4. The system checks that the reflection is not blank.<br>5. The system saves the entry with its associated user and quest.<br>6. The system displays the saved entry.<br><br>**Alternative flows and exceptions:**<br>1. Blank reflection: The system displays “Please enter a reflection” and keeps the form open.<br>2. Save fails: The system displays an error and retains the reflection so the user can retry.<br>3. User cancels: The system exits without saving the entry.<br><br>**Success postcondition:** The entry is stored persistently and can be retrieved after restarting the application.<br><br>**Failure postcondition:** No new entry is saved. |
 |
| Lucy | Use Case 1: LogIn, SetUsername |
| Joon | Use Case |
| Serena | Use case 4: Open Map tab, select side quest pins that open up that particular quests page. |
| Issac | Use Case 6: Create Side Quests |
| Daniel |
2.1 Accept Quest	Daniel	MVP
2.2 Complete Quest	Daniel	MVP
2.3 Abandon Quest	Daniel	MVP
2.4 View Dashboard	Daniel	MVP
2.5 Star / Unstar Theme	Daniel	Later |

---
## Entities
### Entity 1: <Quest Entity>
### Entity 2: <Profile/Person Entity>
### Entity 3: 

UserQuest (Daniel): one user's progress on one quest.
Instance variables: username: String, questId: String, status: QuestStatus, startedAt: LocalDateTime, completedAt: LocalDateTime (null unless completed), abandonedAt: LocalDateTime (null unless abandoned)
Constant: MIN_MINUTES_BEFORE_COMPLETE = 10
Methods: getters, isPending(), meetsMinimumTime(now), minutesUntilCompletable(now), complete(now), abandon(now). complete and abandon throw IllegalStateException if the quest isn't pending.

QuestStatus (Daniel): enum PENDING, COMPLETED, ABANDONED. Status only moves PENDING → COMPLETED or PENDING → ABANDONED.

Full design for this part (classes per layer, data flow, teammate touchpoints): docs/DASHBOARD.md
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

