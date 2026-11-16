# NutriTrack — Calorie Calculator & Meal Planner

A Java Swing desktop application developed by a four-person University of Toronto CSC207 team for Fall 2026.

## Project Overview

**NutriTrack** helps users organize meal plans and track nutritional intake. Users can manage a personal food database, plan meals for specific dates, record food they actually consume, and review daily calories and macronutrients (protein, carbohydrates, and fat).

The project emphasizes **Clean Architecture**, **SOLID principles**, maintainable Java code, and collaborative development using GitHub.

**Current status:** Core Entity layer and its JUnit 5 test classes have been implemented. Use cases, the Swing interface, persistence, and external API integration are upcoming work. Passing test results have not yet been confirmed in this README.

## Project Objectives and Features

### Food Database Management

- Add foods with nutritional information per 100 grams.
- Edit and delete food entries.
- Search and select foods for meals.
- Calculate nutrition for specified quantities in grams.
- Eventually import food information from an external nutrition API.

### Daily Meal Planning

- Create and edit planned meals for specific dates.
- Add, remove, and replace food portions within each meal.
- View daily planned calories and macronutrients.
- Configure meal slots instead of relying on hard-coded breakfast, lunch, and dinner categories.
- Add, remove, rename, and reorder meal slots.
- **Planned extension:** Copy an entire day's plan to another date as new entities with new IDs.

### Food Recording and Nutrition History

- Record meals actually consumed, including date and time.
- Correct or remove food portions in recorded meals.
- View daily actual calorie and macronutrient totals.
- Keep recorded consumption separate from planned meals.
- Preserve nutrition snapshots so later food-bank edits do not change previously recorded values.
- Compare planned and actual nutrition in the interface (planned feature).

### Meal Suggestions (Proposed)

- Suggest meals based on preferred ingredients and nutritional targets.
- Allow users to review and adjust suggestions before including them in plans.

The scope and implementation of meal suggestions are subject to team feasibility review.

### Persistence (Planned)

- Save and load foods, plans, schedules, and consumption history.
- Preserve existing entity IDs across application restarts.
- Use local JSON storage, subject to final team agreement.

## Technical Stack

| Component | Technology |
|---|---|
| Programming language | Java |
| User interface | Java Swing |
| Build and dependencies | Maven |
| Unit testing | JUnit 5 |
| Architecture | Clean Architecture |
| Design principles | SOLID |
| Persistence | JSON (planned) |
| External nutrition data | USDA FoodData Central or Open Food Facts (under evaluation) |
| Version control | Git and GitHub |

The current `pom.xml` sets the Java compiler source and target level to 11.

## Architecture

The project separates domain entities, application use cases, interface adapters, and external concerns.

### Implemented Entity Layer

| Entity | Responsibility |
|---|---|
| `NutritionTotals` | Store calorie and macronutrient values; add and scale nutrition |
| `FoodItem` | Store food identity and nutrition per 100 g |
| `FoodPortion` | Store serving quantity and a snapshot of its nutrition |
| `MealSlot` | Identify a user-configurable meal category |
| `MealSchedule` | Manage an ordered collection of meal slots |
| `Meal` (abstract) | Share meal identity, portion management, and nutrition calculations |
| `PlannedMeal` | Represent a meal the user intends to eat |
| `RecordedMeal` | Represent a meal actually consumed at a specified date/time |
| `DailyMealPlan` | Manage planned meals for a date |
| `DailyFoodLog` | Manage recorded meals and total nutrition for a date |

```text
Meal (abstract)
├── PlannedMeal
└── RecordedMeal

DailyMealPlan ── List<PlannedMeal>
DailyFoodLog ── List<RecordedMeal>
MealSchedule ── List<MealSlot>
Meal ── List<FoodPortion>
FoodItem ── NutritionTotals (per 100 g)
FoodPortion ── NutritionTotals (portion snapshot)
```

### Planned Clean Architecture Package Structure

