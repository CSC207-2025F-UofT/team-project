# Team Project

For all team members, ensure that you read Instructions.md before proceeding.

### Summary

A visual map that allows users to pin passages into real-world locations. Users can pin a short
excerpt from any opensource text they have seen to a real-world place. They can then share it with
the world, or their friends whom they may add.

### Tech Stack Requirements
* Java 17+
* Java Swing
* Git

### User Stories

1. I want to create an account where my pins are saved - Rohail (Logins)
2. I want to have an account where I can follow friends and see what they have saved - Rohail (Logins)
3. I want to be able to select passages from texts I have been reading - Taysen (Library)
4. I want to easily search for the book I want - Taysen (Library)
5. I want to be able to place the pin anywhere I wish - John (Maps)
6. I want to set the pin as public, private, or to friends only - John (Maps)
7. I want to be able to edit or delete my pins - John (Maps)
8. I want to click on any pin available to me (friends' pins, my own pins, public pins) and
read its passage, see the place, and read the users thoughts. - Katelyn (Profile)
9. I want to be able to attach an excerpt, my thoughts, and a photo of the location to any given pin - John & Katelyn (Profile and Maps)
10. I want to be able to edit my profile to add/remove friends and pins - Katelyn (Profile)

### User Cases
#### Add a new Friend
**Primary Actor:** The User adding a new User as a friend

**Preconditions:** The User must be logged into their account, there is no pending Friend request between the two users, the other user exists.

**Trigger:** The User sends the request via a button

**Success Outcome:** The request is sent by the User and received by the other User

**Main Success Scenario:** 
1. The User chooses to send a Friend request
2. The User successfully sends a signal into the system, such that the system updates the status between two Users
3. The other User receives the Friend request
   
**Extensions:**
1. The system can not record the Friend request

#### Remove a Frined
**Primary Actor:** The User removing another User

**Preconditions:** The User must be logged in, there must be an existing connection between the two Users

**Trigger:** The User chooses to delete another User from their friend list. 

**Success Outcome:** There is no longer a connection between the two 

**Main Success Scenario**:
1. The User chooses another User to remove a connection wth
2. The User successfully changes the status of their connection such that they can no longer view each others' private Pins.
3. The other User receives a notification that a connection is deleteed

**Extensions:**
1. There is no connection between the two Users
2. One (or both) of the Users can still see Friend-only information
3. The system fails to record the removal of the other User
4. The other User does not exist anymore

#### Add a new Pin
**Primary Actor:** The User adding a new Pin on their profile

**Preconditions:** The User must be logged into their account, there must not be a Pin with the same description and information. 

**Trigger:** The User clicks Create Pin. 

**Success Outcome:** A new Pin is saved onto the User's profile with all the inputted information correctly. 

**Main Success Scenario:** 
1. The User clicks Create Pin
2. The User inputs all the required fields to create a Pin.
3. The User saves the Pin to their profile
4. The system saves the Pin to correspond to the User and all its relevant information
5. The Pin is visible to others depending on the privacy settings the User has set for the Pin
   
**Extensions:**
1. Not all required fields were inputted to create the Pin.
2. A Pin was created despite incorrectly inputted fields.
3. The Pin was not saved to correspond to the (correct) User.
4. Not all the information was correctly saved to to the Pin.
5. The Pin is not visible on the map or on the User's profile to the correct subset of people.

#### Remove a Pin
**Primary Actor:** The User removing the Pin.

**Preconditions:** The User must be logged in, the Pin must exist, the Pin they are removing is owned by that User.

**Trigger:** The User chooses to delete the Pin which they have created.

**Success Outcome:** There Pin no longer exists on the User's profile or on the map for anyone. The information is erased from database.  

**Main Success Scenario**:
1. The User chooses a Pin to remove.
2. The User deletes the Pin.
3. The Pin and its information is removed from the databsae.
4. The Pin is no longer visible by anyone.

**Extensions:**
1. The Pin does not exist before the User completes the delete.
2. The Pin is not owned by the User.
3. The information and object still exist within the database afterwards.
4. Other Users are still able to view the deleted Pin. 

#### View a Pin
**Primary Actor:** The User trying to view the Pin.

**Precondition:** The User is logged in, the Pin exists, and the User has permissions to view the Pin they are accessing.

**Trigger:** The User asks the system to retrieve a Pin.

**Success outcome:** The information for the Pin is displayed on the screen.

**Main Success Scenario:**
1. The User asks the system to retrieve a Pin.
2. The system finds the Pin and its corresponding informaiton.
3. The information is formatted and displayed on the screen and can not be edited by the User.

**Extensions:**
1. The system can not find the Pin.
2. The system allows the User to view/edit a Pin they do not have access to.
3. The system does not allow the User to view/edit a Pin they should have access to.
4. The system opens the incorrect Pin.
5. The incorrect information is displayed.
   
### Structure (Classes, Interfaces, and Methods)

The following are folders, structured to their uses:

Models: 
* Book
* Coordinate
* Passage
* Pin
* User

### Resources & APIs
* MapBox for map API
* Project Gutenberg for book access. Note that this site only has opensource books, without copyright.
We may supplement this with any access we may have with the UofT library - though this will require
authentication. 

### ReadME Instructions

Please keep this up-to-date with information about your project throughout the term.

The readme should include information such as:
- a summary of what your application is all about
- a list of the user stories, along with who is responsible for each one
- information about the API(s) that your project uses
- screenshots or animations demonstrating current functionality

By keeping this README up-to-date,
your team will find it easier to prepare for the final presentation
at the end of the term.
