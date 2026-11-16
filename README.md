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


# Project Name
- CocktailCraft
## Team Information
- team name: Team400
- TUT: 0401 BA2200 Group4
### Team Members
- Qinli Yang
- Enhao Tang
- Jiaqi Shen
- Wenqian Ling
---
## Project Description
CocktailCraft is an application for discovering, customizing, and managing cocktail recipes. It allows users to search and filter cocktails, personalize ingredients and quantities, save favourites, rate drinks, and receive recommendations based on the ingredients they have available.
### Domain
- food: drinks
---
## User Stories
### User Story 1 [Qinli]
As a user, I want to search for cocktails by name and filter them by base spirit, flavour profile, and carbonation so that I can easily find drinks that match my preferences.
### User Story 2 [Enhao]
As a user, I want to customize existing cocktail recipes by adding, removing, or replacing ingredients and adjusting quantities so that I can make drinks according to my personal preferences.
### User Story 3 [Jiaqi]
As a user, I want to save my favourite cocktails, rate them, and add personal notes so that I can keep track of drinks I enjoy and revisit their recipes later.
### User Story 4 [Wenqian]
As a user, I want to browse cocktails on the main page and save my favourite cocktails so that I can easily find and revisit them later.

---
## Use Cases
### Use Case 1 [Qinli]
Inputs
* Cocktail name: A text field where the user types a cocktail name.
* Base spirit: A dropdown with the options: Gin, Vodka, Rum, Tequila, Whiskey, Brandy, and Other.
  
Input requirement
The user must enter a cocktail name or select a specific base spirit, or both. The user can select multiple base spirits at a time, and the system would return (if exist) any cocktail that contains at least one of the base spirit.

Normal Flow
1. The user enters a cocktail name and/or selects the base spirit(s).
2. The user clicks Search.
3. The system searches for cocktails that match all provided criteria. Name matching is case-insensitive and allows partial matches.
4. The system displays the matching cocktail names in a results list.
   
Alternative Flows
* If the name is blank and there is no selected base spirit, the system displays: “Please enter a cocktail name or select a base spirit.”
* If no cocktails match, the system displays: “No cocktails found.”
* If the API request fails, the system displays an error message and allows the user to retry.

### Use Case 2 [Enhao]
Input
1) An existing cocktail recipe selected by the user.
2) Ingredient names, quantities, and units.
3) A name for a customized recipe

Normal Flow
1) The user selects an existing cocktail recipe and clicks Customize.
2) The system displays the recipe's ingredients, quantities, and units.
3) The user adds, removes, or replaces ingredients and adjusts quantities.
4) The user enters a name for a customized recipe and clicks Save.
5) The system validates the input.
6) The system saves the customized recipe separately from the original and displays the saved recipe.

Alternative Flows
1) If the recipe name or an ingredient name is blank, the system asks the user to enter the missing information.
2) If a quantity is not a positive number or a unit is missing, the system asks the user to enter a valid quantity and unit.
3) If all ingredients have been removed, the system displays: “Please add at least one ingredient.”
4) If saving fails, the system displays an error message and allows the user to retry.
5) If the user clicks Cancel, the system discards the changes and returns to the original recipe.

### Use Case 3 [Jiaqi]
use case 1:
MAIN SUCCESS SCENARIO
1. User access log in page, enters their username and password. 
2. The user click log in/cancel; 
3. The system received the account information.
4. The user enters the main page.
5. If the user doesn't have an account
6. the user click registar.
7. The user chooses a username
8. The user chooses a password and enters it twice to help catch typing mistakes
9. If the username already exists, the system alerts the user
10. If the two passwords don't match, the system alerts the user
11. If the username doesn’t exist in the system and the passwords match, then the system creates the user but does not log in them.
12. System goes back to the login page.

EXTENSION


use case 2:
MAIN SUCCESS SCENARIO
1. User enters the cocktail she/he tried, gives it a rating from 1 to 5, and add additional notes optionally.
2. The users click save button
3. The system received the entered information.
4. System updates the information and display them in the saved page.

EXTENSION:
1. if the user didn’t enter cocktail name or score or both of them and try to click save button first, then the system asks user to enter both of them.
2. Once the user click the saved page, it should display all the rating history to the user. User can click the the specific history to check more details.


### Use Case 4 [Wenqian]
PRECONDTION
1. The application is running.
2. The user is on the main page.

MAIN SUCCESS SCENARIO
1. User browses the cocktails.
2. User click the cocktail.
3. The system display the detail of this cocktail.
4. User selects a cocktail they like.
5. User clicks the "Favourite" button.
6. The system saves the selected cocktail to the user's favourites.
   
EXTENSION
1. If the cocktail is already saved, the system informs the user and ask user whether to remove it from favourites.
2. If saving fails, the system displays an error message.

(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Qinli | The user enters a cocktail name and/or selects one or more base spirits, then clicks Search. The system displays matching cocktail names or a message indicating missing input, no matches, or an API failure. |
| Enhao | The user selects an existing cocktail recipe, adds, removes, or replaces ingredients, and adjusts quantities. The user then names and saves the customized recipe. The system validates the input and saves it separately from the original recipe.  |
| Jiaqi | The user can log in and log out at any time. They are able to create an account if they never log in before. The user enters a cocktail name, then clicks Save. The system saved all the information above to save pages. |
| Wenqian | The user clicks a cocktail, the system display the ingredients of it. The user click 'Favourite' button, then save this cocktail to home page.|
---
## Entities
### Entity 1
Cocktail
- name
- baseSpirit
- flavour
- recipeIngredient
### Entity 2
Ingredient
- name
- category
- flavour
### Entity 3
RecipeIngredient
- ingredient
- unit
- quantity
### Entity 4
User
- userId
- userName
- favourite
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
