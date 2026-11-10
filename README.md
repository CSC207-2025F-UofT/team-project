# Journeye

## Team Information

### Team Members

- Andrei Berzin
- Preston Cai
- Sebastian Cooper-Giannotta
- Tymofiy Spektor

---

## Project Description

### Domain

[Project description]

---

## User Stories

### User Story 1
As a user, I want to generate my travel history by uploading photos so that I can record trips without manual entry.

### User Story 2
As a user, I want to split and merge automatically generated trips in order to resolve inaccuracies in trip generation.

### User Story 3
As a user, I want to visualize my travels on an interactive map so that I can explore my past trips geographically.

### User Story 4
As a user, I want to view analytics regarding my travel history in order to understand my patterns and preferences.

---

## Use Cases

### Use Case 1
- **Name:** Import Photo Collection
- **Primary actor:** User uploading their photos
- **Preconditions:**
  - A valid archive has been selected.
- **Trigger:** The user clicks the import button and selects the photos they want to upload.
- **Success outcome:** The photos are imported and a success message is displayed.
- **Main success scenario:**
  1. The user selects the photos they want to upload.
  2. The system asks the user to input an optional home location or region for ignoring nearby photos during automatic trip generation.
  3. The system begins ingesting the photos into the archive directory and reading their metadata.
  4. The system displays upload progress and statistics, including a counter for photos with invalid metadata.
  5. The system starts categorizing the photos into new or existing trips.
  6. The system displays a success message after all the photos are imported and sorted.
- **Extensions:**
  - **i.** The user-selected photo path is invalid.
    1. The system explains that the selected path was invalid.
    2. The use case ends.
  - **v.** None of the imported photos have location or time metadata.
    1. The system explains smart trip categorization is not possible.
    2. The use case continues.

### Use Case 2
- **Name:** Edit Existing Trips
- **Primary actor:** User viewing past trips
- **Preconditions:**
  - A valid archive is open.
  - There is at least one trip.
- **Trigger:** User selects a trip (on the map or inside the trips tab) and chooses how they want to edit it .
- **Success outcome:** If "Merge" is selected, the n selected trips are merged into one. If "Split" is selected, the chosen trip gets split into n trips based off the specified cutoffs. 
- **Main success scenario:**
  1. The user selects one or multiple trips.
  2. The user selects "Split" or "Merge"
  3. If "Split" is chosen, the user enters the desired cutoff points
    1. The System splits the chosen trip using the cutoffs, and updates the map.
    2. If Merge is chosen, the system merges the selected trips into one trip, and updates the map.
- **Extensions:** None

### Use Case 3
- **Name:** Explore Travel Map
- **Primary actor:** User viewing the map
- **Preconditions:**
  - A valid archive is open.
- **Trigger:** The user clicks on the map tab.
- **Success outcome:** The user's travel history is displayed on a map.
- **Main success scenario:**
  1. The user clicks on the map tab.
  2. The system displays a map of the world with all their locations plotted.
  3. The system dynamically clusters and declusters locations as the user zooms in and out.
  4. The system displays the contained photos when the user clicks on a cluster.
- **Extensions:** None

### Use Case 4
- **Name:** Analyze Travel History
- **Primary actor:** User viewing the analytics
- **Preconditions:**
  - A valid archive is open.
- **Trigger:** The user clicks on the analytics tab.
- **Success outcome**: A tab opens up with options to analyze trips.
- **Main success scenario:**
  1. User select a trip from a dropdown menu, and it creates visualizations such as time elapsed, date ranges, all locations, famous landmarks, favourite photos
  2. User can choose analytics by country, year, etc. and see more summative information such as number of cities visited, number of trips, selected photos, etc.
- **Extensions:** None

---

## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Sebastian | Use Case 1 |
| Andrei | Use Case 2 |
| Tymofiy | Use Case 3 |
| Preston | Use Case 4 |

---

## Entities

### LocationInfo
Represents the information for a location including its city, county, state, country, and optionally a landmark (i.e. the Eiffel Tower).

### Location
Represents the specific latitude and longitude coordinates of a location, as well as its qualitative information.

### Photo
Represents a photo and its associated data such as time and location captured.

### Trip
Represents a past trip, containing a name, start date, end date, and a list of photo ids.

---

## API Usage

### OpenStreetMap
- **Documentation:** [Link](https://wiki.openstreetmap.org/wiki/API)
- **Usage:** Handles reverse-geocoding to resolve location names and information from latitude and longitude metadata in travel photos.

### Google Cloud Vision API
- **Documentation:** [Link](https://docs.cloud.google.com/vision/docs)
- **Usage:** Provides visual landmark detection for travel photos that are missing location metadata.

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
