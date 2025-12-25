# Coursework Submission and Grading Platform Team Project

## Project Summary:
A desktop coursework submission and feedback application that allows users to upload and download assignments, as well as add, remove, and edit grades and feedback for individual or group assignments.

## User Stories:
1.	As a student, I want to submit my coding assignment files to the platform so that the instructor can mark them.
2.	As a student, I want to resubmit my work so instructors can mark the latest one.
3.	As a student, I want to submit a regrade request if I think there have been mistakes in the marking. 
4.	As a student, I want to check the class average and distribution of each assignment so that I know how good I am compared to peers.
5.	As an instructor, I want to create assignments for students to submit and add instructions so students can submit assignments to specifications.
6.	As an instructor, I want to set deadlines for each assignment so I can have an assigned time period to mark them.
7.	As an instructor, I want to release the marks of assignments to all students at once so that they can view and compare their marks.
8.  As a user, I want to register for an account and select whether I am a student or an instructor to gain full access to the platform.

## MVPs
| Lead              | Use Case            | User Story    |
| :---------------- | :-----------------  | :------------ |
| Aiden             | Submit Assignment   | User Story #1 |
| Gracie            | Create Assignment   | User Story #2 |
| Wuqingyi (Cherie) | Show Grade Average  | User Story #3 |
| Jiazheng (Indy)   | Mark Assignment     | User Story #4 |
| Xihang	          | Resubmit Assignment | User Story #5 |
| Turkan Yagmur     | Register            | User Story #6 |

## APIs:

**[CSC207 Grade Api](https://www.postman.com/cloudy-astronaut-813156/csc207-grade-apis-demo/documentation/fg3zkjm/5-password-protected-user?entity=request-15617509-f69997c5-cebf-4917-b478-5bbea67bde4b)** \
Used as persistent data storage. Will hold our entities (users, students, classes, assignments) and allow users to get the most up-to-date information.

**[JFreeChart](https://www.jfree.org/jfreechart/)** \
Creates charts and graphs such as bar charts, pie charts, and histograms for data visualization in Java applications. Will allow us to generate grade distribution charts to show users.

## Project Screenshots / Animations

Nothing to show yet... :\

## Todos: (roughly in order)
1. Create Main Views
    - Login / Register
    - "Home" / Logged In (Student)
    - "Home" / Logged In (Instructor)
    - Assignment (Student)
    - Assignment (Instructor)
2. Create Entities
3. Flesh out Use Cases
4. Implement API
