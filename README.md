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

# Recipe Finder

## Project Specification

Recipe Finder is a Java application that helps users search for recipes using an external recipe API.
Users can search for recipes, view recipe details, filter results, and save recipes they like.

## User Stories

1. As a family member, I want to search for recipes by keyword so that I can find meals I want to cook.(Lindsay)
### Use Case 1: Search Recipes by Keyword

**Lead:** Lindsay

**Primary Actor:** Family Member

**Goal:** Allow users to search for recipes by keyword to find meals they want to cook.

**Preconditions:**
- The user is on the recipe search page.
- A search field is available.

**Main Success Scenario:**
1. The user enters a keyword in the search field.
2. The user clicks the "Search" button.
3. The system receives the keyword and searches for matching recipes using TheMealDB API.
4. The system displays a list of matching recipes, including their names and images.
5. The user can select a recipe to view its details.

**Alternative Scenarios:**
- If the keyword is empty or contains only whitespace, the system asks the user to enter a keyword.
- If no recipes match the keyword, the system displays a message indicating that no matching recipes were found.
- If the API request fails, the system informs the user that the search results could not be loaded and allows them to try again.

**Postconditions:**
- If the search succeeds, matching recipes or a no-results message are displayed.
- The user can enter another keyword to perform a new search.

## 2. As a cook, I want to view the details of a recipe so that I can see its ingredients and cooking instructions.

(Shangzhe Jin)
### Use Case 2: View Recipe Details

**Lead:** Shangzhe Jin

**Primary Actor:** User

**Goal:** Allow users to view the ingredients and cooking instructions of a selected recipe.

**Preconditions:**
- The user has selected a recipe from search or filter results.
- A valid recipe ID is available.

**app.Main Success Scenario:**
    1. The user clicks "View Details" on a recipe.
    2. The system receives the selected recipe ID.
    3. The system retrieves the recipe details from TheMealDB API.
    4. The system displays the recipe name, image, category, ingredients, measurements, and cooking instructions.

**Alternative Scenarios:**
- If the recipe cannot be found, an error message is displayed.
- If the API request fails, the user is informed that the recipe details could not be loaded.

**Postconditions:**
- The selected recipe's details are displayed to the user.
- The user can return to the previous page.

3. As a cook, I want to filter recipes by category or dietary preference so that I can find recipes that fit my needs.(Eden)
### Use Case 3: Filter Recipes

**Lead:** Eden

**Primary Actor:** User

**Goal:** Allow users to filter recipes by category or dietary preference to find recipes that match their needs.

**Preconditions:**
- The user is on the recipe search or browsing page.
- Recipe data and available filtering options can be accessed.

**app.Main Success Scenario:**
1. The user selects a category or dietary preference from the available filter options.
2. The system receives the selected filter criteria.
3. The system retrieves recipe data from TheMealDB API and applies the selected filters.
4. The system displays a list of recipes that match the selected criteria.
5. The user can select a recipe to view its details.

**Alternative Scenarios:**
- If no recipes match the selected filters, the system displays a message indicating that no matching recipes were found.
- If the API request fails, the user is informed that the filtered recipes could not be loaded.
- If the user clears the filters, the system displays the unfiltered recipe results.

**Postconditions:**
- The user can view recipes that match the selected category or dietary preference.
- The user can modify or clear the filters to explore other recipes.

4. As a cook, I want to save recipes to my favorites so that I can easily find them again later.(Isabella)