```text
src/main/java/
├── entity/                  # Core domain models and business rules
├── use_case/                # Interactors, boundaries, input/output data, gateways
├── interface_adapter/       # Controllers, presenters, view models
├── view/                    # Java Swing screens and components
├── data_access/             # JSON repositories and API adapters
└── app/                     # Application entry point and dependency wiring
```

The intended flow is **View → Controller → Input Boundary / Interactor → Output Boundary / Presenter → View Model → View**, with interactors accessing persistence through interfaces. Domain entities should not depend on Swing, JSON libraries, or external API clients.

## Proposed Team Responsibilities

Each of the four team members should lead at least one substantial use case and contribute through feature branches and reviewed pull requests. Final assignments remain **TBD**.

| Proposed workstream | Responsibilities | Assigned member |
|---|---|---|
| Food database management | Add, edit, delete, search foods | TBD |
| Daily meal planning | Manage daily plans, planned meals, and schedules | TBD |
| Food recording | Record consumption and calculate actual daily nutrition | TBD |
| History and integration | Retrieve historical logs, present summaries, support integration | TBD |

Shared work includes API integration, JSON persistence, Swing UI consistency, tests, documentation, and the final presentation.

## External API Research

At least one external nutrition-data API is planned.

- **USDA FoodData Central:** Detailed food and nutrient search; evaluate access requirements, documentation, and rate limits.
- **Open Food Facts:** Product-oriented nutrition data; evaluate completeness, documentation, and usage policies.

No final API choice has been confirmed.

## Testing

Ten JUnit 5 test classes have been written for the Entity layer:

```text
src/test/java/entity/
├── NutritionTotalsTest.java
├── FoodItemTest.java
├── FoodPortionTest.java
├── MealSlotTest.java
├── MealScheduleTest.java
├── MealTest.java
├── PlannedMealTest.java
├── RecordedMealTest.java
├── DailyMealPlanTest.java
└── DailyFoodLogTest.java
```

They exercise construction and validation, nutritional calculations, meal inheritance, portion changes, schedule ordering, duplicate-ID checks, and date consistency.

Run the tests from the repository root:

```bash
mvn test
```

Tests can also be run through IntelliJ IDEA. Results should be recorded after they have been verified in the actual repository.

## Development Workflow

1. Update local `main` from GitHub.
2. Create a branch named `feature/<descriptive-name>`.
3. Implement the feature and relevant tests.
4. Commit with a meaningful message and push the branch.
5. Open a pull request into `main` for teammate review.
6. Merge after review and successful checks.

Follow Java conventions and the course's Checkstyle expectations. Do not commit machine-specific IDE settings, generated build artifacts, credentials, or personal agent instruction files.

## Project Timeline and Milestones

| Milestone | Target date | Status |
|---|---|---|
| Confirm project direction and core requirements | November 2026 | Completed |
| Implement core Entity layer | November 2026 | Implemented |
| Write Entity layer JUnit test classes | November 2026 | Implemented; results to verify |
| Define repository interfaces and use-case boundaries | TBD | Next |
| Implement the major use cases | TBD | Not started |
| Build the Java Swing interface | TBD | Not started |
| Integrate a nutrition API | TBD | Not started |
| Implement persistence | TBD | Not started |
| Integration testing and bug fixes | TBD | Not started |
| Final presentation | **December 7, 2026** | Upcoming |

## Demonstration

Screenshots, walkthroughs, and application setup instructions will be added as working UI features become available.

## Project Documentation

- [Development Log](DEVELOPMENT_LOG.md) — Progress and decisions.
- [Team Contract](TeamContract.md) — Team agreements.
- GitHub Issues and Pull Requests — Tasks, discussions, review, and contribution history.

## Useful Links

- **GitHub Repository:** [NutriTrack](https://github.com/Toooooooooony/team-project) (update this URL if the repository is renamed)
- **CSC207 Project Expectations:** [University of Toronto — Project Expectations](https://q.utoronto.ca/courses/444184/pages/project-expectations)

---

This README will be updated as features, ownership, API choices, and deployment instructions are confirmed.
