# Trip Planner & Budgeter

**Team name:** _TBD_

## Team Members

- Amelia Ha
- Emily Tran
- Jadyn Nok Tong Mo
- Tamima Wadageri
- Yi Xuan (Jessica) Su

---

## Project Description

### Domain

Travel and personal finance.

Planning a trip with friends usually means a group chat, a shared spreadsheet and a currency app, none of which talk to each other. Trip Planner & Budgeter puts the whole trip in one place: the group saves up for it together, decides what to do each day, and keeps track of what everyone spends while they are away.

### Specification

A **user** signs up with a username and password. A user can create a **trip** by giving it a name, a destination, a start date, an end date, a home currency (the currency the group thinks in, such as Canadian dollars) and a daily budget. The user who creates a trip becomes its first **member** and can invite other users by username. Each user sees a list of the trips they belong to and can open any of them.

**Before the trip**, each member can set a personal **savings goal** and log **contributions** as they put money aside. The group can see each member's progress and the group's combined progress toward the total of everyone's goals.

**While planning**, members suggest **activity ideas** for specific days of the trip, optionally with an estimated cost. Every member can vote once for each idea, and each day's ideas are ranked by votes so the group can see what is most popular.

**During the trip**, members log **expenses**: what it was, how much, in which currency, which category (such as food, transport or lodging), on which day, and who paid. Amounts paid in another currency are converted into the home currency at the current exchange rate. For any day of the trip, a member can see how much the group spent compared with the daily budget, with a breakdown by category.

---

## User Stories

1. **Create and share a trip.** As a group trip organizer, I want to create a trip and invite my friends to it, so that we can all plan and track the same trip together.
2. **Save up together.** As a trip member, I want to set a personal savings goal and log what I put aside, so that the group can see how close we are to affording the trip.
3. **Log spending in any currency.** As a traveller, I want to log what I spend in whatever currency I paid in, so that the group has one record of spending in our home currency.
4. **Stay on budget each day.** As a traveller, I want to see how much the group spent on a given day compared with our daily budget, broken down by category, so that we know whether we need to cut back.
5. **Decide what to do.** As a trip member, I want to suggest activities for each day and vote on everyone's suggestions, so that we spend our time on what the group wants most.

---

## Use Cases

Each use case follows the course's lightweight format. Screen sketches for every use case, before and after, including the error cases, are in [`docs/ui-sketches/`](docs/ui-sketches/).

Every use case below assumes the user is signed in (signup, login and logout come from the course starter code).

### Use Case 1a: Create a trip

**Primary actor:** A user planning a trip

**Preconditions:**

* The user is signed in.

**Trigger:** The user asks to create a new trip.

**Success outcome:** The trip exists with the user as its only member, and the user is looking at that trip.

**Main success scenario:**

1. The user asks to create a new trip.
2. The user provides a trip name, destination, start date, end date, home currency and daily budget.
3. The system checks the details.
4. The system saves the trip with the user as its first member.
5. The system shows the new trip.

**Extensions:**

* **3a.** A detail is missing.
  1. The system explains which detail is missing and does not create the trip.
* **3b.** The end date is before the start date.
  1. The system explains the problem and does not create the trip.
* **3c.** The daily budget is not a number or is negative.
  1. The system explains the problem and does not create the trip.
* **3d.** The home currency is not a recognized currency.
  1. The system explains the problem and does not create the trip.

### Use Case 1b: Invite a member to a trip

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing it.

**Trigger:** The user asks to invite another user by username.

**Success outcome:** The invited user is a member of the trip and can see the trip in their own list of trips.

**Main success scenario:**

1. The user asks to invite someone and provides their username.
2. The system checks that a user with that username exists and is not already a member.
3. The system adds that user to the trip.
4. The system shows the updated list of members.

**Extensions:**

* **2a.** No user has that username.
  1. The system says there is no user with that username. Nobody is added.
* **2b.** The user is already a member.
  1. The system says they are already a member. Nothing changes.

### Use Case 1c: Open a trip

**Primary actor:** A member of one or more trips

**Preconditions:**

* The user is signed in.

**Trigger:** The user asks to see their trips.

**Success outcome:** The user is looking at the trip they chose, with its members and the ways to plan and track it.

**Main success scenario:**

1. The user asks to see their trips.
2. The system shows every trip the user is a member of.
3. The user chooses one.
4. The system shows that trip.

**Extensions:**

* **2a.** The user is not a member of any trip yet.
  1. The system says there are no trips yet and offers to create one (Use Case 1a).

### Use Case 2a: Set a savings goal

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing it.

**Trigger:** The user asks to set how much they plan to save for the trip.

**Success outcome:** The user's savings goal is recorded, and the progress shown for the user and for the group reflects it.

**Main success scenario:**

1. The user asks to set a savings goal and provides an amount in the trip's home currency.
2. The system checks the amount.
3. The system records the goal.
4. The system shows the user's progress and the group's progress.

