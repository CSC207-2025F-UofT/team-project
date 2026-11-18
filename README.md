# Time Tracker

## Team Information
### Team Members
- Minsung Kim
- Arish Zobeyer
- Ethan Seet
- Liam Evans

---

## Project Description
### Domain: Productivity

This is a desktop application for budgeting your time. The main page is a dashboard with a chart (pie or bar graph) of the time spent on every activity you log. The menu bar on the right has Punch In and Punch Out buttons, along with the activity name. You can also view your time usage daily, weekly and monthly, similar to screen time on a phone. Each activity can have a time limit ("time I'm willing to spend"), and the app reminds you when you go over it. An external API groups similar activities into categories (e.g. "note-taking" → Study).

The purpose of the app is to help people see accurately how they use their time. If they notice time going to things that aren't worth it, they can change their habits.

---

## User Stories
1. As a student, I want to see how much time I spend on gaming so that I can reallocate that time to studying.
2. As a user, I want to view, edit and delete previously added time entries so that I can correct mistakes.
3. As a user, I want to see which activities I spend the most time on in a week or month.
4. As a user, I want to be able to delete my account and its data.
5. As a user, I want to receive a reminder when I exceed the time limit I set for an activity.
6. As a user, I want to create an account and log in so that my time data is private to me and saved between sessions.

---

## Use Cases

### Use Case: Sign Up
**Lead:** Arish Zobeyer
- **Main flow:**
  1. The user opens the app and clicks "No account? Sign up".
  2. The user enters a username, a password and the password again, then clicks "Sign up".
  3. The system creates the account, saves it, and switches to the login screen with the username filled in.
- **Alternative flows:**
  - Username or password is empty → the system shows "Username and password can't be empty."
  - Username is already taken → the system shows "User already exists."
  - The passwords don't match → the system shows "Passwords don't match."
  - The user clicks "Already have an account? Log in" → the system switches to the login screen.

### Use Case: Log In
**Lead:** Arish Zobeyer
- **Main flow:**
  1. The user enters their username and password and clicks "Log in".
  2. The system checks the credentials, sets the user as the current user, and switches to the dashboard.
- **Alternative flows:**
  - Username or password is empty → the system shows "Username and password can't be empty."
  - The account doesn't exist → the system shows "<username>: Account does not exist."
  - The password is wrong → the system shows an incorrect-password error, and the user stays on the login screen.
  - The user clicks "No account? Sign up" → the system switches to the signup screen.

### Use Case: Log Out
**Lead:** Arish Zobeyer
- **Main flow:**
  1. From the dashboard, the user clicks "Log out".
  2. The system clears the current user and returns to the login screen with the username filled in.
- **Alternative flow:**
  - No user is logged in → the system shows "No user is logged in."

### Use Case: Start / Stop Activity
**Lead:** TBD
- **Main flow:** The user enters or selects an activity and clicks "Punch In". The system records the start time. The user clicks "Punch Out", and the system saves a time entry and updates the dashboard.
- **Alternative flows:** No activity is selected → error. An activity is already running → the user must stop it first.

### Use Case: Create / Delete Activity
**Lead:** TBD
- **Main flow:** The user creates an activity by name. The system categorizes it with the external API and saves it.
- **Alternative flows:** The name is empty or a duplicate → error. The API is unavailable → keyword-based fallback categorization.

### Use Case: Edit / Delete Time Entry
**Lead:** TBD
- **Main flow:** The user opens the list of past entries, selects one, and edits its times or deletes it. The system saves the change and updates the totals.
- **Alternative flow:** The end time is before the start time → error.

### Use Case: View Summary (Daily / Weekly / Monthly)
**Lead:** TBD
- **Main flow:** The user chooses a period. The system totals time per activity and category for that period and shows a chart, with the most-used activities first.
- **Alternative flow:** There are no entries in the period → an empty-state message.

