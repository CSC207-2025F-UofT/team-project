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

# Program Specification
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
  - Tasks overdue, i.e., not completed by the deadline, would have a flag of Overdue.
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
- Difficulty level - default to average
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
- The "monitor tasks" display would be refreshed whenever a task is displayed.
#### Delete tasks
#### Monitor tasks
- Refresh the selection and sorting of the tasks according to the latest information.
- Default method to view tasks
  - Sort by due date.
- Monitor the tasks (Monitoring View)
  - Top three tasks selected and sorted by hours per day.
    - The total number of days to work on the assignment would be the days from today to the due date, not including today and not including the due date.
    - The hours per day would be the estimated total number of hours multiplied by the percentage ***REMAINING*** progress of the task, divided by the total number of days as defined above.
    - The task due today or the next day would have an infinitely large number of hours per day, i.e., the infinity value would be greater than all other non-infinity values.
  - Top three tasks selected and sorted by difficulty.  - ***(FEED BACK FROM JONATHAN - NICE-TO-HAVE FEATURE)***
  - Top three tasks with customized sorting, i.e., choose the algorithm to select the top three tasks, and choose the algorithm to sort them. - ***(FEED BACK FROM JONATHAN - NICE-TO-HAVE FEATURE)***
    - Selecting the top three tasks, and sorting tasks would use due date, difficulty level, and hours per day.
  - Get comments from AI.  - ***(FEED BACK FROM JONATHAN - NICE-TO-HAVE FEATURE)***
    - Send AI the selected tasks along with their sorting order and get a comment from AI.
    - The comment from AI will be displayed in the Monitoring View alongside the selected and sorted tasks for the day.
    - To get a comment from AI, a Markdown prompt template is used. The system simply needs to add the list of the tasks to the Markdown prompt and run the query, and get the prompt result.
  - Decrease the sorting ranking by one.  - ***(NICE-TO-HAVE FEATURE)***
    - If there is a test for a course, and on the day of the test and the next day of the test, a task from the same course is selected in the Monitoring View, decrease the ranking.
    - If the task is already ranked third, then the rank would not be decreased.

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
- All tasks are assume to be overdue at 0:00 at midnight at the ***START*** of the day.
- In practice, this means that all tasks due today are considered overdue.
#### Monitoring View
- User stories 3 and 4 both use the same monitoring view.
- The user can choose which method to sort the tasks in the view. 
- The different methods are different modes to display the tasks, not parallel displays of the same tasks. 
#### Nager.Date Calendar
- The calendar assumes the user lives in Toronto, Ontario, Canada.
#### Task input field requirements
- The estimated number of hours required to complete the task must be a positive number.
- There are exactly three levels of difficulty: "Easy", "Average", and "Difficult".
#### MISC
- We need the rough-ins for nice-to-have features.
- The nice-to-have features are highly likely to be implemented. But the MVP features are selected to ensure a high project success rate.
- We can add more enhancement features later on.
- Use the Markdown file on the project repository as the single source of truth.  Treat the Google Docs as old archived information.

## User Story 1 — Create Academic Tasks

### User Story

As a student, I want to create academic tasks with deadlines and workload estimates so that I can organize my coursework and plan my time effectively.

### Acceptance Criteria

1. The user can create a task by providing a title, description, and deadline.
2. The user can specify the estimated number of hours required to complete the task, defaulting to 10 hours if unspecified.
3. The user can specify the task's difficulty, defaulting to Average if unspecified.
4. The system validates task information before saving and prevents invalid task entries.
5. Each task is assigned a unique identifier and an initial completion progress of 0%.
6. Tasks are saved locally using JSON or SQLite and remain available after the application is restarted.

## User Story 2 - Manage Tasks and Completion Progress

### User Story

As a student, I want to be able to edit existing tasks, update their completion progress, and delete tasks I no longer need so that my task information accurately reflects my current academic workload.

### Acceptance Criteria

1. The user can select an existing task and update its title and description.
2. The user can update the task's deadline, estimated total hours, and difficulty.
3. The user can update the task's completion percentage from 0% to 100% (this should be an integer value). The system rejects values outside this range.
4. The user can mark a task as completed, setting its completion percentage to 100%.
5. The system validates edited task information before saving. Invalid changes display an error message and leave the saved task unchanged.
6. The user can delete an existing task after confirmation. Cancelling the deletion leaves the task unchanged.
7. All successful updates and deletions are saved locally and remain reflected after the application is restarted.
8. When a Monitoring View is reopened or refreshed, it uses the latest task information and excludes deleted tasks.

## User Story 3 - View Upcoming Deadlines and Holiday Warnings

### User Story