**Extensions:**

* **2a.** The amount is not a number, or is zero or negative.
  1. The system explains the problem and keeps the previous goal, if any.
* **3a.** The user already had a goal.
  1. The system replaces it and recalculates both progress figures.

### Use Case 2b: Log a savings contribution

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing it.
* The user has set a savings goal (Use Case 2a).

**Trigger:** The user asks to record money they have put aside for the trip.

**Success outcome:** The contribution is recorded, and the progress shown for the user and for the group includes it.

**Main success scenario:**

1. The user asks to log a contribution and provides an amount and a date.
2. The system checks the amount.
3. The system records the contribution.
4. The system shows the user's progress (saved so far out of their goal) and the group's progress (everyone's savings out of everyone's goals).

**Extensions:**

* **2a.** The amount is not a number, or is zero or negative.
  1. The system explains the problem and records nothing.
* **2b.** The user has not set a savings goal yet.
  1. The system asks the user to set a goal first and records nothing.
* **4a.** The user's savings now reach or pass their goal.
  1. The system tells the user their goal is reached.

### Use Case 3: Log an expense

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing it.

**Trigger:** The user asks to record money that was spent on the trip.

**Success outcome:** The expense is recorded with its amount in the currency it was paid in and in the trip's home currency, and appears in the trip's list of expenses.

**Main success scenario:**

1. The user asks to log an expense and provides a description, amount, currency, category (flight, transport, food, lodging, activity, shopping or other), date, and who paid.
2. The system checks the details.
3. The system works out the amount in the trip's home currency using today's exchange rate.
4. The system records the expense.
5. The system shows the trip's expenses, including the new one with both amounts.

**Extensions:**

* **2a.** The description is missing, or the amount is not a number, or is zero or negative.
  1. The system explains the problem and records nothing.
* **2b.** The date is not within the trip's dates.
  1. The system says the date is not part of this trip and records nothing.
* **2c.** The person who paid is not a member of the trip.
  1. The system explains the problem and records nothing.
* **3a.** The currency is the trip's home currency.
  1. The system uses the amount as it is, with no conversion.
* **3b.** Today's exchange rate cannot be found, for example because there is no internet connection.
  1. The system says it could not get the exchange rate and asks the user to try again. Nothing is recorded.

### Use Case 4a: View the daily budget summary

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing it.

**Trigger:** The user asks to see how spending compares with the daily budget for a day of the trip.

**Success outcome:** The user sees what the group spent that day, what is left of the daily budget, and what the spending was on, by category.

**Main success scenario:**

1. The user asks for the budget summary and chooses a day of the trip.
2. The system adds up that day's expenses in the trip's home currency.
3. The system compares the total with the daily budget.
4. The system shows the amount spent, the amount remaining, and the spending by category.

**Extensions:**

* **2a.** No expenses were logged that day.
  1. The system shows nothing spent and the full budget remaining.
* **3a.** The group spent more than the daily budget.
  1. The system shows how much over budget the group is and makes this stand out.

### Use Case 4b: Change the daily budget

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing the budget summary (Use Case 4a).

**Trigger:** The user asks to change the daily budget.

**Success outcome:** The trip has the new daily budget, and the summary reflects it.

**Main success scenario:**

1. The user asks to change the daily budget and provides the new amount.
2. The system checks the amount.
3. The system saves the new daily budget.
4. The system shows the updated summary.

**Extensions:**

* **2a.** The amount is not a number or is negative.
  1. The system explains the problem and keeps the current budget.

### Use Case 5a: Suggest an activity

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing it.

**Trigger:** The user asks to suggest something to do on a day of the trip.

**Success outcome:** The idea is recorded for that day and is visible to every member, ranked with the other ideas for that day.

**Main success scenario:**

1. The user asks to suggest an activity and provides a day, a name, and optionally an estimated cost.
2. The system checks the details.
3. The system records the idea with no votes.
4. The system shows that day's ideas, ranked.

**Extensions:**

* **2a.** The name is missing, or the estimated cost is not a number or is negative.
  1. The system explains the problem and records nothing.
* **2b.** The day is not within the trip's dates.
  1. The system explains the problem and records nothing.
* **2c.** An idea with the same name already exists for that day.
  1. The system says it was already suggested and records nothing.

### Use Case 5b: Vote on an activity

**Primary actor:** A member of the trip

**Preconditions:**

* The user is a member of the trip and is viewing a day's activity ideas.

**Trigger:** The user asks to vote for an idea.

**Success outcome:** The user's vote is counted once for that idea, and the day's ideas are ranked by number of votes, with ties going to the idea suggested first.

**Main success scenario:**

1. The user asks to vote for an idea.
2. The system records the vote.
3. The system shows that day's ideas, ranked again.

**Extensions:**

* **2a.** The user already voted for this idea.
  1. The system removes their vote instead.
  2. The system shows that day's ideas, ranked again.

---