### Use Case: Set / Check Time Limit
**Lead:** TBD
- **Main flow:** The user sets a time limit on an activity. When the tracked time for that activity exceeds the limit, the system shows a reminder notification.
- **Alternative flow:** The limit is ≤ 0 → error.

### Use Case: Delete Account
**Lead:** TBD
- **Main flow:** The user clicks "Delete account" and confirms. The system deletes the user and all of their activities and time entries, then returns to the login screen.
- **Alternative flow:** The user cancels → nothing is deleted.

---

## Minimum Viable Product (MVP)
| Team Member | MVP Use Case(s) |
|-------------|-----------------|
| Arish Zobeyer | Sign Up, Log In, Log Out |
| Minsung Kim | TBD |
| Ethan Seet | TBD |
| Liam Evans | TBD |

---

## Entities

### User
- **Instance variables:** `username: String`, `password: String`
- **Methods:** `getUsername()`, `getPassword()`
- Created through `UserFactory.create(username, password)`. Other entities refer to their owner by `username`.

### Activity
- **Instance variables:** `name: String`, `category: ActivityCategory`, `timeLimit: Duration` (optional), `ownerUsername: String`
- **Methods:** getters, `hasTimeLimit()`

### ActivityCategory
- **Values:** e.g. Study, Work, Exercise, Gaming, Leisure

### TimeEntry
- **Instance variables:** `activity: Activity`, `start: Instant`, `end: Instant`
- **Methods:** `getDuration()`

### TimeDuration
- A value object wrapping `java.time.Duration`, with helpers for summing and formatting (e.g. "2h 15m").
- Totals per activity are computed from time entries when needed (`Map<Activity, Duration>`) rather than stored separately.

---

## API Usage
**API name:** TBD (candidates: Google Gemini API, Hugging Face zero-shot classification)
**Documentation:** [Gemini API](https://ai.google.dev/gemini-api/docs) / [Hugging Face Inference](https://huggingface.co/docs/inference-providers)
**How it is used in the program:**
- When a user creates an activity, its name and the list of allowed categories are sent to the API, which returns the best-matching category (e.g. "note-taking" → Study).
- The API sits behind an `ActivityCategorizer` interface in the use case layer, with the HTTP implementation in the data access layer. A keyword-based fallback keeps the app working offline and lets tests run without the network.
- The API key is read from an environment variable and never committed to the repo.

**Status:** Not yet called successfully.

---

## Design
### Architecture
The project follows Clean Architecture:
- `entity/`: enterprise business rules (User, Activity, TimeEntry, …)
- `use_case/`: one package per use case (Interactor, Input/Output Boundary, Input/Output Data, Data Access Interface)
- `interface_adapter/`: controllers, presenters, view models
- `data_access/`: storage implementations (in-memory for tests, JSON file) and API clients
- `view/`: Java Swing screens, switched with a `CardLayout` through `ViewManager`
- `app/`: `AppBuilder` (Builder pattern) and `Main`, which wire everything together

### SOLID
- **S:** one interactor per use case; controllers and presenters are separate classes.
- **O:** new storage backends or categorizers can be added without changing use cases.
- **L:** `InMemoryUserDataAccessObject` and `FileUserDataAccessObject` are interchangeable; tests use the in-memory one.
- **I:** each use case declares its own small data access interface (e.g. `SignupUserDataAccessInterface`).
- **D:** interactors depend only on interfaces; `Main` and `AppBuilder` choose the concrete classes.

### UML Class Diagram
TBD

### Sequence Diagrams
TBD

---

## How to Run
1. Install JDK 17 or newer, and Maven (bundled with IntelliJ).
2. Clone the repo and open it in IntelliJ as a Maven project.
3. Run `app.Main`.
4. Run tests with `mvn test`, or right-click `src/test/java` → Run All Tests.

User accounts are saved to `users.json` in the project root (gitignored).

---

## Notes
- Passwords are currently stored in plain text. A planned improvement is to hash them (e.g. SHA-256 with a salt).
- `LoggedInView` is a placeholder until the dashboard is implemented.
