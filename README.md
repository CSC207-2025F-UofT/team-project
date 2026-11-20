# Collage App
## Team Information
### Team Members
- Rowan Parkinson
- Niki Zhang
- Elya Fan
- Gwen Gao
- Alex Heo
- Kyra Park
---
## Project Description
### Domain

---
## User Stories
| # | User Story                                                                                                                                        |
|---|---------------------------------------------------------------------------------------------------------------------------------------------------|
| 1 | As a user, I want to draw on the canvas with a chosen colour and stroke thickness, so that I can add my own artwork to the collage.                   |
| 2 | As a user, I want to generate a colour palette from a base colour and save it with my project so I can keep my artwork visually consistent.       |
| 3 | As a user, I want to reorder layers and control their visibility so that I can manage the arrangement of my collage.                              |
| 4 | As a user, I want to undo or redo recent changes so that I can correct mistakes.                                                                  |
| 5 | As a user, I want to import an image from my computer and adjust its size and shape (i.e. crop and cut out) so that I can personalize my collage. |
| 6 | As a user, I want to search for external images and add them to my canvas so that I can find my material for my collage without leaving the app.  |
| 7 | As a user, I want to save and reopen my project so that I can continue editing my work.                                                           |
| 8 | As a user, I want to export my project as a PNG so that I can share my finished artwork.                                                     |
| 9 | As a user, I want to publish my work to the community and comment on others’ projects.                                                            |


---
## Use Cases
### Use Case 1: Free Draw
Primary Actor: User 

Preconditions: User has created or opened a Canvas

Trigger: User selects the drawing tool

Main Success Scenario:
1. User selects the drawing tool and adjusts the stroke colour and stroke thickness.
2. User clicks and drags on the canvas to free draw.
3. System displays the user's stroke on the canvas.
4. User continues to draw or selects a different tool.

Alternative Flows:
- 1a. User selects a colour from their created palette instead of choosing a custom colour.
- 1b. User uses the default stroke thickness.

Outcome: The user's drawing is added to the canvas as a new layer.

### Use Case 2: Draw a Shape
Primary Actor: User

Preconditions: User has created or opened a Canvas

Trigger: User selects the shape tool

Main Success Scenario:
1. User chooses a shape (circle, square, star) and adjusts the colour and width.
2. User clicks the canvas to place the center of the shape.
3. System stamps the shape on the canvas.
4. User selects the shape and drags to adjust its position.
5. System updates the canvas accordingly. 

Alternative Flows:
- 1a. User chooses a different shape without stamping.
- 1b. User inputs an invalid width. System displays an error message.

Outcome:

### Use Case 3: Generate Colour Palette
Primary Actor: User

Preconditions: User has created or opened a Canvas.

Trigger: User opens the colour panel. 

Main Success Scenario:
1. User selects or inputs a base colour.
2. User specifies a type of colour scheme from a dropdown menu.
3. System calls The Color API to generate a custom colour palette. 
4. System displays the generated colours. 
5. User saves the palette to their project.
6. System makes the saved colour swatches available in the colour window.

Alternative Flows:
- 1a. User chooses a different base colour and generates a new palette
- 2a. User selects a different colour scheme type and generates a new palette
- 5a. User does not save the palette

Outcome: The generated colour palette is saved to the project and can be used while creating.

### Use Case 4: Manage Layers
Primary Actor: User

Preconditions: User has created or opened a Canvas containing at least one Layer. 

Trigger: The user opens the layer management panel.

Main Success Scenario:
1. System displays the Project's layers in their current stacking order.
2. User selects a layer
3. User reorders the layer by dragging it to a different position.
4. System updates the stacking order.
5. User toggles layer visibility.
6. System hides or displays the corresponding layer.

Alternative Flows:
- 2a. User selects a different layer. 
- 3a. User cancels the reorder.

Outcome: The project's layer order and visibility are updated.

### Use Case 5: Undo & Redo
Primary Actor: User

Preconditions: User has created or opened a Canvas and at least one editable action has been performed.

Trigger: The user clicks the undo or redo button.

Main Success Scenarios:
1. User clicks the undo button.
2. System reverses the most recent action.
3. System updates the Canvas to reflect the previous state. 
4. User selects the redo button if they want to restore the undone action.
5. System reapplies the action.
6. System updates the Canvas accordingly. 

Alternative Flows:
- 1a. User selects undo when no previous actions are available. System disables the undo function.
- 2a. User performs a new action after undoing. System clears the redo history.
- 4a. User selects redo when no actions are available to redo. System disables the redo function.

Outcome: The canvas reflects the user's desired editing state.

### Use Case 6: Import Image
Primary Actor: User

Preconditions: User has created or opened a Canvas and an image file is available on their computer.

Trigger: User clicks the import image button.

