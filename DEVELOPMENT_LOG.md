# Development Log

### October 30, 2026

**Eric**

**Planned:**

- Research suitable APIs for our PC Build Planner.
- Compare eBay Browse API and SerpApi.
- Test eBay API once developer account activation is complete.

**Completed:**

- Compared eBay and SerpApi, including pricing, usage limits, and accessibility.
- Identified potential issues with irrelevant listings, damaged components, and abnormal prices.
- Proposed keyword filtering and price outlier detection algorithms using keyword searches and statistical percentiles.
- Initiated eBay Developer registration; account activation is pending.

### November 17, 2026

**Letao**

**Planned**

- Create the feature/meal-planning branch from the latest main.
- Review the existing DailyMealPlan, PlannedMeal, MealSchedule, MealSlot, and FoodPortion entities.
- Discuss the shared Daily Meals page with teammates, including date selection, matching planned and actual meals by meal slot ID, and responsibilities for each side of the page.
- Define the Create Plan use case, including inputs, successful outputs, validation errors, and the interface needed to save a plan.
- Create the use_case/plan_meal package and begin implementing CreatePlanInputData, starting with a plan for one date.

**Completed:**

- Defined the Create Plan use case, including input data, successful output, validation rules, and storage failure handling.
- Implemented the Create Plan input/output data classes, boundaries, data access interface, and custom data access exception.
- Implemented CreatePlanInteractor to validate input, prevent duplicate plans for the same date, create configurable meal slots, and save the plan together with its schedule.
- Added six unit tests covering successful creation, duplicate dates, missing dates, blank meal slot names, storage read failures, and storage save failures. All six Create Plan unit tests passed.
- Defined the intended behavior of planned-meal replacement and swapping for later implementation.

### November 18, 2026

**Letao**

**Planned**

- Implement the Create Plan controller, state, view model, and presenter.
- Add tests for presenter state updates and notifications.

**Completed:**

- Implemented CreatePlanController to pass user input to the use case.
- Added CreatePlanState and CreatePlanViewModel to store results and notify the UI of state changes.
- Implemented CreatePlanPresenter to handle success and failure results and clear outdated state.
- Added two presenter tests covering state transitions and change notifications
- All eight tests passed locally: two presenter tests and six interactor tests.

---

### Future Daily Entry Template

### [Date]

**[Member Name]**

**Planned:**

- ...

**Completed:**

- ...

### Important Rules

- Each date can contain updates from multiple teammates; repeat the member block under the same date.
- Keep entries chronological and concise.
- Record only actual completed work; do not invent contributions or completion dates.
- Keep general project information in README.md and use this log only for daily updates.
