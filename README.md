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
As a user, I want to register an account, log in and log out, and access my personal profile so that I can securely manage my account and revisit my favourite cocktails and customized recipes.
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
INPUTS
- Username: The username entered by the user.
- Password: The password entered by the user.
- Confirm Password: Required only when registering a new account.
- Login, Sign Up, Profile, and Logout buttons.

MAIN SUCCESS SCENARIO
1. The user opens the Login page.
2. The user enters their username and password.
3. The user clicks the "Login" button.
4. The system verifies the username and password.
5. If the credentials are valid, the system logs the user in and establishes the current-user session.
6. The system displays the user's personal profile, including their username.
7. The user can navigate to Favourite Cocktails or My Custom Cocktails from their profile.
8. When the user clicks "Logout", the system clears the current-user session and returns to the Login page.

EXTENSION

User does not have an account
  1. The user clicks the "Sign Up" button on the Login page.
  2. The system displays the Registration page.
  3. The user enters a username, password, and password confirmation.
  4. The user clicks the "Sign Up" button.
  5. The system checks that the required fields are completed, the passwords match, and the username is available.
  6. The system creates and saves the new account.
  7. The system displays a registration success message and returns to the Login page.
  8. The user continues from Step 2 of the Main Success Scenario.

Invalid username or password
  1. The system displays an error message indicating that the username or password is incorrect.
  2. The user can re-enter their credentials and try again.

Invalid registration information
  1. If the system cannot verify the account because the data service is unavailable, it displays an error message.
  2. The user remains on the Login page and can try again later.

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
| Jiaqi | The users register a new account, log in with their username and password, and access their personal profile to access saved cocktails. The system saves user account information so that users can log in again after restarting the application.|
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
