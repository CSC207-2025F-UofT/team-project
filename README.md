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

# Project Name: Wear House

## Team Information

### Team Members
- Sarah Zhang
- Lynn Bae
- Luiz Molnar
- Ostap Doroshenko

---

## Project Description

### Domain: Organization

This program allows the user to add pictures of their clothes to a wardrobe and use the clothes
to make various outfits. In addition, the program suggests outfits that would pair well with 
specific articles of clothing.

---

## User Stories

### User Story 1
As a user, I want to be able to save pictures of my clothes with a name and type so that I can 
keep track of all the clothes in my wardrobe.

### User Story 2
As a user, I want to be able to create an outfit with the clothes in my wardrobe so that I can
see how they will look together.

### User Story 3
As a user, I want to be able to get suggested outfits based on a piece of clothing so that I can 
pair items that look good together.

### User Story 4
As a user, I want to be able to search for specific clothes so that I can keep track of all the
clothes in my wardrobe.

### User Story 5
As a user, I want to be able to label specific clothes so that I can note attributes of the item.


(Add additional user stories as needed.)

---

## Use Cases

### Use Case 1: Add a Clothing Item
**Primary actor:** User

**Precondition:** 
- User is on wardrobe page
- User has an image of clothing

**Trigger:** User chooses to add an item

**Main success scenario:**
1. The user clicks "Add Clothing" button
2. The application displays a clothing registration form.
3. The user selects an image from their device.
4. The user enters a name and selects type "Top", "Bottom", "Outerwear", or "Shoes".
5. The user selects "Save"
6. The application validates the input and saves the clothing
   item, including its image, name, and label.

**Extensions:**
- If required information is missing, the application displays
  an error and allows the user to complete the form.
- If the image cannot be read, the application asks the user
  to select another image.
- If saving fails, the application displays an error and allows
  the user to retry.
- If the user cancels, no clothing item is saved.

**Success outcome:** The application displays the new item in the wardrobe.
### Use Case 2: View and Manage Saved Clothing
**Primary actor:** User

**Preconditions:**
- The application is running.

**Main Success Scenario:**
1. The user opens the wardrobe page.
2. The application loads and displays saved clothing items.
3. The user optionally searches for a clothing item.
4. The application displays the matching items.
5. The user selects an item.
6. The application creates a pop-up that displays its image, name, type, and has an edit and 
delete button.
7. If the user chooses to delete the item, the application requests confirmation.
8. The user confirms, and the application removes the item and updates the wardrobe display. 
9. If the user chooses to update the item, they can edit the text field. 
10. The user confirms changes, and the application and updates the item and wardrobe display.

**Extensions:**
- If the wardrobe is empty, the application displays a message inviting the user to add clothing.
- If no items match the selected name, the application displays a "No matching items" message.
- If the user cancels deletion, the item is kept.
- If the user cancels editing, the item is kept the same.

**Success Outcomes:** 
- User can view and search without changing stored clothing data.
- A successfully deleted item no longer appears in the wardrobe.
- A successfully edited item no longer contains previously edited information in the wardrobe.
- Saved clothing remains available after restarting the application.

### Use Case 3: Assemble and Save an Outfit
**Primary Actor:** User

**Preconditions:**
- The user has at least one saved clothing item.

**Main Success Scenario:**
1. The user selects "Create Outfit."
2. The application displays the user's saved clothing items.
3. The user selects items to include in the outfit.
4. The application displays the selected items together. 
5. The user selects "Save Outfit."
6. The application validates the input and saves the outfit. 
7. The application displays the saved outfit.

**Extensions:**
- If no clothing items are available, the application prompts
  the user to add clothing first.
- If no items are selected or the outfit name is missing,
  the application displays a validation error.
- If saving fails, the application displays an error
  and allows the user to retry.
- If the user cancels, no outfit is saved.

**Success Outcome:**
- An outfit containing the selected clothing
  items is saved.
- The original clothing items remain in the wardrobe.

### Use Case 4: Outfit suggestion
**Main Actor:** User

**Preconditions:**
- The user has saved clothing items that have clear and front facing images
- The Image Model API is configured and network access is available
- The user has selected "Create Outfit"

**Main Success Scenario:**
1. The application displays the user's saved clothing items.
2. The user selects an item to include in the outfit.
3. The user selects "Suggest Clothing" for a type of clothing. 
4. The application sends the relevant clothing images and
   associated information to the Image Model API. 
5. The API returns a list of suggested clothing items. 
6. The application validates that the suggested items exist
   in the user's wardrobe.
6. The application displays the suggested clothes. 
7. The user selects which suggestion to accept.
8. The user can repeat the outfit suggesting flow from step 3 onwards.
9. The user may choose to save the outfit through the outfit-saving 
flow described in UC3.

**Extensions:**
- If the wardrobe is empty, the application prompts the user
  to add clothing first.
- If the API is unavailable or the request times out,
  the application displays an error and allows a retry.
- If the API response is invalid or refers to unavailable
  clothing items, the application displays a message that
  no valid suggestion could be generated.

**Success outcome:** The user is able to create an outfit using suggested clothing pieces,
which the application displays.

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Sarah Zhang | Use Case 1: Add a Clothing Item |
| Lynn Bae | Use Case 3: Outfit assembly |
| Luiz Molnar | Use Case 4: Suggest an Outfit |
| Ostap Doroshenko | Use Case 2:  Assemble and Save an Outfit|

---

## Entities

### ClothingItem
**Description:** Contains information about individual pieces of clothing uploaded by the user.
**Attributes**
- id: String 
- name: String 
- imagePath: String 
- type: ClothingType 
- extractedFeatures: List<String>
- tags: List<Tags>

### Outfits
**Description:** Contains information about which clothes the user has paired together.
**Attributes**
- id: String
- name: String
- clothingIds: List<String>
- tags: List<Tags>

### Tags
***Description:*** Contains information about the type/style of clothes or outfit.
**Attributes**
- id: String
- name: String

---

## API Usage

**API Name:**

**Documentation:** [Link]

**How it is used in the program:**
- Analyzes clothing images and associated labels.
- Suggests combinations using items in the user's wardrobe.
- Returns clothing item identifiers so the application can display the corresponding saved items.

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