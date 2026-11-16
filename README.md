# Linked Out

## Table of Contents

- [Team Information](#team-information)
  - [Team Members](#team-members)
- [Project Description](#project-description)
  - [Domain](#domain)
- [User Stories](#user-stories)
- [Use Cases](#use-cases)
  - [Use Case 1: LogIn, SetUsername, SetProfilePicture, CheckLevel](#use-case-1-login-setusername-setprofilepicture-checklevel)
  - [Use Case 2: Progress](#use-case-2-progress)
    - [2.1 Accept Quest](#21-accept-quest)
    - [2.2 Complete Quest](#22-complete-quest)
    - [2.3 Abandon Quest](#23-abandon-quest)
    - [2.4 View Dashboard](#24-view-dashboard)
  - [Use Case 3: Create a Journal Entry](#use-case-3-create-a-journal-entry)
  - [Use Case 4: Find a Nearby Side Quest on the Map](#use-case-4-find-a-nearby-side-quest-on-the-map)
  - [Use Case 5: Recommendations Quests](#use-case-5-recommendations-quests)
  - [Use Case 6: Defaults/Adding Quests](#use-case-6-defaultsadding-quests)
- [Minimal Viable Product (MVP)](#minimal-viable-product-mvp)
  - [Pauline](#pauline)
  - [Serena](#serena)
  - [Daniel](#daniel)
- [Entities](#entities)
  - [Entity 1: Quest Entity](#entity-1-quest-entity)
  - [Entity 2: User (Profile/Person Entity)](#entity-2-user-profileperson-entity)
  - [Entity 3: UserQuest and QuestStatus](#entity-3-userquest-and-queststatus)
  - [Entity 4: Location](#entity-4-location)
  - [Entity 5: SearchArea](#entity-5-searcharea)
- [API Usage](#api-usage)
- [Design Diagrams](#design-diagrams)
  - [UML Class Diagram](#uml-class-diagram)
  - [Sequence Diagrams](#sequence-diagrams)
- [How to Run](#how-to-run-complete-later-after-implementation)
- [Notes](#notes)

---

## Team Information

### Team Members

| Name | GitHub | Email | Message for Your Future Self |
|------|--------|-------|------------------------------|
| Pauline Nguyen | [papql](https://github.com/papql) | pauline.nguyen@mail.utoronto.ca | kinda want molly tea |
| Lucy Liu | [NOVAx1216-coder](https://github.com/NOVAx1216-coder) | lucymeichen.liu@mail.utoronto.ca | Locked in and Linked Out! |
| Joon Park | [kdaster](https://github.com/kdaster) | jooner.park@mail.utoronto.ca | |
| Serena Ganbaatar | [serena-gb](https://github.com/serena-gb) | saruul.ganbaatar@mail.utoronto.ca | This too shall pass. |
| Issac Wan | [isaacwan-dev](https://github.com/isaacwan-dev) | isaac.wan@mail.utoronto.ca | We can do this |
| Daniel Park | [dannysauce25](https://github.com/dannysauce25) | daniell.park@mail.utoronto.ca | we got dis shi |

[Back to top](#linked-out)

---

## Project Description

Turn everyday life into an adventure with Linked Out, a gamified productivity app that encourages users to explore new interests, step outside their comfort zones, and take on exciting side quests! From earning XP and leveling up to capturing memories and tracking accomplishments, Linked Out makes trying new things feel like a game.

### Domain

Side Quest App focused on Productivity & Entertainment

[Back to top](#linked-out)

---

## User Stories

* **User Story 1**: As a user, I want to set up my personal info (username, profile picture) and check out my current level so that I can visually personalize my account page.
* **User Story 2**: As a user, I want to accept side quests and track them on a dashboard, marking each one completed or abandoned and earning XP when I finish, so that I can see my progress and feel accomplished as my history grows.
* **User Story 3**: As a user, I want to document my completed side quests through journal entries and photos (either taken with my camera or uploaded from my gallery) so that I can look back on my experiences, relive fun memories, and celebrate my achievements.
* **User Story 4**: As a user, I want to be able to see nearby side quests.
* **User Story 5**: As a user, I want to receive a list of recommended side quests so that I can choose side quests I am interested in.
* **User Story 6**: As a user, I want to be able to log a side quest I completed as an entry in the app for other people to see, as well as browse a collection of default side quests to complete.

[Back to top](#linked-out)

---

## Use Cases

### Use Case 1: LogIn, SetUsername, SetProfilePicture, CheckLevel

### Use Case 2: Progress

**Lead:** Daniel

#### 2.1 Accept Quest

<details>
<summary>Main flow and alternative flows</summary>

**Main flow:**

1. User clicks "Do this quest" on a quest (from the quest catalogue, recommendations, or a map pin).
2. System asks "Would you like to do this side quest?" and the user confirms.
3. System records the quest as PENDING for this user, with the current time as the start time.
4. System shows "Quest accepted!" and the quest appears under Pending on the dashboard.

**Alternative flows:**

* The quest is already pending for this user: system shows "You're already doing this quest."
* The user has already completed this quest: system shows "You've already completed this quest."
* The quest doesn't exist: system shows an error.
* The user cancels at step 2: nothing happens.

</details>

#### 2.2 Complete Quest

<details>
<summary>Main flow and alternative flows</summary>

**Main flow:**

1. User clicks "Complete" on a pending quest in the dashboard.
2. System checks that at least 10 minutes have passed since the quest was accepted and that the user has completed fewer than 5 quests today.
3. System marks the quest COMPLETED and adds its XP to the user.
4. System shows "+50 XP!" (and "Level up!" if the user's level went up).
5. System asks "Would you like to journal this?", which opens the journal entry ([Use Case 3.x](#use-case-3-create-a-journal-entry)).

**Alternative flows:**

* Less than 10 minutes since accepting: system shows "Too soon! Wait N more minutes before completing."
* 5 quests already completed today: system shows "You've hit today's limit of 5 quests. Come back tomorrow!"
* The quest is not pending (already completed or abandoned): system shows an error.
* The quest has been deleted: system shows "This quest no longer exists."

In every alternative flow, nothing is saved and no XP is given.

</details>

#### 2.3 Abandon Quest

<details>
<summary>Main flow and alternative flows</summary>

**Main flow:**

1. User clicks "Abandon" on a pending quest.
2. System asks "Are you sure? You won't get XP for this quest." and the user confirms.
3. System marks the quest ABANDONED and moves it to the Abandoned list.

**Alternative flows:**

* The quest is not pending: system shows an error.
* The user cancels at step 2: nothing happens.

</details>

#### 2.4 View Dashboard

<details>
<summary>Main flow and alternative flows</summary>

**Main flow:**

1. User opens the Dashboard tab (it also refreshes after any of Use Cases [2.1](#21-accept-quest) to [2.3](#23-abandon-quest)).
2. System shows the user's quests in three lists, Pending, Completed, and Abandoned, newest first. Each row shows the title, theme, XP, and date.
3. System shows a summary: number of quests completed and total XP earned from quests.

**Alternative flows:**

* The user has no quests: system shows "No quests yet!" with a button to the recommendations.

</details>

### Use Case 3: Create a Journal Entry

**Lead:** Pauline Nguyen <br>
**Primary actor:** App user <br>
**Goal:** Save a journal entry containing a written reflection and optional photos, associated with a selected quest.

<details>
<summary>Full use case</summary>

**Preconditions:**

1. The user is logged in.
2. The user has a quest available (quest has status of completed, in progress or cancelled) that they can journal about.

**Trigger:** The user selects “Add Journal Entry” for a quest.

**Main success scenario:**

1. The system displays a journal entry form showing the selected quest.
2. The user enters a written reflection.
3. The user optionally imports photos.
4. The system displays the selected photos alongside the reflection.
5. The user selects "Save".
6. The system validates the entry.
7. The system saves the entry with its associated user and quest.
8. The system displays the saved entry and confirms that it was saved successfully.

**Alternative flows and exceptions:**

1. Empty reflection: The system displays a validation message and allows the user to enter text.
2. Camera unavailable or permission denied: The user can import an existing photo or continue without one.
3. Save fails: The system displays an error, retains the entered content, and allows the user to retry.
4. User cancels: The system asks whether to discard unsaved changes. If confirmed, it exits without creating an entry.

**Success postcondition:** The journal entry is saved and available for the user to revisit. <br>
**Failure postcondition:** No new journal entry is saved.

**Features that can be added later:**

1. Editing and deleting existing entries.
2. Searching or filtering entries.

</details>

### Use Case 4: Find a Nearby Side Quest on the Map

**Lead:** Serena Ganbaatar <br>
**Primary actor:** App user <br>
**Goal:** See which side quests are near the user and open one of them from the map.

<details>
<summary>Full use case</summary>

**Preconditions:**

1. The user is logged in.
2. The system has a location for the user. In the MVP this is a fixed default (U of T St. George campus).

**Trigger:** The user opens the Map tab.

**Main success scenario:**

1. The system reads the user's location and the current search radius (2 km the first time).
2. The system loads the side quests that have a location and keeps the ones inside the radius.
3. The system displays a map centred on the user, with a "you are here" marker and one pin for each nearby quest.
4. The user selects a pin.
5. The system shows a preview of that quest: title, theme, XP, and distance from the user.
6. The user selects "Open quest".
7. The system opens that quest's page.

**Alternative flows and exceptions:**

* 2a. Quests cannot be loaded: The system displays the map and the "you are here" marker with no pins, and shows an error with a "Retry" option.
* 3a. No quests inside the radius: The system displays the map with no pins and the message "No side quests within N km. Try a larger radius."
* 3b. Map images cannot be downloaded (for example, no internet): The marker and pins are still drawn, on a blank background, so the user can still select a pin.
* 4a. The user changes the radius instead of selecting a pin: The system repeats steps 2 and 3 with the new radius.
* 4b. The radius is outside the allowed range (0.5 km to 10 km): The system shows an error and keeps the previous radius.
* 6a. The user closes the preview: The system returns to step 3 with the same pins.
* 7a. The quest was deleted after the pins were drawn: The system shows "This quest no longer exists." and reloads the pins.

**Success postcondition:** The selected quest's page is displayed. Nothing is saved or changed, because this use case only reads data. <br>
**Failure postcondition:** The user stays on the Map tab and no data is changed.

**Features that can be added later:**

1. Real user location in place of the fixed default: the user types an address that a geocoding API converts to coordinates, or the location is looked up from the IP address.
2. Filtering pins by theme.
3. Choosing a quest's location by dropping a pin on the map when adding a quest (with [Use Case 6](#use-case-6-defaultsadding-quests)).
4. Different pin colours for pending and completed quests (with [Use Case 2](#use-case-2-progress)).

</details>

### Use Case 5: Recommendations Quests

### Use Case 6: Defaults/Adding Quests

[Back to top](#linked-out)

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Pauline | [Create a Text Journal Entry](#pauline) |
| Lucy | Use Case 1: LogIn, SetUsername |
| Joon | Use Case |
| Serena | [Use Case 4: Find a Nearby Side Quest on the Map](#serena) |
| Issac | Use Case 6: Create Side Quests |
| Daniel | [Use Case 2.1: Accept Quest, Use Case 2.2: Complete Quest, Use Case 2.3: Abandon Quest, Use Case 2.4: View Dashboard](#daniel) |

### Pauline

<details>
<summary><b>Create a Text Journal Entry</b></summary>

**Lead:** Pauline Nguyen <br>
**Primary actor:** App user

**Goal:** Create and save a text journal entry linked to a quest.

**Preconditions:**

1. The user is logged in.
2. The user has a quest available to journal about.
3. The user is on the journal page for that quest.

**Trigger:** The user clicks a text block on the journal page for the quest.

**Main success scenario:**

1. The system displays the selected quest and a text field for the reflection.
2. The user enters a reflection.
3. The user selects “Save.”
4. The system checks that the reflection is not blank.
5. The system saves the entry with its associated user and quest.
6. The system displays the saved entry.

**Alternative flows and exceptions:**

1. Blank reflection: The system displays “Please enter a reflection” and keeps the form open.
2. Save fails: The system displays an error and retains the reflection so the user can retry.
3. User cancels: The system exits without saving the entry.

**Success postcondition:** The entry is stored persistently and can be retrieved after restarting the application.

**Failure postcondition:** No new entry is saved.

</details>

### Serena

<details>
<summary><b>Use Case 4: Find a Nearby Side Quest on the Map</b></summary>

**Lead:** Serena Ganbaatar <br>
**Primary actor:** App user

**Goal:** See the side quests within a chosen radius of a fixed location and open one from its pin.

**MVP limits:**

1. The user's location is a fixed default (U of T St. George campus). The app does not detect or ask for a real location.
2. The radius is picked from preset values: 0.5, 1, 2, 5, or 10 km. The default is 2 km.
3. The map shows a "you are here" marker and quest pins. There is no filtering by theme and no pin colour coding.

**Preconditions:**

1. The user is logged in.

**Trigger:** The user opens the Map tab.

**Main success scenario:**

1. The system reads the default location and the current radius.
2. The system loads the quests that have a location and keeps the ones inside the radius.
3. The system displays a map centred on the default location, with a "you are here" marker and one pin per nearby quest.
4. The user selects a pin.
5. The system shows a preview with the quest's title, theme, XP, and distance.
6. The user selects "Open quest".
7. The system opens that quest's page.

**Alternative flows and exceptions:**

1. Quests cannot be loaded: The system displays the map and marker with no pins, and shows an error with a "Retry" option.
2. No quests inside the radius: The system displays "No side quests within N km. Try a larger radius."
3. Map images cannot be downloaded: The marker and pins are still drawn on a blank background.
4. User changes the radius: The system reloads the pins for the new radius.
5. User closes the preview: The system returns to the map with the same pins.

**Success postcondition:** The selected quest's page is displayed. No data is changed.

**Failure postcondition:** The user stays on the Map tab and no data is changed.

</details>

### Daniel

<details>
<summary><b>Use Case 2.1: Accept Quest</b></summary>

**Lead:** Daniel Park <br>
**Primary actor:** App user

**Goal:** Start a quest so it appears as pending on the dashboard.

**Preconditions:**

1. The user is logged in.
2. The quest exists.
3. The user is viewing the quest in the quest catalogue, recommendations, or map.

**Trigger:** The user clicks “Do this quest.”

**Main success scenario:**

1. The system displays “Would you like to do this side quest?”
2. The user confirms.
3. The system checks that the quest is not already pending or completed for this user.
4. The system saves the quest as PENDING with the current time as its start time.
5. The system displays “Quest accepted!” and the quest appears under Pending on the dashboard.

**Alternative flows and exceptions:**

1. Already pending: The system displays “You're already doing this quest.”
2. Already completed: The system displays “You've already completed this quest.”
3. Quest no longer exists: The system displays an error.
4. User cancels: The system exits without saving.

**Success postcondition:** The quest is stored as pending with its start time and can be retrieved after restarting the application.

**Failure postcondition:** Nothing is saved.

</details>

<details>
<summary><b>Use Case 2.2: Complete Quest</b></summary>

**Lead:** Daniel Park <br>
**Primary actor:** App user

**Goal:** Mark a pending quest as completed and earn its XP.

**Preconditions:**

1. The user is logged in.
2. The user has accepted the quest and it is still pending.
3. The user is on the dashboard.

**Trigger:** The user clicks “Complete” on a pending quest.

**Main success scenario:**

1. The system checks that the quest is pending.
2. The system checks that the user has completed fewer than 5 quests today.
3. The system checks that at least 10 minutes have passed since the quest was accepted.
4. The system marks the quest COMPLETED with the current time.
5. The system adds the quest's XP to the user.
6. The system saves the quest and the user.
7. The system displays “+50 XP!” and “Level up!” if the user's level increased.
8. The system asks “Would you like to journal this?” and opens the journal entry if the user agrees.

**Alternative flows and exceptions:**

1. Too soon: The system displays “Too soon! Wait N more minutes before completing.”
2. Daily limit reached: The system displays “You've hit today's limit of 5 quests. Come back tomorrow!”
3. Quest not pending: The system displays “This quest is already completed.” (or abandoned).
4. Quest deleted: The system displays “This quest no longer exists.”
5. User declines to journal: The system returns to the dashboard. The completion is still saved.

**Success postcondition:** The quest is stored as completed with its completion time, the user's XP is increased, and both can be retrieved after restarting the application.

**Failure postcondition:** The quest stays pending and no XP is given.

</details>

<details>
<summary><b>Use Case 2.3: Abandon Quest</b></summary>

**Lead:** Daniel Park <br>
**Primary actor:** App user

**Goal:** Give up on a pending quest.

**Preconditions:**

1. The user is logged in.
2. The quest is pending for this user.
3. The user is on the dashboard.

**Trigger:** The user clicks “Abandon” on a pending quest.

**Main success scenario:**

1. The system displays “Are you sure? You won't get XP for this quest.”
2. The user confirms.
3. The system checks that the quest is pending.
4. The system marks the quest ABANDONED with the current time and saves it.
5. The system moves the quest to the Abandoned list.

**Alternative flows and exceptions:**

1. Quest not pending: The system displays an error.
2. User cancels: The system exits without saving.

**Success postcondition:** The quest is stored as abandoned and no XP is given.

**Failure postcondition:** The quest's status is unchanged.

</details>

<details>
<summary><b>Use Case 2.4: View Dashboard</b></summary>

**Lead:** Daniel Park <br>
**Primary actor:** App user

**Goal:** See all of the user's quests grouped by status.

**Preconditions:**

1. The user is logged in.

**Trigger:** The user opens the Dashboard tab, or Use Case 2.1, 2.2, or 2.3 finishes.

**Main success scenario:**

1. The system loads the user's quests.
2. The system groups them into Pending, Completed, and Abandoned, newest first.
3. Each row shows the quest's title, theme, XP, and date.
4. The system displays a summary of quests completed and total XP earned from quests.
5. Pending rows show “Complete” and “Abandon” buttons.

**Alternative flows and exceptions:**

1. No quests: The system displays “No quests yet!” with a button to the recommendations.
2. A quest was deleted: The system skips that row.
3. Loading fails: The system displays an error and shows empty lists.

**Success postcondition:** The dashboard shows the user's saved quest history.

**Failure postcondition:** An error is shown and no data is changed.

</details>

**Later (not MVP):** Use Case 2.5: Star / Unstar Theme

[Back to top](#linked-out)

---

## Entities

### Entity 1: Quest Entity

### Entity 2: User (Profile/Person Entity)

### Entity 3: UserQuest and QuestStatus

<details>
<summary>Entity details</summary>

**UserQuest** (Daniel): one user's progress on one quest.

**Instance variables:** username: String, questId: String, status: QuestStatus, startedAt: LocalDateTime, completedAt: LocalDateTime (null unless completed), abandonedAt: LocalDateTime (null unless abandoned)

**Constant:** MIN_MINUTES_BEFORE_COMPLETE = 10

**Methods:** getters, isPending(), meetsMinimumTime(now), minutesUntilCompletable(now), complete(now), abandon(now). complete and abandon throw IllegalStateException if the quest isn't pending.

**QuestStatus** (Daniel): enum PENDING, COMPLETED, ABANDONED. Status only moves PENDING → COMPLETED or PENDING → ABANDONED.

Full design for this part (classes per layer, data flow, teammate touchpoints): docs/DASHBOARD.md

</details>

### Entity 4: Location

One point on the Earth. It is the only class that knows how coordinates are checked and how distance is measured.

<details>
<summary>Entity details</summary>

**Instance variables:** latitude: double, longitude: double (both final, so a Location never changes after it is created)

**Methods:** getters, distanceTo(other: Location): double, equals, hashCode. The constructor throws IllegalArgumentException if latitude is outside -90 to 90 or longitude is outside -180 to 180. distanceTo returns the distance in kilometres along the Earth's surface (haversine formula).

**Reason to change:** the way a position is represented or the way distance is calculated.

</details>

### Entity 5: SearchArea

A circle on the map: a centre and a radius. It holds the rule for what counts as "nearby".

<details>
<summary>Entity details</summary>

**Instance variables:** centre: Location, radiusKm: double

**Constants:** MIN_RADIUS_KM = 0.5, MAX_RADIUS_KM = 10, DEFAULT_RADIUS_KM = 2

**Methods:** getters, contains(point: Location): boolean. The constructor throws IllegalArgumentException if the radius is outside MIN_RADIUS_KM to MAX_RADIUS_KM. contains returns true when centre.distanceTo(point) is at most radiusKm.

**Reason to change:** the rule for which quests count as nearby (for example, new radius limits).

</details>

[Back to top](#linked-out)

---

## API Usage

* **API Name:**
* **Documentation:** [Link]
* **How it is used in the program:**

[Complete Later, Map API & Camera API]

[Back to top](#linked-out)

---

## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

[Back to top](#linked-out)

---

## How to Run (Complete Later After Implementation)

[Instructions for running the program.]

[Back to top](#linked-out)

---

## Notes

**Why We Chose The Name "Linked Out":**

[Back to top](#linked-out)
