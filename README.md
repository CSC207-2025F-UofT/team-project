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


Page
1
of 2
# Project Name
## Team Information
### Team Members
- Kate Richardson 
- Sophia Iannuzzi 
- Thanika Haltrich 
- Zhaniya Mukhtarbek
- Purnima Chhetri
---
## Project Description
### Domain
In this project, we want to create cognitive tracking app that can be used by both everyday people and researchers alike. 
The everyday user will choose between an attention and memory cognitive test. They will then take that test and get their result for what their cognitive capacity is that day. 
They will then be able to go about their day. At the end of the day, they will log back in and say what their overall mood was for the day. 
They also have the option to se a graph of how their mood and cognitive capacity affects their productivity over time.
On the research side, they are able to access all users data. This data will be used to find general trends about how mood and cognitive capacity affect productivity.

---
## User Stories
### User Story 1
as a user, I want to be able to choose whether I will used this app as a researcher or as a regular user so that I can decide which features I would like to access.

### User Story 2
as a user, I want to be able to decide whether I am taking a memory or attention cognitive test so that I can gain be tested on the skills I am planning on using on any given day.

### User Story 3
as a user, I would like to be able to see what my immediate results are from my cognitive test of choice is so that I can structure my work accordingly

### User Story 4
as a user, I would like to be able to input my mood and productivity rating (along with the date/time that for each rating) so that they can be used later for any trend analysis

### User Story 5
as a user, I would like to be able to view a graph that shows my productivity as a function of my mood and my productivity as a function of how well I do on each cognitive test so that I can learn more about how my mood and mental sharpness affect how much I might get done on any given day.

### User Story 6
as a researcher, I would like to be able to access data from individuals to do analysis so that I can learn about how moods and cognitive capacity affect productivity among everyday people.

---
## Use Cases
### Use Case 1: Signup
1. the user inputs the username they want.
2. the user is given a prompt "I am a researcher". When ticked, the new account gets the role "researcher"
3. if the username doesn't exist, create an account with that username and assign a role to that account (user or researcher)
4. the system says "Hello new {Role}"

### Use Case 2: Login
1. the user enters a username
2. if the username already exists, the user gets logged in to the account witht at username
3. the system says "Hello {username}"

### Use Case 3: SaveData
1. user completes a test
2. the date, mood, productivity, test type,  test score gets saved in the database
3. if the input in incomplete, the system alerts the user
4. if saving is successful, the system outputs "Saving complete!"

### Use Case 4: GetData
1. the user chooses the dates they want to see their data
2. if there is no data on one (or more) dates the user selected, the sytem alerts the user
3. if all dates are valid, the data for those dates are shown.

### Use Case 5: Accessing data
Primary actor: Researcher sending a request
Preconditions:
There should be enough data from normal users
Researcher should be logged in
Trigger: button pressed
Success outcome: buttons for further analysis appear
Main success scenario:
The researcher presses the “access data” button
The program checks that data is not null
The program outputs buttons to access further analysis
Extensions:
The reason why the error occurred is outputted

### Use Case 5: Outputting memory, attention, mood to productivity statistical results
Primary actor: Researcher presses the button for the desired variable
Preconditions:
Researcher fetched the data
The statistical analysis for the selected variable is accessible
Trigger: the button for the variable is pressed
Success outcome: a graph with statistical data is outputted
Main success scenario:
The researcher presses the button for the required analysis
The correct data is processed
The statistical analysis and graph is outputted correctly for the required variable

### Use Case 7: UserAddsProductivity
1. User clicks on the "Add Mood" button on app
2. User is presented with a screen saying "What day do you wish to log your mood for -- please enter date as day/month/year (e.g. 02/01/2000 for January 2nd, 2000)"
3. User enters the date
4. If there is an error in the format of the date, they wll be asked to try again
5. Once they have successfully entered a valid date, the user will be presented with a question: "on a scale of 1 - 10, how productive did you feel on [date] where 10 is extremely productive, and 0 is not productive at all."
6. If the number they give falls outside the range or is not a valid number, catch that and ask again.
7. User is presented with a screen that says "you have successfully logged a [rating] productivity rating for [date]"

### Use Case 8: UserAddsMood
1. User clicks on the "Add Mood" button on app
2. User is presented with a screen saying "What day do you wish to log your mood for -- please enter date as day/month/year (e.g. 02/01/2000 for January 2nd, 2000)"
3. User enters the date
4. If there is an error in the format of the date, they wll be asked to try again
5. Once they have successfully entered a valid date, the user will be presented with a question: "please select the mood that best describes how you felt on [date] from this list: {happy, sad, angry, anxious, calm, tired, excited}"
5. If they enter a word that is not one of those then they will be asked to try again
6. Otherwise, they are presented with the screen "you have successfully logged the mood [their mood] for [date]"
7. They are returned to the home screen

### Use Case 9: See Graph
Primary Actor: User
Precondition: User exists and has a username
Trigger: User selects which type of graph to view (i.e. Productivity vs mood or scores)
Success Outcome: A graph appears in view that shows and average productivity against mood or scores
Failure Outcome: Text appears that says "no data for this user" (i.e. the user has no complete data for any date)
Success Scenario:
(in SeeGraphView)
1. User chooses moods or scores
2. system enacts GraphMakerUseCase (username is provided and is used to pull data from DB. CognitiveData Entities are created and used to make values)
3. System makes graph model
4. Graph is shown to user

### Use Case 10: Choose Between Memory and Attention Test
Primary Actor: User
Precondition: User exists and has logged in to their regular user type
Trigger: User selects the type of attention test they will take
Success Outcome: The selected test appears
Success Scenario:
1. User is presented with "Memory" button or "Attention" Button and a prompt that says "Please select the type of cognitive test you would like to complete."
2. User clicks one of the two buttons
3. System loads the selected type of test

### Use Case 11: User Does Attention Test
Primary Actor: User
Precondition: User is logged in and has chosen to take the attention test 
Trigger: User selects "Attention" as the type of test
Success Outcome: 


### Use Case 12: User Does Memory Test
Primary Actor: User
Precondition: User is logged in and has chosen to take the attention test
Trigger: User selects "Attention" as the type of test
Success Outcome:

---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |
---
## Entities
CognitiveData
Fields:
   date : Date
   mood : String
   cogTestType : String
   cogResults : float
   productivityRating : float
Methods:
   returnData : Dictionary
               {date : [mood, (cogTest, cogResutls), productivityRating]}

User
Fields:
   username : String
Methods:
   getUsername : String

---
## API Usage
**API Name:**
**Documentation:** [Link]
**How it is used in the program:**
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