Main Success Scenario:
1. User selects the import image button.
2. System opens the computer's file browser.
3. User selects the desired image file.
4. System imports and displays the image on the canvas.

Alternative Flows:
- 3a. User selects an unsupported file format. System displays an error message.
- 3b. User cancels the file selection.

Outcome: The imported image is added to the Canvas as a new layer.

### Use Case 7: Search for Image
Primary Actor: User

Preconditions: User has created or opened a Canvas.

Trigger: User clicks the image search button.

Main Success Scenario:
1. User enters a search term in the image search panel.
2. System retrieves relevant images using an API call.
3. System displays the top search results.
4. User selects an image.
5. System imports the selected image onto the canvas.

Alternative Flows:
- 2a. No matching images are found
- 4a. User decides not to select any of the displayed images

Outcome: The external image is added to the Canvas as a new layer.

### Use Case 8: Edit Image
Primary Actor: User

Preconditions:

Trigger:

Main Success Scenario:

Alternative Flows:

Outcome:

### Use Case 9: Create Project
Primary Actor: User

Preconditions: User has logged into the app.

Trigger: User clicks the button to create a new project.

Main Success Scenario:
1. System prompts the user to input canvas dimensions and a project name.
2. System creates a new, blank canvas of the specified size.
3. System opens the project in the editing view.

Alternative Flows:
- 1a. User inputs unsupported dimensions. System reports an error.

Outcome: A new project is created and ready for editing.

### Use Case 10: Open Project
Primary Actor: User

Preconditions: User is logged in and has at least one existing saved project.

Trigger: User selects an existing project to open.

Main Success Scenario:
1. System displays the user's available projects.
2. User selects a project.
3. System retrieves the project's saved data. 
4. System loads the project onto the canvas.
5. User resumes editing.

Alternative Flows:
- 2a. User has no saved projects. System displays an empty project list.

Outcome: The selected project is open and ready for editing.

### Use Case 11: Save Project
Primary Actor: User

Preconditions: User has an open project with unsaved changes.

Trigger: User clicks the Save Project button.

Main Success Scenario:
1. User makes a change to their project.
2. System detects that the project has been modified.
3. User clicks save.
4. System saves the project's data and confirms the project has been saved.

Alternative Flows:
- 3a. User attempts to exit the app without saving. System shows a warning message reminding the user to save their work.
- 4a. The project has no new changes to save. System does nothing.

Outcome: The project's current state is saved to the user's project bank.

### Use Case 12: Export Project
Primary Actor: User

Preconditions: User has an open project. 

Trigger: User clicks the Export Project button.

Main Success Scenario:
1. System renders the canvas as a PNG image. 
2. System prompts the user to download the image.
3. User downloads the image to their computer.

Alternative Flows:
- 3a. User cancels the download.

Outcome:

### Use Case 13:  Publish Project
Primary Actor: User

Preconditions: User has an open project. 

Trigger: User clicks the Publish Project button.

Main Success Scenario:
1. System prompts the user to enter a description and edit the title, if needed.
2. User provides the requested information.
3. System displays a preview of the project as it will appear on the community explore page.
4. User confirms publication.
5. System uploads the project to the community explore tab.

Alternative Flows:
2a. User leaves title field blank. System prompts the user to complete the field. 
4a. User cancels publication
5a. User attempts to publish a project that is already published. System offers to Unpublish or Update the existing publication instead.

Outcome: The project is published to the community explore tab and available for other users to view.


### Use Case 14: Explore Other Projects
Primary Actor: User

Preconditions: User is logged in and on the home page.

Trigger: User navigates to the community explore tab.

Main Success Scenario:
1. System displays published projects from other users.
2. User browses the displayed projects.
3. User selects a project to view.
4. System displays the selected project and its details.
5. User writes and submits a comment.
6. System publishes the comment beneath the project.
7. User continues browsing projects or returns Home.

Alternative Flows:
- 1a. No projects are currently published. System displays an empty gallery message.
- 5a. User attempts to submit an empty comment. System prompts the user to enter text.

Outcome:

### Use Case 15: Sign up / Login / Logout
Primary Actor: User

Preconditions:

Trigger:

Main Success Scenario:

Alternative Flows:

Outcome:

---
## Minimal Viable Product (MVP)
| Team Member     | MVP Use Case        |
|-----------------|---------------------|
| Rowan Parkinson | Use Cases 6, 7      |
| Niki Zhang      | Use Cases 1, 2      |
| Elya Fan        | Use Cases 5, 15     |
| Gwen Gao        | Use Cases 9, 10, 11 |
| Alex Heo        | Use Cases 13, 14    |
| Kyra Park       | Use Cases 4, 12     |
---

## Entities
- Project:
- Canvas:
- Layer:
- Drawing:
- Image:
- ColourPalette:
- User:

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
