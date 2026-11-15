# GOAL FOR THIS WEEK:

- Finish this template!
- Decide everyone's User Stories

Stretch
- Finish UML Diagram

# Project Name
UofT Job Path Planner

## Team Information
Team Name: 62

### Team Members
- Zhirun Yang
- Jaylen Sze
- Aidan Zhang
- Marco Mao
- Yuheng Bao

---

## Project Description
Analyzing what kinds of jobs students can do based on the courses they are taking.

### Domain
Data Analysis


---

## User Stories
As a student, I want to upload my courses so that I can see what skills I obtained from them.(Julien Yuheng)

As a student, I want to see what skills I have so that I can see what jobs I can apply to. (Marco)

As a student, I want to see a list of jobs I can do with my skills, so that I can find jobs I’m qualified for. (Jaylen)

As a student, I want to divide the list of jobs into different groups, so I can keep track of jobs I like. (Aidan)

As a student, I want to filter the jobs list so that I can track my application status.(Jeffrey)

As a student, I want to know what courses I need to take to get a specific job, so that I can pick courses to prepare for future jobs.

---

## Use Cases

### Use Case 1
NAME: Upload Courses

PRIMARY ACTOR: Student

PRECONDITIONS: The student has opened the application.

TRIGGER: The student clicks the “Upload Courses” button.

SUCCESS OUTCOME: Their courses are saved and the skills obtained from the courses are identified and added to their profile.

MAIN SUCCESS SCENARIO:

- The student clicks the "Upload Courses" button.

- The system displays a form for entering courses.

- The student enters courses and clicks "Submit".

- The system checks if the course entries are valid. ← API call
- The system saves the courses to the


### Use Case 2a
NAME: Skill Analysis

PRIMARY ACTOR: User uploads courses.

PRECONDITIONS: Database receives a valid list of courses.

TRIGGER: The student clicks the “Upload Courses” button.

SUCCESS OUTCOME: A list of skills is derived from the given list of courses.

MAIN SUCCESS SCENARIO:

- The user uploads a list of courses.

- The system makes a call to a Timetable Builder API, to fetch a course description.

- The system saves the description under the courses.

- The system makes an API call to an LLM, to check what skills can be derived from the given course description.

- The system saves the list of skills under a course and independently, and caches them for faster searching.

### Use Case 3a
NAME: See a list of jobs that match

PRIMARY ACTOR: User looking for jobs

PRECONDITIONS:
- They uploaded their courses
- The courses were turned into skills by the system.

TRIGGER: User goes onto job list page

SUCCESS OUTCOME: User is presented with a list of jobs sorted by which skills they have

MAIN SUCCESS SCENARIO:

- User goes onto job list

- The system checks that the user uploaded the courses

- The system checks that the courses were turned into skills

- The system ranks the jobs by which skills they have that match the job

- The system presents the user with the job list

Extension (4a): No matching jobs found.

The system informs the student that no current jobs match their skill set.
The system shows an empty list

### Use Case 3b
NAME: See a job description

PRIMARY ACTOR: User looking for jobs

PRECONDITIONS:
- They uploaded their courses
- The courses were turned into skills by the system.
- The user is on the job list page

TRIGGER: User selects a specific job title

SUCCESS OUTCOME: User is presented with the job description, including what skills are needed and what skills the user has.

MAIN SUCCESS SCENARIO:
- User selects a specific job posting
- The system finds the job description
- The system presents the user with the description

### Use Case 3c
NAME: See a skill list

PRIMARY ACTOR: User trying to see skills

PRECONDITIONS:
- They uploaded their courses
- The courses were turned into skills by the system.
- The user is on the job list page

TRIGGER: User selects a specific job title

SUCCESS OUTCOME: User is presented with the job description, including what skills are needed and what skills the user has.

MAIN SUCCESS SCENARIO:
- User selects a specific job posting
- The system finds the job description
- The system presents the user with the description

### Use Case 4a
NAME: Create Job Group

PRIMARY ACTOR: User

PRECONDITIONS: User has uploaded their course(s) and has a list of jobs they are qualified for.

TRIGGER: User selects “create new job group” option

