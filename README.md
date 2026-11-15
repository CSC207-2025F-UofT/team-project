# Project Name
Group 03 - BA2200

## Team Information

### Team Members
- Eva Verkhovsky
- Xia Rao
- Lufei Zhang
- Anjie Zhou

---

## Project Description

### Domain

***Student Task Planner*** - An App to Plan, Organize, and Prioritize Academic Tasks and Assignments:
- This app helps students organize assignments and track academic deadlines. 
- It helps students prioritize important tasks and plan their daily study workload. 
- Students can also track their progress, manage their study time effectively, and stay on top of their coursework.

---

## Epic
### Minimum Viable Product (MVP) Epic Overview
- As a university student, I want to ***create***, ***view***, ***update***, and ***monitor*** upcoming ***academic deadlines*** and ***record*** ***task completion*** progress in one centralized desktop application so that I can stay organized, ***identify*** upcoming assignments, and manage my academic responsibilities effectively.
  - Automatically ***warn*** students when a deadline falls on a public holiday.
    - Use ***Nager.Date Public Holiday API***.
  - Each task would have the option to have an estimated number of hours for completing it.
  - ***Sort*** by difficulty and sort by hours per day for the task. (Monitoring upcoming academic deadlines)
    - ***Show*** the top three tasks that need to be worked on today, ***ranked*** with the most important task to be done first.
    - Each task would also ***display*** a calculated suggested number of hours to work on the task for the day.
- Clarifications (user story extensions):
  - Users can update the task completion progress, the total number of hours to complete the task, and the deadline.
  - Tasks overdue, i.e., not completed by the deadline, would have an Overdue flag.
    - The overdue tasks would always be displayed first.
    - These overdue tasks are not part of the top three tasks.
    - The overdue tasks would always be displayed, and then the top three tasks would be displayed below the overdue tasks.
  - The user would be able to input test dates into the system. i.e., it's not a task.
    - This is a NICE-TO-HAVE.
    - The selection and sort would prefer no consecutive work for projects and tests of the same course.

### MVP Epic components decomposition
#### Create tasks
- Task title
- Task description
- The deadline of a task
- Estimated number of hours for completing the task - default to ten hours if unspecified
- Difficulty level - default to Average
- Course code (nice-to-have feature)
#### Create tests - ***(NICE-TO-HAVE FEATURE)***
- Test name
- Course code of the test
- Date of the test
- Description of the test
  - Location
  - Specific time
#### Update tasks
- Update the title of a task
- Update the description of a task
- Update the deadline of a task
- Update the percentage completion progress of a task
  - Mark a task as completed
- Update the estimated number of hours for completing a task
- Update the difficulty level of a task
- Update the course code of a task
#### Delete tasks
#### Monitoring View
- Default method to view tasks
  - Sort by due date.
- Additional selection and sorting algorithms
  - Top three tasks selected and sorted by hours per day.
    - The total number of days to work on the assignment would be the days from today to the due date, not including today and not including the due date.
    - The hours per day would be the estimated total number of hours multiplied by the percentage ***REMAINING*** progress of the task, divided by the total number of days as defined above.
    - The task due the next day would have an infinitely large number of hours per day, i.e., the infinity value would be greater than all other non-infinity values.
  - Top three tasks selected and sorted by difficulty.  - ***(FEED BACK FROM JONATHAN - NICE-TO-HAVE FEATURE)***
  - Top three tasks with customized sorting, i.e., choose the algorithm to select the top three tasks, and choose the algorithm to sort them. - ***(FEED BACK FROM JONATHAN - NICE-TO-HAVE FEATURE)***
    - Selecting the top three tasks and sorting tasks would use due date, difficulty level, and hours per day.
- Get comments from AI.  - ***(FEED BACK FROM JONATHAN - NICE-TO-HAVE FEATURE)***
  - Send AI the selected tasks along with their sorting order and get a comment from AI.
  - The comment from AI will be displayed in the Monitoring View alongside the selected and sorted tasks for the day.
  - To get a comment from AI, a Markdown prompt template is used. The system simply needs to add the list of tasks to the Markdown prompt, run the query, and get the prompt result.