As a student, I want to view my tasks in deadline order and receive warnings when tasks are overdue or due on a public holiday so that I can identify the deadlines and plan my work accordingly.

### Acceptance Criteria
1. The system can display all saved tasks in the default Monitoring View.
2. The system can sort tasks by due date, with the closest deadline first.
3. The system can identify overdue tasks that remain incomplete.
4. The system can always display overdue tasks at the top.
5. The system can apply the task-name tie-breaker when deadlines are equal.
6. The system can integrate the Nager.Date Public Holiday API to detect deadlines falling on public holidays.
7. Externally, the system can display suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings.
8. The system can keep updating and update the displayed information when the view is "opened" or "refreshed".
9. The system can expose holiday-warning functionality for reuse in User Story 4.

## User Story 4 - Prioritize Today's Tasks and Calculate Suggested Daily Workload (Lead by Anjie Zhou)

As a student, I want to be able to have a suggested prioritization of today's tasks and also have a suggested daily workload, so that I can plan what to study based on the information.

### Acceptance Criteria

1. The system can select the top three incomplete, non-overdue tasks using hours per day.
2. The system can calculate remaining workload per task.
3. The system can calculate the days available to work, excluding today and the due date.
4. The system can calculate suggested daily workload per task in hours per day
5. When a task is due today or tomorrow:
  - Internally, the system would assign it a suggested daily workload of positive infinity per day to be used for the ranking calculation.
  - Externally, the system would display the suggested daily workload by displaying the remaining workload.
6. The system can select the top three tasks first, then sort those selected tasks by hours per day, descending.
  - The user would be able to choose this selection and sorting algorithm in the Monitoring View and display the tasks selected and sorted using this algorithm.
7. The system would break ranking ties using the deadlines, i.e., whichever has the earlier deadline would be ranked higher, followed by task name.
8. The system would always select every overdue task above the three selected tasks, without counting overdue tasks toward the limit of three.
  - The overdue tasks would be handled and displayed by User Story 3. This criterion is to clarify that the selection and sorting of the tasks should ***NOT*** include these overdue tasks.
9. Internally, the system would provide suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings.
10. The system would keep recalculating and recalculate prioritization from current task data whenever the view is "opened" or "refreshed".
11. Have an architecture design that can allow future developers to add new selection and sorting algorithms easily.

## Use Cases
### Story 1

#### Name

- Create a new academic task.

#### Primary actor

- The user creating an academic task.

#### Preconditions

- The application is open, and the user has access to the task creation interface.

#### Trigger

- The user chooses to create a new academic task.

#### Success outcome

- A new academic task is successfully created, saved locally, and made available in the Monitoring View.

#### Main success scenario

1. The user requests to create a new academic task.
2. The system displays the task creation form.
3. The user enters the task title, description, and deadline.
4. The user optionally enters the estimated number of hours required to complete the task. If unspecified, the system defaults to 10 hours.
5. The user optionally selects a difficulty level. If unspecified, the system defaults to Average.
6. The user submits the task information.
7. The system validates the entered information.
8. The system assigns the task a unique identifier and initializes its completion progress to 0%.
9. The system saves the new task locally using JSON or SQLite.
10. The system confirms that the task has been successfully created.

#### Extensions

- If the task title, description, or deadline is invalid or missing, the system displays an error message and prevents the task from being saved.
- If the estimated number of hours is not a positive number, or the completion rate percentage is not a positive number between 0 and 100, the system displays an error message and requests a valid value.
- If the difficulty level is not Easy, Average, or Difficult, the system rejects the invalid value.
- If the user cancels task creation, the system does not save the task.

### Story 2

### Story 3

### Story 4

#### Name
- Prioritize today's tasks using the suggested daily workload.

#### Primary actor
- The user viewing the tasks.

#### Preconditions
- The Monitoring View window is activated.

#### Trigger
- The user requested to prioritize the tasks in the Monitoring View using the suggested daily workload algorithm.

#### Success outcome
- The tasks are selected and ordered again according to this algorithm.

#### Main success scenario
1. The user requests to prioritize the tasks in the Monitoring View using the suggested daily workload algorithm.
2. The system retrieves all the tasks stored inside the system.
3. The system finds all the tasks ***NOT*** overdue ***AND*** that have ***NOT*** been completed, as the overdue tasks would always be displayed at the top separately.
4. The system sorts the tasks using the suggested daily workload, with the task requiring the greatest workload listed first.
5. The system selects the top three tasks (or up to three tasks, if there are fewer than three tasks), and they are already sorted in the right order to be displayed.
6. These selected and sorted tasks would be displayed in the Monitoring View according to the sorted order.

#### Extensions
- NONE
