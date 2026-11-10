# Project Name (WIP)

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
As a user, I want to generate my travel history by uploading photos so that I can automatically record location history without manual entry.

### User Story 2
As a user, I want to visualize my travels on an interactive map so that I can explore my past and planned trips geographically.

### User Story 3
As a user, I want to sort, categorize, and view analytics regarding my travel history in order to understand my patterns and preferences.

### User Story 4
As a user, I want to plan and optimize future trip itineraries in order to reduce overall cost and travel time.

---

## Use Cases

### Use Case 1
- **Name:** Import Photo Collection
- **Primary actor:** User uploading their photos
- **Preconditions:** A data directory has been configured by the user.
- **Trigger:** The user clicks the upload button and selects the photos they want to upload.
- **Success outcome:** The photos are imported and a success message is displayed.
- **Main success scenario:**
  1. The user selects the photos they want to upload.
  2. The system begins ingesting the photos and reading their metadata.
  3. The system displays upload progress and statistics, including a counter for photos with invalid metadata.
  4. The system displays a success message after all the photos are imported.

- **Extensions:**
  - **1.** The user-selected photo path is invalid.
    1. The system explains that the selected path was invalid.
    2. The use case ends.

### Use Case 2
- **Name:** Explore Travel Map
- **Primary actor:** User viewing the map
- **Preconditions:** The user has selected a data directory.
- **Trigger:** The user clicks on the map tab.
- **Success outcome:**
- **Main success scenario:**
- **Extensions:**

### Use Case 3
- **Name:** Analyze Travel History
- **Primary actor:** User viewing the analytics
- **Preconditions:**: A travel project (archive) has been opened.
- **Trigger:** The user clicks on the analytics tab.
- **Success outcome: A tab opens up with options to analyze trips.
- **Main success scenario:**
  1. User select a trip from a dropdown menu, and it creates visualizations such as
  time elapsed, date ranges, all locations, famous landmarks, favourite photos
  2. User can choose analytics by country, year, etc. and see more summative
  information such as number of cities visited, number of trips, selected photos, etc.
- **Extensions:**

### Use Case 4
- **Name:** Plan Future Trips
- **Primary actor:** User planning a new trip
- **Preconditions:** A data directory has been configured by the user.
- **Trigger:** The user asks the system to create a new trip itinerary.
- **Success outcome:**
- **Main success scenario:**
- **Extensions:**

---

## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Sebastian | Use Case 1 |
| Tymofiy | Use Case 2 |
| Preston | Use Case 3 |
| Andrei | Use Case 4 |

---

## Entities

### Trip
Represents a past trip, with metadata such as places (cities) in chronological order, 
datetime, landmarks, photo ids, blog_paths, etc.

### Location
Represents a specific location, including longitude/latitude, location name (such
as Bahen center), city, state/province, country, etc.

### Photo
Represents a photo and its associated data such as datetime captured and location.
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
