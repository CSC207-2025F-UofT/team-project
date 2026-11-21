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

# PawPortal

## Team Information

### Team Members
- Audrey Luo
- Xinmeng Zhang
- Xiangyu Zhou
- Kenny Gu

---
## Project Description
The problem: The shelter's current process is opaque. 
Adoptable animals are advertised on Instagram, and anyone who wants to adopt must first apply on the shelter's website to become a general "adopter" 
and wait for an interview before they can even see which cats are available. People are blocked by the process before they can make an informed choice.

Our solution: PawPortal lets anyone browse every adoptable cat and dog, 
each with an honest, detailed profile (temperament, health conditions, vaccination and spay/neuter status, adoption fee). 
An adopter compares a pet's needs with their own situation and then applies for that one specific pet. 
The shelter manages pets and reviews applications in one place, and adopters can track the status of their applications online.

Scope: We focus on a small core: browse, match, apply, track, and review. Everything else is listed under Future Work in the Notes section.

### Domain
PawPortal is a pet adoption portal for a nonprofit animal shelter.

---
## User Stories
### User Story 1
As a visitor, I want to browse and filter all adoptable pets without logging in, 
so that I'm not blocked by an approval step before I can see who is available.
### User Story 2
As an adopter, I want to see how a pet's needs (for example, daily medication or a quiet home) line up with my own situation, 
so that I can decide whether to apply with full information instead of guessing.
### User Story 3
As an adopter, I want to submit an application for one specific pet and track its status online, 
so that I always know where things stand without depending on someone replying to a message.
### User Story 4
As a shelter admin, I want to publish and maintain accurate pet profiles and review applications in one place, 
so that animals are matched with suitable adopters efficiently.

---
## Use Cases
### Use Case 1
Sign up / Admin Sign up / Log in \
The adopter enters their username, id, email, and password to sign up.
The admin enters their username, id, email, password, and invite code to sign up.
Both log in with their email and password.
The system validates the input and checks that the id and the email are not already registered (sign up) or that the 
credentials match (log in).
After signing up, the system opens the pet gallery.
### Use Case 2
Add / Edit / Remove a Pet \
The admin opens the pet management screen and chooses Add, Edit, or Remove.
For Add/Edit, the admin fills in the pet form: name, species, breed, age, sex, temperament tags, health notes, vaccinated, spayed/neutered, adoption fee, and photo.
The system validates the form and saves the pet.
The pet appears in (or disappears from) the public gallery.
### Use Case 3
Browse and Filter Pets \
The user opens the gallery and sees cards for all pets with status AVAILABLE.
The user applies filters (species, age range, temperament, health needs).
The gallery updates to show only matching pets.
The user clicks a card to open the pet's detail page.
### Use Case 4
Check Pet Compatibility \
The adopter fills in their preferences once: children at home, other pets, willingness to care for a pet with medical needs, preferred species.
On a pet's detail page, the system compares the pet's profile with the adopter's preferences.
The system shows each trait as a match or a conflict, with a short explanation (for example, "Needs daily medication: you said you cannot take this on").
### Use Case 5
Submit an Application \
From a pet's detail page, the adopter clicks "Apply".
The adopter fills in the application: housing type, experience with pets, household members, contact information, and a short note.
The system validates the form, creates an application with status PENDING, and links it to the pet and the adopter.
### Use Case 6
Track My Applications \
The adopter opens "My Applications".
The system lists each application with the pet's name, the date submitted, and the current status (PENDING, INTERVIEW, APPROVED, REJECTED, WITHDRAWN).
The adopter can open an application to see the shelter's note, and can withdraw an application that is still PENDING.
### Use Case 7
Review Applications \
The admin opens the applications list and can filter by pet or by status.
The system shows each application with the pet's name, the adopter's name, the date submitted, and the current status.
The admin opens an application to read the adopter's answers.
### Use Case 8
Update Application Status \
The admin changes the application status with an optional note.
The system saves the change. The adopter sees the new status and note in "My Applications".
When an application is APPROVED, the system marks the pet as ADOPTED.

---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Audrey Luo | Use Case 3: Browse and Filter Pets, Use Case 4: Check Pet Compatibility |
| Kenny Gu | Use Case 2: Add / Edit / Remove a Pet |
| Xiangyu Zhou | Use Case 5: Submit an Application, Use Case 6: Track My Applications |
| Xinmeng Zhang | Use Case 1: Sign up / Log in, Use Case 7: Review Applications, Use Case 8: Update Application Status|
---
## Entities
### User
Id, name, email, passwordHash
role: ADOPTER or ADMIN
preferences (an AdopterPreferences object, adopters only)
### Pet
id, name, species (CAT / DOG), breed, ageYears, sex
temperamentTags (list, for example "affectionate", "shy", "good with kids")
healthNotes, needsMedication, vaccinated, spayedNeutered
adoptionFee, photoPath
status: AVAILABLE, PENDING, or ADOPTED
### Application
id, adopterId, petId
housingType, petExperience, householdMembers, contactInfo, note
status: PENDING, INTERVIEW, APPROVED, or REJECTED
adminNote, submittedAt
### AdopterPreferences
hasChildren, 
hasOtherPets, 
canHandleMedicalNeeds, 
canHandleKitten&Pregnancy,
preferredSpecies

---
## API Usage
**API Name: TheCatAPI**
**Documentation:** [ https://homeatlastrescue.ca ]
**How it is used in the program: When an admin picks a breed in the Add/Edit Pet form, 
the program calls the API to fetch general breed information (typical temperament and a 
reference photo).**

**API Name: TorontoCat**
**Documentation:** [ https://torontocatrescue.ca/ ]
**How it is used in the program: When an admin picks a breed in the Add/Edit Pet form,
the program calls the API to fetch general breed information (typical temperament and a
reference photo).**

**API Name: **
**Documentation:** [ https://northtorontocatrescue.com/ ]
**How it is used in the program: When an admin picks a breed in the Add/Edit Pet form,
the program calls the API to fetch general breed information (typical temperament and a
reference photo).**

https://www.torontohumanesociety.com/cats/
https://teamcatrescue.com/
https://annexcatrescue.ca/

---
## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

---

## How to Run
1. Clone the repo:

   `git clone [repo URL]`

   `cd [repo name]`

2. Set the admin invite code (needed to sign up as an admin):

   `export PAWPORTAL_ADMIN_INVITE_CODE=your-code-here`
 
3. Run the app:

   `mvn compile exec:java -Dexec.mainClass=app.Main`

---
## Notes
[Any additional project information.]
