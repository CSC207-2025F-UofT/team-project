# Team Project

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses 
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.

# Project Name

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

### As a user, I want to register my account so that I can start using the app.
### As a beginner, I want to be able to search exercises by filters so I can find exercises I didn't know existed.
### As a regular user, I want to create a workout plan so I don't have to log the same exercises every time.
### As a user, I want to be able to edit my workout plan so I don't have to create a new one each time.
### As a user, I want to log my exercises to track my progress and look at my history.
### As a user, I want to see my history and personal records so I can see my own growth and feel motivated to keep working out.
### As a user with friends, I want to send my friends friend requests to check in on each other.
### As a user, I want to be able to respond to friend requests so I can choose whether I want to stay in touch or not.
### As a user, I want to copy my friend's workout plans so I can do the same exercises as my friends.

---

## Use Cases

### Register an Account

A new user picks a username, display name and password. The system checks that the username is free and the password is valid, creates the account, and logs them in.

### Log In

A registered user enters their username and password. If they're correct, the system logs them in and shows their workout plans.

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
| Demeng | Use Case |
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
- Password (String)
- Workout Plans (List of WorkoutPlan)
- Workout History (List of Workout)

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
- WorkoutCompleted (boolean) :: assuming that we're going with you just clicking complete after u finish the workout (no individual completion tracking)
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

Name: WORKOUTX

Documentation: https://workoutxapp.com/docs.html

How we are using it: The user can select the exercises they want from the API and it provides back the corresponding description and video

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