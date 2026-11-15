# Sweater Weather
## Team Information
### Team Members 
Ginni Arun Kumar

Sofiia Maksyuta

Zahraa Badran

Rie Campbell

Carmen Teng

## Project Description
Sweater Weather is a desktop wardrobe and outfit recommendation application built in Java. It helps users pick weather-appropriate, stylish outfits directly from their personal closet inventory using real-time local forecast data and customizable aesthetics.

### Domain
Personal Productivity & Lifestyle / Fashion Technology

System Overview & Specification
Sweater Weather allows users to digitally manage their closet, set their personal style preferences, and receive daily outfit recommendations tailored to live local weather conditions.

When a user opens Sweater Weather, the system retrieves current temperature and weather conditions via an external weather API. Based on temperature thresholds (mapped on a scale from cold to warm) and user-defined rules—such as outfit layering constraints and opacity checks (e.g., verifying that see-through tops have a valid base layer underneath)—the application filters the user's closet inventory.

Users can set their top style vibes (e.g., Streetwear, Vintage, Minimalist) and generate either a personalized outfit based on their preferences or a randomized outfit. If a selected style category has fewer than two valid items, the system alerts the user that it cannot style the outfit and provides an alternative suggestion. Users can save outfits for future days, request a regeneration with specific changes, or manage their wardrobe items (adding apparel with image uploads, categories, materials, and colors). All closet state and user preferences are persisted locally in a JSON file format.
 
## User Stories
### User Story 1 (Wardrobe Management & Import): 
As a user, I want to add new clothing items with images, colors, materials, and style tags so that I can maintain a digital inventory of my closet.

### User Story 2 (Weather-Driven Outfit Recommendation): 
As a user, I want Sweater Weather to automatically check my local weather and recommend an outfit with valid layering and warmth levels so that I dress appropriately for the day's temperature. (random feature)

### User Story 3 (Aesthetic & Style Preference Filtering): 
As a user, I want to select my favorite style vibes so that recommended outfits align with my personal aesthetic preferences. (custom feature)

### User Story 4 (Outfit Save & Regeneration): 
As a user, I want to request a new outfit generation or save a generated outfit to a favorites list so that I can plan my outfits ahead of time.

### User Story 5 (Closet Inventory Visualizer): 
As a user, I want to view a visual grid of all items in my wardrobe filtered by category (tops, bottoms, shoes) so that I can inspect my available clothes easily. (just a list of tops, bottoms, shoes..., most worn outfit)

## Use Cases
### Use Case 1: Add Clothing Item
**Primary Actor:** User
**Preconditions:** ?
**Trigger:** The user selects the "Add Item" button.
**Main Success Scenario:**
1. The system displays the form for adding a clothing item.
2. The user selects a category (such as Tops, Bottoms, Shoes, etc.), provides the colour, style tags, and uploads an image for the clothing item.
3. The user submits the form.
4. The system saves the item to the user's closet.
**5. The system displays the new items in the closet under its category.**

**Extensions:**
- **2a.** If the user cancels, the system returns to the closet without adding an item. The use case ends.
- **4a.** If a required field is missing or invalid, the system displays an error and lets the user correct the form and submit it again.

**Success Outcome:** The new clothing item is saved in the user's closet and is visible under the selected category.


### Use Case 2: Generate Random Outfit
**Primary Actor:** User
**Preconditions:** ?
**Trigger:** The user selects the "Randomize" button.
**Main Success Scenario:**
1. The system retrieves the current weather for the user's location.
2. The system identifies which clothing combinations from the user's closet satisfy the current weather conditions.
3. The system randomly selects one of the valid combinations.
4. The system displays the clothing items in the selected outfit.

**Extensions:**
- **1a.** If no weather data is available, the system displays an error message and lets the user try again.
- **2a.** If no such combinations exist, the system displays a message explaining why it could not generate an outfit. The use case ends.

**Success Outcome:** The user sees a randomly selected, weather-appropriate outfit created from items from their closet.


### Use Case 3: Generate Custom Outfit
**Primary Actor:** User
**Preconditions:** ?
**Trigger:** The user selects the "Custom Outfit" option.
**Main Success Scenario:**
1. The system displays the available style options (?)

**Extensions:**
- (?)

**Success Outcome:** The user sees a weather-appropriate outfit from their closet that matches the preferences they selected.


### Use Case 4: Save Outfit
**Primary Actor:** User
**Preconditions:** ?
**Trigger:** The user selects the "Save Outfit" button.
**Main Success Scenario:**
1. The system checks whether the displayed outfit is already in favourites.
2. The system adds the outfit to the user's favourites.
3. The system confirms that the outfit was saved.

**Extensions:**
- **1a.** If the displayed outfit is already in the user's favourites, the system informs the user that it has already been saved. The use case ends.

**Success Outcome:** The outfit appears in the user's favourites.


### Use Case 5: Regenerate Outfit
**Primary Actor:** User
**Preconditions:** ?
**Trigger:** The user selects the "Regenerate Outfit" button.
**Main Success Scenario:**
1. The system looks for another outfit in the user's closet satisfying the same weather and style preferences from the current recommendation.
2. The system selects a different valid outfit.
3. The system displays the new outfit.

**Extensions:**
- **1a.** If there is no valid outfit, the system informs the user and continues displaying the current outfit (?).

**Success Outcome:** The user sees a different outfit that meets the current recommendation criteria.


### Use Case 6: View Closet Inventory
**Primary Actor:** User
**Preconditions:** ?
**Trigger:** The user selects the "My Closet" button.
**Main Success Scenario:**
1. The system displays the items in the user's closet.
2. The user selects a category, such as Tops, Bottoms, or Shoes.
3. The system displays the items in the selected category.

**Extensions:**
- **1a.** If the closet is empty, the system displays a message indicating that there are no clothing items in the closet. The use case ends.
- **3a.** If the selected category is empty, the system displays a message indicating that there are no clothing items in the selected category. The user can select another category.

**Success Outcome:** The user sees the clothing items in their closet in the selected category.

## API Usage
**API Name:** OpenWeatherMap API
**Documentation:*** [OpenWeatherMap API Documentation](https://openweathermap.org/current)

**How it is used in the program:** Sweater Weather uses the OpenWeatherMap API to fetch live weather conditions and current temperatures based on a target city or geographic coordinate. The returned temperature data is mapped to a required warmth scale (ranging from cold -10°C to warm 10°C+) inside an API Gateway adapter, which converts the JSON response into a domain WeatherData entity.
