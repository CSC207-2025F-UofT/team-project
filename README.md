# Team Project - QuercusPro

## Application Summary 
QuercusPro is a desktop application designed to help University of Toronto students manage their academic responsibilities through a centralized platform. It integrates three main features: a calendar for organizing classes, assignments, and personal commitments; a grade tracker for monitoring academic performance and calculating grades; and a campus map for locating university buildings and classrooms.
Students can manually enter and manage their information, with potential support for importing course information from syllabi and external services. The application aims to simplify academic organization and reduce reliance on multiple separate platforms.

## User Stories
1. As a student, I want to import tasks and their grade weights from course syllabi so that I don't have to manually enter every assignment.
2. As a student, I want to be able to mark assignments as completed so that I can distinguish between finished and unfinished work.
3. As a student, I want to be able to categorize calendar events differently, such as between marked and not-marked, academic or personal, so that I can see all my events in one place.
4. As a student, I want to be able to filter calendar events by course or category so that I can focus on relevant responsibilities and to avoid clutter.
5. As a student, I want to be able to see my running course grade and the current grade breakdown so I have an understanding of my current academic performance.
6. As a student, I want to enter hypothetical grades for remaining assessments so I have an understanding of how to achieve a target grade.

## API - Instructure Quercus API
"Journey is a separate Instructure service that lives alongside Canvas LMS. It hosts skills, experiences, programs, the learning library, and dashboards for users who are already authenticated through Canvas. Journey owns its own resources and reads users, accounts, courses, and enrollments from Canvas via the Canvas REST and GraphQL APIs" - From the [Instructure API page](https://developerdocs.instructure.com/services/journey/api)

## MVP 
- There is an interactable calendar view
- User can add a calendar event with a course, category / categories, and mark if applicable, which will be properly displayed in a calendar and table view
- User can delete existing calendar events
- User can toggle the visibility of calendar events based on course
- A grade can be displayed for each course based on the given marks

## Use Cases
#### Use case 1: Manage Calendar Events
Add, edit, delete, and categorize academic or personal events
#### Use case 2: Import Syllabus Deadlines
Extract assignment deadlines from uploaded course syllabi and add them to the calendar
#### Use case 3: Manage Calendar Tasks
Mark assignments as completed, filter events by course or category, and track upcoming deadlines
#### Use case 4: Calculate Course Grades
Calculate course grades based on given assessment grades and display it in an understandable manner.
#### Use case 5: Calculate Hypothetical Grades
Predict final averages and allow user to enter hypothetical grades and see the results

## GUI Sketches
#### Main dashboard
<img width="857" height="612" alt="image" src="https://github.com/user-attachments/assets/59535862-7329-48d6-8e1e-93b4467f3ba4" />

#### Calendar view
<img width="858" height="616" alt="image" src="https://github.com/user-attachments/assets/4d3c771a-1b12-4abd-bc5b-e45918bfaa05" />

#### Table view
<img width="860" height="607" alt="image" src="https://github.com/user-attachments/assets/f47c696e-ddfc-4a49-9fd1-edca92efd808" />

#### Grade view 
TBD

<details>
  <summary>Initial instructions from template</summary>
  
  Please keep this up-to-date with information about your project throughout the term.

  The readme should include information such as:
  - a summary of what your application is all about
  - a list of the user stories, along with who is responsible for each one
  - information about the API(s) that your project uses 
  - screenshots or animations demonstrating current functionality
  
  By keeping this README up-to-date,
  your team will find it easier to prepare for the final presentation
  at the end of the term.
  
</details>