- Decrease the sorting rank by one.  - ***(NICE-TO-HAVE FEATURE)***
  - If there is a test for a course, and on the day of the test and the next day of the test, a task from the same course is selected in the Monitoring View, decrease the ranking.
  - If the task is already ranked third, then the rank would not be decreased.

---

### Notes and clarifications
#### Selection and sorting
- Selecting and sorting mean different things here.
- Selection is about selecting the top three items to be displayed.
- Sorting is about the order of the items to be displayed.
- The top three tasks are selected first and then sorted.
- When selecting or sorting by due date, the task with the closest due date is selected or sorted first.
- When selecting or sorting by difficulty, the hardest task is selected or sorted first.
- When selecting or sorting by hours per day, the task with the largest number of hours is selected or sorted first.
- Whenever there is a tie, sort by due date next. If there is still a tie, sort by task name.
- The rule of "Decrease the sorting ranking by one", if applied, is applied at the very end, after the above rules are applied.
#### For both display methods:
- The overdue tasks would ***ALWAYS*** be displayed at the top, in addition to the selected tasks.
- If the task displayed is due on a public holiday, clearly display this information.
#### Overdue tasks definition
- All tasks are assumed to be overdue at 0:00 (midnight at the ***START*** of the day.
- In practice, this means that all tasks due today are considered overdue.
#### Monitoring View
- User stories 3 and 4 both use the same monitoring view.
- The user can choose which method to sort the tasks in the view. 
- The different methods are different modes to display the tasks, not parallel displays of the same tasks. 
#### Task input field requirements
- The estimated number of hours required to complete the task must be a positive number.
- There are exactly three levels of difficulty: "Easy", "Average", and "Difficult".
#### MISC
- We need the rough-ins for nice-to-have features.
- The nice-to-have features are highly likely to be implemented. But the MVP features are selected to ensure a high project success rate.
- We can add more enhancement features later on.
- Use the Markdown file in the project repository as the single source of truth.  Treat the Google Docs as old archived information.
- User authentication features (sign-up, login, and log-off) are required for the MVP. 
- Public holiday warnings would be based on the selected country and not at a specific province/state level. 
- Each authenticated user should have a private task list, i.e., all accounts would ***NOT*** share the same tasks. 

---

## User Story 1 — Create Academic Tasks (Led by Eva Verkhovsky)

### User Story

As a student, I want to create academic tasks with deadlines and workload estimates so that I can organize my coursework and plan my time effectively.

### Acceptance Criteria

1. The user can create a task by providing a title, description, and deadline.
2. The user can specify the estimated number of hours required to complete the task, defaulting to 10 hours if unspecified.
3. The user can specify the task's difficulty, defaulting to Average if unspecified.
4. The system validates task information before saving and prevents invalid task entries.
5. Each task is assigned a unique identifier and an initial completion progress of 0%.
6. Tasks are saved locally using JSON or SQLite and remain available after the application is restarted.
7. When a Monitoring View is opened/reopened, it is able to retrieve the latest task information, including newly created tasks.

---

## User Story 2 - Manage Tasks and Completion Progress (Led by Xia Rao) 

### User Story

As a student, I want to be able to edit existing tasks, update their completion progress, and delete tasks I no longer need so that my task information accurately reflects my current academic workload.

### Acceptance Criteria

1. The user can select an existing task and update its title and description.
2. The user can update the task's deadline, estimated total hours, and difficulty.
3. The user can update the task's completion percentage from 0% to 100% (this should be an integer value). The system rejects values outside this range.
4. The user can mark a task as completed, setting its completion percentage to 100%.
5. The system validates edited task information before saving. Invalid changes display an error message and leave the saved task unchanged.
6. The user can delete an existing task after confirmation. Canceling the deletion leaves the task unchanged.
7. All successful updates and deletions are saved locally and remain reflected after the application is restarted.
8. When a Monitoring View is opened/reopened, it is able to retrieve the latest task information and excludes deleted tasks.

---

## User Story 3 - View Upcoming Deadlines and Holiday Warnings (Led by Lufei Zhang)

### User Story

As a student, I want to view my tasks in deadline order and receive warnings when tasks are overdue or due on a public holiday so that I can identify the deadlines and plan my work accordingly.

### Acceptance Criteria
1. The system can display all saved tasks in the default Monitoring View.
2. The system can sort tasks by due date, with the closest deadline listed first.
3. The system can identify overdue tasks that remain incomplete.
4. The system can always display overdue tasks at the top.
5. The system can apply the task-name tie-breaker when deadlines are equal.
6. The system can integrate the Nager.Date Public Holiday API to detect deadlines falling on public holidays.
7. Externally, the system should display overdue warnings and any applicable public-holiday warnings to the tasks listed in the Monitoring View.
8. Whenever the Monitoring View is opened/reopened, it should use the latest state to select and sort the tasks, and display them. 
9. Tasks that are 100% complete should never be included in the tasks listed in the Monitoring View.
10. The system can expose holiday-warning functionality for reuse in User Story 4.

---

## User Story 4 - Monitoring View and Prioritize Today's Tasks and Calculate Suggested Daily Workload (Led by Anjie Zhou)

As a student, I want to be able to have a suggested prioritization of today's tasks using the suggested daily workload, so that I can plan what to study based on the information.

### Acceptance Criteria

2. The system can calculate remaining workload per task.
3. The system can calculate the days available to work, excluding today and the due date.
4. The system can calculate suggested daily workload per task in hours per day
5. When a task is due tomorrow:
  - Internally, the system would assign it a suggested daily workload of positive infinity per day to be used for the ranking calculation.
6. The system can select the top three tasks first, then sort those selected tasks by hours per day, descending.
  - The user would be able to choose this selection and sorting algorithm in the Monitoring View and display the tasks selected and sorted using this algorithm.
  - Externally, the system would display the suggested daily workload per day.
  - For the task due tomorrow, instead of displaying the suggested daily workload, it would display the text "URGENT".
7. The system would break ranking ties using the deadlines, i.e., whichever has the earlier deadline would be ranked higher, followed by task name.
8. The system would always select every overdue task above the three selected tasks, without counting overdue tasks toward the limit of three.
  - The overdue tasks would be handled and displayed by User Story 3. This criterion is to clarify that the selection and sorting of the tasks should ***NOT*** include these overdue tasks.
9. The system would display suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings.
10. Whenever the Monitoring View is opened/reopened, it should use the latest state to select and sort the tasks, and display them. 
11. Tasks that are 100% complete should never be included in the tasks listed in the Monitoring View.
12. Have an architecture design that can allow future developers to add new selection and sorting algorithms easily.

---

## Use Cases

### Story 1

#### Sub Case 1A Name

- Create a new academic task.

##### Primary actor

- The user creating an academic task.

##### Preconditions

- The application is running, and the window for both task creation and task editing is open.

##### Trigger

- The user chooses to create a new academic task.

##### Success outcome

- A new academic task is successfully created, saved locally, restored when the application restarts, and reflected when the Monitoring View is opened or reopened.

##### Main success scenario

1. The user requests to create a new academic task.
2. The system displays the task creation form.
3. The user enters the task title, description, and deadline.
4. The user optionally enters the estimated number of hours required to complete the task. If unspecified, the system defaults to 10 hours.
5. The user optionally selects a difficulty level. If unspecified, the system defaults to Average.
  - There would only be these three exact choices for the user to select for the difficulty level. i.e., the user cannot just type in a string. 
6. The user submits the task information.
7. The system validates the entered information.
8. The system assigns the task a unique identifier and initializes its completion progress to 0%.
9. The system saves the new task locally using JSON or SQLite.
10. The system confirms that the task has been successfully created.

##### Extensions
- If the estimated number of hours is not a positive number, or the completion rate percentage is not a number between 0 and 100 percent, inclusive, the system displays an error message and requests a valid value.
- If the task deadline is invalid or missing, the system displays an error message and prevents the task from being saved.
- If the user cancels task creation, the system does not save the task.
  - The system pops up a window to let the user confirm the cancellation.
  - The system discards the unsaved task to be created, and the use case ends.

#### Sub Case 1B Name

- Allow the user to log in to the app

##### Primary actor

- The user logging in. 

##### Trigger

- The user chooses to log in on the welcome screen instead of signing in.

##### Success outcome

- The user logs in successfully. 

##### Main success scenario

1. The user requests to log in to the app. 
2. The system displays the login screen to the user.
3. The user enters the username and password and clicks on the login button.
4. The system checks the information entered by the user against the list of users stored in the system.
5. The system confirms that the user information found matches what is in the system.
6. The user is logged into the system. 
7. The system sends a confirmation message that the user is now logged in.

##### Extensions
- If the username or password doesn't match, the system displays an error message. 
- If the user cancels login, the system does not save the login information.
  - The system pops up a window to let the user confirm the cancellation.
  - The system discards the unsaved login information, and the use case ends.

### Story 2

#### Sub Case 2A Name
- Edit Task information and completion progress

#### Primary actor
- The user editing the tasks

#### Preconditions
- The application is running.
- There is at least one task in the system.

#### Trigger
- The user chooses to edit an existing task.

#### Success outcome
- The valid changes are saved locally under the same task identifier, are restored when the application restarts, and are reflected when the Monitoring View is opened or reopened.

#### Main success scenario
- The user selects an existing task to edit.
- The system displays its current title, description, deadline, estimated total hours, completion progress, and difficulty. 
- The student modifies one or more fields.
- The student asks to save the changes. 
- The system validates the edited information. 
  - There would only be these three exact choices for the user to select for the difficulty level. i.e., the user cannot just type in a string. 
- The system saves the updated task locally, preserving its identifier and other unchanged fields. 
- The system displays the updated information and a confirmation message that it was saved.

#### Extensions
- The entered information is invalid.
  - The number entered for completion progress should be between 0 and 100 percent, inclusive, and the number of hours should be positive. If not, the system identifies the invalid fields and leaves the saved task unchanged.
  - The student returns to the editing page to correct the information or cancels editing.
- The student cancels the editing.
  - The system pops up a window to let the user confirm the cancellation.
  - The system discards the unsaved changes.
  - The saved task remains unchanged, and the use case ends.

#### Sub Case 2B Name
- Deletes the task

#### Primary actor
- The user asks to delete an existing task.

#### Preconditions
- The application is running.
- There is at least one task in the system.

#### Trigger
- The user chooses to edit an existing task.

#### Success outcome
- The task is deleted from local storage, remains deleted after restarting the application, and is excluded from subsequent Monitoring View results.

#### Main success scenario
- The user selects an existing task and asks to delete it.
- The system identifies the selected task and asks for confirmation
- The student confirms the deletion.
- The system deletes the task from local storage.
- The system removes the task from the displayed task list, from the persistent data storage, and confirms the deletion.

#### Extensions
- The student cancels the deletion
  - The system closes the confirmation prompt.
  - The task remains unchanged, and the use case ends.

#### Sub Case 2C Name

- Allow the user to sign up a new user for the app

##### Primary actor

- The user signing up. 

##### Trigger

- The user chooses to sign up on the welcome screen instead of logging in.

##### Success outcome

- The user signs up successfully. 

##### Main success scenario

1. The user requests to sign up as a new user of the app. 
2. The system displays the sign-up screen to the user.
3. The user enters the username and password and clicks on the sign-up button.
4. The system checks the information entered by the user against the list of users stored in the system.
5. The system confirms the user is not already in the system.
6. The user information is added to the system. 
7. The system sends a confirmation message that the user is now signed up.
8. The system displays the login screen to the user. 

##### Extensions
- If the username already exists, the system displays an error message. 
- If the user cancels sign-up, the system does not save the sign-up information.
  - The system pops up a window to let the user confirm the cancellation.
  - The system discards the unsaved sign-up information, and the use case ends.

### Story 3

#### Name
- View upcoming deadlines and public holiday warnings.

#### Primary actor
- The user viewing the tasks.

#### Preconditions
- The Monitoring View window is activated.

#### Trigger
- The user opens the Monitoring View window, or the Monitoring View window is already open and the user requests to select and sort tasks by due date.

#### Success outcome
- The tasks are displayed by due date, with incomplete overdue tasks shown first and public holiday warnings displayed.

#### Main success scenario
1. The user opens the Monitoring View window, or the Monitoring View window is already open and the user requests to select and sort tasks by due date.
2. The system retrieves all the tasks stored inside the system.
3. The system identifies all incomplete tasks whose deadlines have passed, including tasks due today, as overdue and displays them at the top separately.
4. The system sorts the remaining incomplete tasks by deadline, with the closest deadline listed first.
5. The system retrieves public holiday information from the Nager.Date Public Holiday API and checks whether the deadline of each task falls on a public holiday.
    - The initial result will be cached, and subsequent runs will not repeat requests to the API. 
6. The system displays an overdue warning beside each overdue task and a public holiday warning beside each task whose deadline falls on a public holiday.
7. The system makes the holiday-warning information available for reuse by Story 4 through an abstract interface.

#### Extensions
- NONE

### Story 4

#### Sub Case 4A Name
- Turn on Monitoring View

##### Primary actor
- The user viewing the tasks.

##### Preconditions
- The application is running. 

##### Trigger
- The user requests to activate the Monitoring View. 

##### Success outcome
- The Monitoring View window is activated and allows the user to trigger the Story 3 use case, including requests for different ways to select and sort the tasks. 

##### Main success scenario
1. The user requests to activate the Monitoring View.
2. The user is prompted to select the country code setting. It will impact the holiday warning.
  - The country would default to Canada.
  - The ISO 3166-1 alpha-2 country code would be used here.
3. The Monitoring View window is displayed and supports the functionality of the Story 3 use case through an abstract interface. 

#### Sub Case 4B Name
- Prioritizing today's tasks using the suggested daily workload.

##### Primary actor
- The user viewing the tasks.

##### Preconditions
- The application is running, and the Monitoring View is open. 

##### Trigger
- The user requests to prioritize today's tasks using the suggested daily workload.

##### Success outcome
- The selected and sorted tasks are displayed in the Monitoring View according to the suggested daily workload.

##### Main success scenario
1. The user requests to prioritize the tasks in the Monitoring View using the suggested daily workload algorithm.
2. The system retrieves all the tasks stored inside the system.
3. The system finds all the tasks ***NOT*** overdue ***AND*** that have ***NOT*** been completed, as the overdue tasks would always be displayed at the top separately.
4. The system sorts the tasks using the suggested daily workload, with the task requiring the greatest workload listed first.
5. The system selects the top three tasks (or up to three tasks, if there are fewer than three tasks), and they are already sorted in the right order to be displayed.
6. These selected and sorted tasks are displayed in the Monitoring View according to the sorted order.

#### Sub Case 4C Name

- Allow the user to log off the app

##### Primary actor

- The user logging off. 

##### Trigger

- The user chooses to log off on the main app screen.

##### Success outcome

- The user logs off successfully. 

##### Main success scenario

1. The user requests to log off the app. 
2. The system displays a confirmation window for the user to confirm.
3. The user confirms the action.
3. The user logs off the system. 
4. The system displays the welcome screen to the user.
5. The system sends a confirmation message that the user is now logged off.

##### Extensions
- If the user cancels log off, the system does not log the user off.
  - The system pops up a window to let the user confirm the cancellation.
  - The user remains logged in, and the use case ends.

---

## Entities
- See the file in `Entities.puml`. 

---

## API Usage

**API Name:** 
Nager.Holidays

**Documentation:** 
- A high-level overview and quick-start guide [https://nagerholidays.com/api]
- Detailed technical documentation [https://nagerholidays.com/scalar/#community-api-v4/description/introduction]

**How it is used in the program:**
- The program uses the Nager.Date Public Holiday API to retrieve public holiday information. 
- It checks whether academic task deadlines fall on public holidays and displays holiday warnings beside affected tasks in the Monitoring View. 
- The API results are cached to avoid repeated requests.
