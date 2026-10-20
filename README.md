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
- Monitor the tasks (Monitor view)
    - Top three tasks selected and sorted by hours per day.
        - The total number of days to work on the assignment would be the days from today to the due date, not including today and not including the due date.
        - The hours per day would be the estimated total number of hours multiplied by the percentage ***REMAINING*** progress of the task, divided by the total number of days as defined above. 
        - The task due today or the next day would have an infinitely large number of hours per day, i.e., the infinity value would be greater than all other non-infinity values. 
    - Top three tasks selected and sorted by difficulty.  - ***(NICE-TO-HAVE FEATURE)***
    - Top three tasks with smart sorting, i.e., select the top three tasks by hours per day, then sort them by difficulty.  - ***(NICE-TO-HAVE FEATURE)***
    - Decrease the sorting ranking by one.  - ***(NICE-TO-HAVE FEATURE)***
        - If there is a test for a course, and on the day of the test and the next day of the test, a task from the same course is selected in the monitoring view, decrease the ranking. 
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
#### MISC
- We need the rough-ins for nice-to-have features. 
- The nice-to-have features are highly likely to be implemented. But the MVP features are selected to ensure a high project success rate. 
- We can add more enhancement features later on. 
- Use the Markdown file on the project repository as the single source of truth.  Treat the Google Docs as old archived information.  

## Grouping of non-nice-to-have epic components
### Acceptance Criteria 1 — Create Academic Tasks
- Create tasks with a title, description, and deadline.
- Specify an estimated number of hours, defaulting to 10 hours when unspecified.
- Specify difficulty, defaulting to Average when unspecified.
- Validate task input before saving.
- Assign each task a unique identifier and an initial completion progress of 0%.
- Persist task data locally using JSON or SQLite so that tasks survive application restarts.

### Acceptance Criteria 2 — Manage Tasks and Completion Progress
- Update a task's title and description.
- Update its deadline, estimated hours, and difficulty.
- Update its completion percentage from 0% to 100%.
- Mark a task as completed.
- Delete an existing task, preferably with a confirmation step.
- Persist all modifications and deletions.
- Ensure monitoring views reflect updated information when "reopened" or "refreshed".

### Acceptance Criteria 3 — View Upcoming Deadlines and Holiday Warnings
- Display saved tasks in the default monitoring view.
- Sort tasks by due date, with the closest deadline first.
- Identify overdue tasks that remain incomplete.
- Always display overdue tasks at the top.
- Apply the task-name tie-breaker when deadlines are equal.
- Integrate the Nager.Date Public Holiday API to detect deadlines falling on public holidays.
- Externally, display suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings.
- Keep updating and update the displayed information when the view is "opened" or "refreshed".
- Expose holiday-warning functionality for reuse in User Story 4.

### Acceptance Criteria 4 — Prioritize Today's Tasks and Calculate Suggested Daily Workload
- Select the top three incomplete, non-overdue tasks using hours per day.
- Calculate remaining workload.
- Calculate the days available to work, excluding today and the due date. 
- Calculate suggested daily workload per task in hours per day
- When a task is due today or tomorrow: 
   - Internally, assign it a suggested daily workload of positive infinity per day to be used for the ranking calculation.
   - Externally, display the suggested daily workload by displaying the remaining workload.
- Select the top three tasks first, then sort those selected tasks by suggested daily workload per task in hours per day, descending.
- Break ties using the earlier deadline, followed by task name.
- Select every overdue task above the three selected tasks, without counting overdue tasks toward the limit of three.
- Internally, provide suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings.
- Keep recalculating and recalculate prioritization from current task data whenever the view is "opened" or "refreshed".

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
8. When a monitoring view is reopened or refreshed, it uses the latest task information and excludes deleted tasks.

## User Story 3 - View Upcoming Deadlines and Holiday Warnings

### User Story

As a student, I want to view my tasks in deadline order and receive warnings when tasks are overdue or due on a public holiday so that I can identify the deadlines and plan my work accordingly.

### Acceptance Criteria
1. The system can display all saved tasks in the default monitoring view.
2. The system can sort tasks by due date, with the closest deadline first.
3. The system can identify overdue tasks that remain incomplete.
4. The system can always display overdue tasks at the top. 
5. The system can apply the task-name tie-breaker when deadlines are equal.
6. The system can integrate the Nager.Date Public Holiday API to detect deadlines falling on public holidays. 
7. Externally, the system can display suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings. 
8. The system can keep updating and update the displayed information when the view is "opened" or "refreshed". 
9. The system can expose holiday-warning functionality for reuse in User Story 4.

## User Story 4 - Prioritize Today's Tasks and Calculate Suggested Daily Workload

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
7. The system would break ranking ties using the deadlines, i.e., whichever has the earlier deadline would be ranked higher, followed by task name.
8. The system would always select every overdue task above the three selected tasks, without counting overdue tasks toward the limit of three.
9. Internally, the system would provide suggested daily workload per task in hours per day, overdue warnings, and any applicable public-holiday warnings.
10. The system would keep recalculating and recalculate prioritization from current task data whenever the view is "opened" or "refreshed".