## Minimal Viable Product (MVP)

All of the use cases above are in the MVP. There are five groups, one per team member, and each member leads one group. Sign up, log in and log out come from the course's starter code and are shared.

| Team Member | MVP Use Cases |
|-------------|---------------|
| | 1. Create a trip (1a), invite a member (1b), open a trip (1c) |
| | 2. Set a savings goal (2a), log a savings contribution (2b) |
| | 3. Log an expense (3) |
| | 4. View the daily budget summary (4a), change the daily budget (4b) |
| | 5. Suggest an activity (5a), vote on an activity (5b) |

---

## Entities

A class diagram is in [`docs/diagrams/entities.puml`](docs/diagrams/entities.puml).

### User
- **Instance variables:** `name: String`, `password: String`
- **Methods:** getters

### Trip
- **Instance variables:** `id`, `name`, `destination`, `startDate: LocalDate`, `endDate: LocalDate`, `homeCurrency` (ISO code such as "CAD"), `dailyBudget: double`, `ownerUsername`, `memberUsernames: List<String>`, `savingsGoals: Map<String, Double>`
- **Methods:** `addMember(username): boolean`, `hasMember(username)`, `containsDate(date)`, `getNumberOfDays()`, `getDates()`, `getTotalBudget()`, `setDailyBudget(amount)`, `setSavingsGoal(username, amount)`, `hasSavingsGoal(username)`, `getSavingsGoal(username)`, `getTotalSavingsGoal()`

### Contribution
- **Instance variables:** `id`, `tripId`, `memberUsername`, `amount: double` (home currency), `date: LocalDate`
- **Methods:** getters

### Expense
- **Instance variables:** `id`, `tripId`, `payerUsername`, `description`, `amount: double`, `currency`, `amountInHomeCurrency: double`, `category: ExpenseCategory`, `date: LocalDate`
- **Methods:** getters
- `ExpenseCategory` is an enum: `FLIGHT, TRANSPORT, FOOD, LODGING, ACTIVITY, SHOPPING, OTHER`

### ActivityIdea
- **Instance variables:** `id`, `tripId`, `day: LocalDate`, `title`, `estimatedCost: double`, `proposedBy`, `createdAt: LocalDateTime`, `voterUsernames: Set<String>`
- **Methods:** `addVote(username)`, `removeVote(username)`, `toggleVote(username)`, `hasVoted(username)`, `getVoteCount()`, and the `BY_RANK` comparator (most votes first, ties broken by earliest suggestion)

All entities check their own inputs (for example, amounts must be positive and currencies must be real ISO codes), so invalid data can never be created.

---

## API Usage

### Frankfurter (exchange rates)

**Documentation:** https://frankfurter.dev

**How it is used in the program:** Converts expenses paid in a foreign currency into the trip's home currency (Use Case 3). The currency dropdowns are filled from the API's list of supported currencies. Free, no API key, rates from the European Central Bank, updated every working day. About 30 major currencies are supported.

- Rate request: `GET https://api.frankfurter.dev/v1/latest?base=CAD&symbols=JPY` returns `{"amount":1.0,"base":"CAD","date":"2026-11-16","rates":{"JPY":111.72}}`
- Currency list: `GET https://api.frankfurter.dev/v1/currencies`
- Code: `data_access.FrankfurterExchangeRateGateway`, used through the `use_case.exchange_rate.ExchangeRateDataAccessInterface` interface, so use cases never depend on the API directly.

**Status:** Endpoints confirmed working on Nov 16, 2026. Run `app.ExchangeRateApiDemo` to see live rates from Java.

### Data persistence

Users are saved to `users.csv` (course starter code). Trips, contributions, expenses and activity ideas are saved to `trips.json` by `data_access.JsonFileTripDataAccessObject`, and loaded again when the app starts. Use cases only see small data access interfaces, so the storage could later be swapped for a database without changing any use case code.

---

## Design Diagrams

### UML Class Diagram

- Entities: [`docs/diagrams/entities.puml`](docs/diagrams/entities.puml)

### Sequence Diagrams

_To be added for each MVP use case._

---

## How to Run

1. Open the project in IntelliJ as a Maven project (JDK 11 or newer, as in the course Quickstart).
2. Optional: run `app.SampleDataSeeder` once to create five sample users (`tamima`, `amelia`, `jadyn`, `jessica`, `emily`, password `password`) and a sample trip.
3. Run `app.Main`.

Tests: `mvn test` · Coverage report: `mvn verify`, then open `target/site/jacoco/index.html` · Style check (Google Java Style, same rules as the course notes): `mvn checkstyle:check` · Auto-format: `mvn spotless:apply`

---

## Notes

### Future extensions
- Standalone currency converter screen (reuses the same exchange rate interface)
- Save flights, transport, restaurants and lodging to the trip with their prices
- Split costs and show who owes whom
- Trip-wide spending charts
- Store data online so members on different laptops share the same trip
