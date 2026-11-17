# Team Project
--------------------------------
# Project Name: Flicksy
## Team Information
### Team Members
- Mehal Patel 
- Grace Manurung
- Syaikha Faiza Arifin
- Yuxuan Lin
- Boao Li
- Zhanyuan Su
---
## Project Description
Our group is making a project where we create a movie finder application for people so that they could get an insight about a movie that they wanted to watch. It could 
also be a platform where they could make their own list about the movies that they have already watched.
### Domain
entertainment
---
## User Stories
### User Story 1:
#### As a user of the movie finder I want to be able to search movies by their title so that I can look at its description, rating and other relevant information.  
### User Story 2:
#### As a user of the movie finder, I want to see the details regarding the searched movie so that I can review its synopsis, rating, release year, and cast to decide if I want to watch it.
### User Story 3:
#### As a user of the movie finder I want to have a watchlist so that I can save movies I plan to watch later.
### User Story 4:
#### As a user of the movie finder I want to add favourites so that I can easily find and keep track of the movies I like.
### User Story 5:
#### As a user of the movie finder, I want to view movie posters so that I can visually identify films.
### User Story 6:
#### As a user of the movie finder I want to see past comments or movie ratings so that I can look at other's opinion about the movie and also the overall rating of a movie.
### User Story 7:
#### As a user [type of user] I want to filter and sort movies so that I can [some benefit]
### User Story 8:
#### As a user [type of user] I want to be able to log in/log out so that I can [some benefit]

---
## Use Cases
### Use Case 1
Use Case: Search a movie 

Primary actor: User entering the title and searching the movie 

Preconditions:

The user is logged in.
The user has typed the name of the movie in the text field.
Trigger: The user clicks the search button.


Success outcome: The title has been found and the details regarding the movie appears.

Main success scenario:

The user enters the movie title.
The system checks that the movie is present.
The system displays the details of the movie.

Extensions:

2a. The movie is not found in the system.
The system explains that the movie is not found.
The system does not display the movie details.

### Use Case 2
### Use Case 3
Use Case: Add a movie to the watchlist

Primary actor: User who wants to save a movie to watch later

Preconditions:

The user is logged in.
The user has searched for a movie and is viewing its details page.
Trigger: The user clicks the "Add to Watchlist" button.


Success outcome: The movie is saved to the user's watchlist and remains there after the user logs out and logs back in.

Main success scenario:

The user clicks the "Add to Watchlist" button on the movie details page.
The system checks that the movie is not already in the user's watchlist.
The system adds the movie to the user's watchlist and saves it.
The system displays a confirmation message that the movie was added.

Extensions:

2a. The movie is already in the user's watchlist.
The system explains that the movie is already in the watchlist.
The system does not add the movie again.

### Use Case 4
Use Case: Add a movie to favourites

Primary actor: User who wants to save a movie as one of their favourite movies.

Preconditions:
The user is logged in.
The user has searched for a movie and is viewing its details page.

Trigger: The user clicks the "Add to Favourites" button.

Success outcome: The movie is successfully added to the user's favourites list and remains there after the user logs out and logs back in.

Main success scenario:
The user clicks the "Add to Favourites" button on the movie details page.
The system checks whether the movie is already in the user's favourites list.
The system adds the movie to the user's favourites list and saves the changes.
The system displays a confirmation message that the movie has been added to favourites.

Extensions:

2a. The movie is already in the user's favourites list.
The system informs the user that the movie is already in their favourites.
The system does not add the movie again.

### Use Case 6
Use Case: View past comments and movie ratings

Primary actor: User clicking the movie posters and the past comments and ratings will pop up

Precondition: 
The user is logged in.
The user has searched for a movie and can view its poster.

Trigger: User clicks on the movie poster.

Success Outcome: The user can view the available comments and overall rating of the selected movie.

Main success scenario:
User clicks on the movie poster
The system retrieves the comments and ratings associated with the selected movie
The system displays the available comments and overall movie rating

Extensions:
2a. No comments or ratings are available for the selected movie
the system displays a message indicating that no comments or ratings are available
the system does not display any comments or ratings


(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Mehal Patel | Use Case #1: Search a movie|
| Grace Manurung | Use Case #6: Comments and Ratings |
| Syaikha Faiza Arifin | Use Case #3: Add a movie to watchlist |
| Yuxuan Lin | Use Case #4: Add a movie to favourites |
| Frank Li | Use Case |
| Zhanyuan Su | Use Case |
---
## Entities
### Entity 1: Movie
### Entity 2: User 
(Add additional entities as needed.)
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

