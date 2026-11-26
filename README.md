
# NutriTrack

## Team Information

### Team Members

- Eric Wei
- Letao Li
- Pangbo Li
- Tony Wu

---

## Project Description

### Domain

NutriTrack is a Java-based desktop application designed to help users manage their food intake, plan meals, and monitor daily nutritional consumption.

The application provides a centralized food bank where users can add, view, search, edit, and delete food items, each containing nutritional information such as calories, protein, carbohydrates, and fat per 100 grams.

Users can create personalized daily meal plans based on their dietary preferences and nutritional goals. The application also supports recording consumed meals, calculating daily nutritional totals, and reviewing historical food intake.

NutriTrack aims to simplify meal planning and calorie tracking by combining food management, meal scheduling, and nutritional analysis in one user-friendly application.

The application is developed using Java Swing and follows Clean Architecture and SOLID design principles to support modularity, maintainability, and future extensions.

---

## User Stories

### User Story 1: Food Bank Management

As a user, I want to add, view, search, edit, and delete food items in my personal food bank, so that I can maintain an organized collection of foods with accurate nutritional information.

Each food item includes its name, calories, protein, carbohydrates, and fat per 100 grams.

### User Story 2: Automatic Nutrition Lookup

As a user, I want to search for foods using an external nutritional database and automatically fill in their nutritional information, so that I can add new foods conveniently without entering every value manually.

I also want to review and modify the retrieved information before saving it to my personal food bank.

---

## Use Cases

### Food Bank Management

The Food Bank Management feature consists of five independent use cases:

1. **Add Food:** Create and save a food item with nutritional information per 100g.
2. **View Food Bank:** Retrieve and display all stored foods.
3. **Search Food:** Find saved foods using case-insensitive name matching.
4. **Edit Food:** Update an existing food item's name or nutritional information.
5. **Delete Food:** Remove a selected food item after confirmation.

Each use case follows Clean Architecture with its own interactor and interfaces where appropriate. All use cases share the same underlying food repository.

### External Nutrition Lookup (Planned)

Allow users to retrieve food information from an external nutrition API, review or adjust the results, and save selected foods through the existing Add Food use case.

---

## Minimal Viable Product (MVP)

The MVP of NutriTrack focuses on food bank management, meal planning, and nutritional tracking. Each team member is responsible for implementing their assigned use cases following Clean Architecture principles.

| Team Member | User Story | Use Cases | Description |
|-------------|------------|-----------|-------------|
| Eric Wei | To be confirmed | To be confirmed | To be confirmed |
| Tony Wu | Food Bank Management | Add Food, View Food Bank, Search Food, Edit Food, Delete Food | Allow users to create, view, search, update, and remove food entries containing nutritional information per 100g. |
| Letao Li | To be confirmed | To be confirmed | To be confirmed |
| Pangbo Li | To be confirmed | To be confirmed | To be confirmed |

### Implementation Status

| Use Case | Status |
|----------|--------|
| Add Food | Implemented |
| View Food Bank | Implemented |
| Search Food | In Development |
| Edit Food | Planned |
| Delete Food | Planned |

### Current Limitations

- Food data is currently stored in memory and is lost when the application closes.
- Persistent JSON storage will be implemented after the team finalizes the storage design.
- External nutritional database lookup is planned as a future extension.

---

## Entities

### Entity 1: FoodItem

`FoodItem` represents an individual food stored in the user's personal food bank.

Each food has a unique identifier, a name, and nutritional information standardized per 100 grams.

**Attributes:**
- `id` (`String`): Unique identifier for the food, generated using UUID.
- `name` (`String`): Name of the food.
- `nutritionPer100g` (`NutritionTotals`): Nutritional values per 100 grams.

**Responsibilities:**
- Store and validate a food's identity and nutritional information.
- Calculate nutritional values for a specified portion size in grams.
- Provide immutable food data to the application's use cases.

### Entity 2: NutritionTotals

`NutritionTotals` represents nutritional information, including calories and macronutrients.

It is used by `FoodItem` to represent nutritional values per 100 grams and can also represent calculated nutritional totals for food portions and meals.

**Attributes:**
- `calories` (`double`): Energy measured in kilocalories (kcal).
- `protein` (`double`): Protein measured in grams.
- `carbs` (`double`): Carbohydrates measured in grams.
- `fat` (`double`): Fat measured in grams.

**Responsibilities:**
- Store immutable nutritional information.
- Validate that nutritional values are finite and nonnegative.
- Support nutritional calculations through addition and scaling.
- Provide reusable nutritional data across food management, meal planning, and food logging.

### Entity Relationships

- Each `FoodItem` contains one `NutritionTotals` object representing nutrition per 100g.
- Multiple `FoodItem` objects are stored in the food repository and identified by their UUIDs.
- Food management use cases retrieve, create, update, or delete food entries through data access interfaces.

---

## API Usage

### External Nutrition Database API (Planned)

**API Name:** To be determined (potential options include USDA FoodData Central or Open Food Facts).

**Documentation:**
- USDA FoodData Central: https://fdc.nal.usda.gov/api-guide
- Open Food Facts: https://openfoodfacts.github.io/openfoodfacts-server/api/

**How it is used in the program:**

The external nutrition API will allow users to search for foods and retrieve nutritional information without manually entering every value.

The planned workflow is:

1. The user selects **Add Food → Auto-fill from Database**.
2. The user enters a food name in the search field.
3. The application sends a request to the external nutrition API.
4. Matching food items and their nutritional information are displayed.
5. The user selects a food and reviews its nutritional values.
6. The user may modify the retrieved information before saving.
7. The selected food is added to the local Food Bank through the existing Add Food use case.

The application will standardize nutritional information to values per 100 grams, including calories, protein, carbohydrates, and fat.

API integration will be implemented as a separate use case following Clean Architecture principles.

**Current Status:** Not implemented. The application currently supports manually entered foods stored in an in-memory repository.

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
