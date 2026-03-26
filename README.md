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

# Weather Application

## 📌 Overview
This application is a user-friendly weather and account-management platform that allows users to search real-time weather data, manage saved locations, and access personalized features. Users can create accounts, log in, save favourite places, and export weather information in multiple formats for sharing.

The system integrates external APIs for accurate geolocation and up-to-date weather reporting. With features like search suggestions, detailed weather views, and personal favourites, the application provides a simple and efficient experience for everyday weather lookup.

---

## 🧩 User Stories & Responsibilities

### Account-Related User Stories
| User Story | Responsible |
|-----------|-------------|
| As a user, I want to create an account so that I can access personalized features. | *Luca Piccone* |
| As a user, I want to log in so that I can access my saved locations and settings. | *Jincheng* |
| As a user, I want to log out so that I can securely end my session. | *Jincheng* |
| As a user, I want to delete my account so that I can remove my data permanently. | *Jincheng* |
| As a user, I want to change my password so that I can keep my account secure. | *Jincheng* |

### Weather-Related User Stories
| User Story | Responsible |
|-----------|-------------|
| As a user, I want to navigate between different pages of the application so that I can access the features I need.| *Luca Piccone* |
| As a user, I want to search locations by city name so that I can quickly find weather information for that place. | *Kate* |
| As a user, I want to see search suggestions so that I can select accurate city names easily. | *Luca Piccone (API and Backend)* and *Jincheng (Front-end)* |
| As a user, I want to view a place's weather report so that I can understand current conditions. | *Kate* |
| As a user, I want my search converted to longitude and latitude so that the system can retrieve accurate weather data. | *Kate* and *Denise (API)* |
| As a user, I want to save locations to my favourites so that I can access them quickly in the future. | *Luca Piccone* |
| As a user, I want to remove locations from my favourites so that I can manage my saved list. | *Denise* |
| As a user, I want to export the current weather report so that I can share it via Facebook, email, or save it as a PDF/screenshot. | *Liu Can* |
| As a user, I want to see the upcoming weather for the next 6 hours so I can plan my day accordingly. | *Luca Piccone* |
| As a user, I want to see suggested outfits to wear so I can be comfortable when I go outside. | *Jincheng* |

Although specific responsibilities were assigned for each user story, all group members contributed to design discussions, problem-solving, and implementation support throughout the project.
---

## 🔗 External APIs Used

### Google Places Autocomplete API  
Provides search suggestions as the user types.  
Documentation: https://developers.google.com/maps/documentation/places/web-service/place-autocomplete

### OpenWeather Current Weather Data API  
Retrieves real-time weather for a given set of coordinates.  
Documentation: https://openweathermap.org/current

### OpenWeather Geocoding API  
Converts city names into latitude/longitude values used for weather lookup.  
Documentation: https://openweathermap.org/api/geocoding-api

---

## 📸 Screenshots / Demonstrations

### Login Page

### Create Account Page

### Search with Autocomplete

### Weather Report View

### Favourites Page

### Export Weather Feature

### Setting Page

---

## 📦 Dependencies

This application leverages several external libraries to enhance functionality and improve the user experience:

- **FlatLaf** – Delivers a modern and visually polished look-and-feel for all Swing components, ensuring a clean and consistent UI across the application.

- **OpenCSV** – Enables reliable reading and writing of CSV files, supporting features such as account storage, login validation, and exporting weather data.

These libraries help provide a smoother interface and robust data handling throughout the system.