SUCCESS OUTCOME: User successfully creates a new group containing one or more jobs.

MAIN SUCCESS SCENARIO:

- User selects the “create new job group” option

- System prompts the user to enter a group name

- User enters a name for the group

- System displays the list of jobs available to the user

- User selects one or more jobs to add to the group

- User selects “make group” option

- System creates the group and saves the selected jobs to it

- System displays the newly created group with its jobs

### Use Case 4b

NAME: View job group

PRIMARY ACTOR:  User

PRECONDITIONS: User has uploaded their course(s) and has a list of jobs they are qualified for, and there exists a job group

TRIGGER: User selects option to view all their groups

SUCCESS OUTCOME: User can see what jobs are in selected group

MAIN SUCCESS SCENARIO:

- User selects option to view their groups

- System shows a list of all the groups user has

- User selects a group they want to view

- System gets the jobs in the group

- System displays them for user

### Use Case 4c
NAME: Add Job to Group

PRIMARY ACTOR:  User

PRECONDITIONS: User has uploaded their course(s) and has a list of jobs they are qualified for, and there exists a job group

TRIGGER: User selects a job and chooses the “add job to group” option

SUCCESS OUTCOME: User successfully adds the job to an existing group

MAIN SUCCESS SCENARIO:

- User selects a job

- User selects “add job to group” option

- System prompts user to pick a pre existing group (or multiple)

- User selects a group (or multiple)

- User selects “add to group” option

- System adds job to group

- System displays newly added job in group

### Use Case 4d

NAME: Remove group

PRIMARY ACTOR:  User

PRECONDITIONS: User has made at least one group

TRIGGER: User selects a group and chooses “remove group” option

SUCCESS OUTCOME: User successfully deletes the group

MAIN SUCCESS SCENARIO:

- User selects group

- User selects “delete group” option

- System prompts confirmation

- User clicks confirm

- System removes group and removes its association with jobs

- System updates display to not show removed group

### Use Case 5
NAME: User Filter Tracked Jobs.

PRIMARY ACTOR:  Jobs, Filter Options

PRECONDITIONS:  The list of Jobs  is not Empty

TRIGGER:  User filter jobs after selecting several filter options

SUCCESS OUTCOME: User get selected jobs returned by status

MAIN SUCCESS SCENARIO:

- User goes into the filter jobs page

- User select  provided option in the filter page

- User click the Filter button

- User get a list of jobs that pass the filter

- User get a summary of the application status for all jobs that are returned


### Use Case 6

NAME: Recommend a job

PRIMACY ACTOR: Courses

PRECONDITION: There are several courses already added in the database.

TRIGGER: The user clicks a “predict” button.

MAIN SUCCESS SCENARIO:

- The student opens the Career Recommendations page.

- The system displays the student’s course list.

- The student confirms or updates the courses to include.

- The system identifies the knowledge and skills covered by those courses and matches them to career requirements.

- The system displays relevant careers and explains how they relate to the selected courses.
- The student selects a career to view its responsibilities, related courses, relevant skills, and skills they may need to develop.

(Add additional use cases as needed.)

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Julien | Use Case 1 |
| Marco | Use Case 2 |
| Jaylen | Use Case 3(a-c) |
| Aiden | Use Case 4(a-c) |
| Jeffrey | Use Case 5 |

---

## Entities

### Entity 1
Student

### Entity 2
Job

(Add additional entities as needed.)

---

## API Usage

**Careerjet:**

**Documentation:** [https://www.careerjet.com/partners/api/]

**How it is used in the program:**
Return jobs that satisfy the user's skills and  description.


**Gemini API:**

**Documentation:** [https://aistudio.google.com/docs]

**How it is used in the program:**
Analyze course descriptions to determine relevant skills, and match skills to different job descriptions.

**Timetable Builder API:**

**Documentation:** [https://apis.io/providers/university-of-toronto/, https://api.easi.utoronto.ca/ttb/reference-data, https://api.easi.utoronto.ca/ttb/getCoursesByCodeAndSectionCode]

**How it is used in the program:**
To validate course title, and check description of courses.


## Design Diagrams

### UML Class Diagram


### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]

---

## Notes

[Any additional project information.]
