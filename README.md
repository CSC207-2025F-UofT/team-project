# Sweater Weather
Sweater Weather is a desktop wardrobe and outfit recommendation application built in Java. It helps users pick weather-appropriate, stylish outfits directly from their personal closet inventory using real-time local forecast data and customizable aesthetics.

# Project Domain
Domain: Personal Productivity & Lifestyle / Fashion Technology

System Overview & Specification
Sweater Weather allows users to digitally manage their closet, set their personal style preferences, and receive daily outfit recommendations tailored to live local weather conditions.

When a user opens Sweater Weather, the system retrieves current temperature and weather conditions via an external weather API. Based on temperature thresholds (mapped on a scale from cold to warm) and user-defined rules—such as outfit layering constraints and opacity checks (e.g., verifying that see-through tops have a valid base layer underneath)—the application filters the user's closet inventory.

Users can set their top style vibes (e.g., Streetwear, Vintage, Minimalist) and generate either a personalized outfit based on their preferences or a randomized outfit. If a selected style category has fewer than two valid items, the system alerts the user that it cannot style the outfit and provides an alternative suggestion. Users can save outfits for future days, request a regeneration with specific changes, or manage their wardrobe items (adding apparel with image uploads, categories, materials, and colors). All closet state and user preferences are persisted locally in a JSON file format.
 
# User Stories
User Story 1 (Wardrobe Management & Import): As a user, I want to add new clothing items with images, categories, colors, materials, and style tags so that I can maintain a digital inventory of my closet.

User Story 2 (Weather-Driven Outfit Recommendation): As a user, I want Sweater Weather to automatically check my local weather and recommend an outfit with valid layering and warmth levels so that I dress appropriately for the day's temperature.

User Story 3 (Aesthetic & Style Preference Filtering): As a user, I want to select my favorite style vibes so that recommended outfits align with my personal aesthetic preferences.

User Story 4 (Outfit Save & Regeneration): As a user, I want to request a new outfit generation or save a generated outfit to a favorites list so that I can plan my outfits ahead of time.

User Story 5 (Closet Inventory Visualizer): As a user, I want to view a visual grid of all items in my wardrobe filtered by category (tops, bottoms, shoes) so that I can inspect my available clothes easily.

# API Usage
OpenWeatherMap API
Documentation Link: OpenWeatherMap Current Weather Data API Documentation

Usage in Project: Sweater Weather uses the OpenWeatherMap API to fetch live weather conditions and current temperatures based on a target city or geographic coordinate. The returned temperature data is mapped to a required warmth scale (ranging from cold -10°C to warm 10°C+) inside an API Gateway adapter, which converts the JSON response into a domain WeatherData entity.
