# Workout Manager

## Team Information

### Team Members
- Jirat
- Demeng
- Eric
- David
- Amgalan

---

## Project Description

### Domain

A workout companion app that logs user exercises as well as having social capabilities.

---

## User Stories

### User Story 1

As a user, I want to register my account so that I can start using the app.

### User Story 2

As a beginner, I want to be able to search exercises by filters so I can find exercises I didn't know existed.

### User Story 3

As a regular user, I want to create a workout plan so I don't have to log the same exercises every time.

### User Story 4

As a user, I want to be able to edit my workout plan so I don't have to create a new one each time.

### User Story 5

As a user, I want to log my exercises to track my progress and look at my history.

### User Story 6

As a user, I want to see my history and personal records so I can see my own growth and feel motivated to keep working out.

### User Story 7

As a user with friends, I want to send my friends friend requests to check in on each other.

### User Story 8

As a user, I want to be able to respond to friend requests so I can choose whether I want to stay in touch or not.

### User Story 9

As a user, I want to copy my friend's workout plans so I can do the same exercises as my friends.

---

## Use Cases

### Register an Account

**Actor:** New user. **Precondition:** Signed out. **Trigger:** The user opens registration.

**Success outcome:** The account and profile are saved, and the user is signed in on Home.

1. The user submits an email address, username, display name, password, and password confirmation.
2. The system validates the details and creates the account.
3. The system loads the profile and opens Home.

**Extensions:**
- At step 2, if a detail is rejected, the system explains why and the user corrects it.
- At step 2, if the system can't tell whether the account was created, it suggests logging in first in case the account already exists.
- At step 3, if the profile can't be loaded or the email address needs confirming, the user stays signed out and can log in later.

### Log In

**Actor:** Registered user. **Precondition:** Signed out. **Trigger:** The user submits their login details.

**Success outcome:** The user is signed in and sees Home.

1. The user provides their email address and password.
2. The system verifies the credentials and loads the profile.
3. Home shows the user's display name and username.

**Extensions:** At step 2, invalid credentials, pending email confirmation, or a service failure leave the user signed out. The app explains what they can try next.

### Log Out

**Actor:** Signed-in user. **Precondition:** Home is open. **Trigger:** The user selects Log Out.

**Success outcome:** The user is signed out and sees Log In.

1. The system signs the user out and removes their details from the screen.
2. The system returns to Log In and confirms that the user is signed out.

**Extension:** At step 1, if the account service can't be reached, the user is still signed out on this computer.

### Find an Exercise

The user browses the exercise library, filters by name, muscle group or equipment, and opens an exercise to see its description, muscles worked, equipment and picture.

### Create a Workout Plan

The user starts a blank or preset plan, names it, adds exercises with target sets and reps, and saves it to their list of plans.

### Edit a Workout Plan

The user changes a plan's name, exercises or targets, or deletes the plan. Workouts already logged from it are kept unchanged.

### Log a Workout

The user starts a workout from a plan, records the weight and reps of each set (with a rest timer between sets), and finishes. The system saves the workout and shows a summary with any new personal records.

### Review Workout History and Personal Records

The user views their past workouts, newest first, and opens one to see its sets. Their profile shows total workouts and their heaviest lift for each exercise.

### Send a Friend Request

The user enters another user's username. If the request is allowed, the system records it as pending and shows it in the other user's requests.

### Respond to a Friend Request

The user views pending friend requests and accepts or declines each one. Accepting makes the two users friends, so they can see each other's profiles, plans and recent workouts.

### Copy a Friend's Workout Plan

The user opens a friend's profile and copies one of their plans. The system saves an independent copy to the user's own plans, named to show where it came from.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Jirat | Use Case |
| Demeng | Register an Account, Log In, Log Out |
| Eric | Use Case |
| David | Use Case |
| Amgalan | Use Case |

---

## Entities

### User
Instance Attributes:
- UserID (UUID)
- Username (String, unique)
- Display Name (String)
- Workout Plans (List of WorkoutPlan)
- Workout History (List of Workout)

Rules: a username is 3 to 24 lowercase letters, digits or underscores, and a display name can't be blank.

### Exercise
Instance Attributes:
- ExerciseID (String, from the WorkoutX API)
- Name (String)
- Description (String)
- Main Muscle Group (MuscleGroup enum)
- Muscles Worked (List of MuscleGroup)
- Equipment (String)
- Image URL (String, optional)

### PlannedExercise
Instance Attributes:
- Exercise (Exercise)
- Target Sets (int)
- Target Reps (int)
- Target Weight (double, kg, optional)

### WorkoutPlan
Instance Attributes:
- WorkoutPlanID (UUID)
- Name (String)
- UserOwner ID (UUID)
- Exercises (ordered List of PlannedExercise)
- WorkoutCompleted (boolean)
- Is Preset (boolean)
- Copied From User ID (UUID, optional)

### ExerciseSet
Instance Attributes:
- Weight (double, kg)
- Reps (int)

### LoggedExercise
Instance Attributes:
- Exercise (Exercise)
- Completed Sets (List of ExerciseSet)

### Workout
Instance Attributes:
- WorkoutID (UUID)
- UserOwner ID (UUID)
- Plan ID (UUID)
- Plan Name (String)
- Date (LocalDate)
- Exercises (List of LoggedExercise)

### FriendRequest
Instance Attributes:
- FriendReqID (UUID)
- Sender ID (UUID)
- Target ID (UUID)
- Status (RequestStatus enum: PENDING, ACCEPTED, DECLINED, CANCELLED)
- Created At (Instant)

### Friendship
Instance Attributes:
- User A (UUID, always the smaller of the two IDs)
- User B (UUID, always the larger of the two IDs)
- Since (Instant)

---

## API Usage

**API Name:** Supabase

**Documentation:** [Auth](https://supabase.com/docs/guides/auth/passwords), [Data REST API](https://supabase.com/docs/guides/api)

**How it is used in the program:** Supabase Auth handles registering, logging in and logging out. Each user's username and display name are stored in a `profiles` table, which the app loads after the user signs in.

**API Name:** WorkoutX

**Documentation:** [Link](https://workoutxapp.com/docs.html)

**How it is used in the program:** Planned for looking up exercises, including their descriptions, muscles worked, equipment and pictures.

---

## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

---

## How to Run

Install JDK 25, then run this from the repository root (use `.\mvnw.cmd` on Windows):

```sh
./mvnw compile exec:exec
```

You can also open `pom.xml` as a Maven project in your IDE and run `app.WorkoutManagerApp`. The app connects to the team's Supabase project, so no setup is needed.

---

## Notes

### Development

- `./mvnw test` checks style and runs the unit tests. `./mvnw verify` also writes a coverage report to `target/site/jacoco/index.html`.
- GitHub Actions runs the same checks on pull requests and pushes to `main`.
- To point the app at a different Supabase project, apply the [profile migration](supabase/migrations/20261123190718_account_profiles.sql), then set both `SUPABASE_URL` and `SUPABASE_PUBLISHABLE_KEY`.

### Using the signed-in user

- Interactors that need the signed-in user depend on `use_case.account.CurrentUserReader`, which `InMemorySessionStore` implements.
- Supabase DAOs send `InMemorySessionStore.accessToken()` as the bearer token so row-level security applies.
- A new screen gets a view model extending `interface_adapter.ViewModel` and is registered with `ViewManager.addView`. Presenters open it by setting `ViewManagerModel`'s state to its view name.
