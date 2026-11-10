# Trip Planner & Budgeter

**Team name:** _TBD_

## Project Domain

**Domain:** Travel / personal finance

A group trip planner for friends travelling together. Before the trip, each member sets a personal savings goal and logs what they've saved, so the group can see how close it is to affording the trip. While planning, members propose activities for each day and vote on them to build a ranked list. During the trip, members log every expense in whatever currency they paid, and the app converts it to the trip's home currency using live exchange rates and compares each day's spending to a daily budget.

**Specification.** A *user* creates a *trip* with a name, destination, start and end dates, a home currency and a daily budget, then invites other users as members. Each member sets a savings goal and logs *contributions* toward it. Members propose *activity ideas* for specific days of the trip and vote on them; each member can vote once per idea. During the trip, members log *expenses* with an amount, currency, category, date and payer. Expenses in a foreign currency are converted to the home currency with the current exchange rate. For any day of the trip, a member can see the total spent against the daily budget.

## User Stories

1. As a group trip organizer, I want to create a trip and invite my friends, so that we can all plan it together.
2. As a trip member, I want to set a personal savings goal and log what I've saved, so that the group can see how close we are to affording the trip.
3. As a traveller, I want to log each expense in whatever currency I paid, so that the group has one record of what we spent.
4. As a traveller, I want to see how much we've spent on a given day compared to our daily budget, so that I know whether we need to cut back.
5. As a traveller, I want to convert an amount between currencies using current rates, so that I understand what things actually cost.
6. As a trip member, I want to suggest activities and vote on other members' suggestions, so that we choose what the group wants most each day.

## Use Cases

### Use Case 1: Create a trip and invite collaborators

**Main Flow**
1. A logged-in user clicks **New Trip**.
2. The user enters a trip name, destination, start date, end date, home currency and daily budget, then clicks **Create**.
3. The system saves the trip with the user as its first member and opens the trip dashboard.
4. On the dashboard, the user enters another user's username and clicks **Invite**.
5. The system adds that user to the trip's members, and the member list updates.

**Alternative Flows**
- A required field is empty: the system shows an error and does not create the trip.
- The end date is before the start date: the system shows an error.
- The invited username does not exist: the system shows "No user with that username."
- The invited user is already a member: the system shows "Already a member of this trip."

### Use Case 2: Set a savings goal and log contributions

**Main Flow**
1. A member opens the **Savings** view of a trip.
2. The member enters a personal savings goal (in the trip's home currency) and clicks **Set Goal**.
3. The member enters a contribution amount and date and clicks **Log Contribution**.
4. The system records the contribution and updates the member's progress bar and the group's total progress (total contributed / total of all goals).

**Alternative Flows**
- The amount is zero, negative or not a number: the system shows an error.
- The member logs a contribution before setting a goal: the system asks them to set a goal first.
- A member's contributions pass their goal: the contribution is accepted and their progress shows "Goal reached."

### Use Case 3: Log an expense

**Main Flow**
1. On the trip dashboard, a member clicks **Add Expense**.
2. The member enters a description, amount, currency, category (flight, transport, food, lodging, activity, other), date and payer (defaults to themselves), then clicks **Save**.
3. If the currency differs from the trip's home currency, the system gets the current exchange rate and stores the converted amount.
4. The system saves the expense, and the expense list updates.

**Alternative Flows**
- The amount is zero, negative or not a number: the system shows an error.
- The date is outside the trip's dates: the system shows an error.
- The exchange rate cannot be retrieved: the system shows "Couldn't get the exchange rate. Try again." and does not save the expense.

### Use Case 4: View daily budget summary

**Main Flow**
1. A member opens the **Daily Budget** view and selects a day of the trip.
2. The system totals that day's expenses in the home currency and compares the total to the daily budget.
3. The view shows the amount spent, the amount remaining and a breakdown by category.

**Alternative Flows**
- No expenses were logged that day: the view shows 0 spent and the full budget remaining.
- Spending is over the daily budget: the view shows the amount over budget, highlighted in red.

### Use Case 5: Convert currency

**Main Flow**
1. A user opens the **Currency Converter** view.
2. The user enters an amount and chooses a "from" currency and a "to" currency, then clicks **Convert**.
3. The system gets the latest exchange rate from the exchange rate API.
4. The view shows the converted amount and the date of the rate used.

**Alternative Flows**
- Both currencies are the same: the system returns the same amount without calling the API.
- The amount is not a valid number: the system shows an error.
- The API cannot be reached or rejects the currency: the system shows "Couldn't get the exchange rate. Try again."

### Use Case 6: Propose and vote on activities

**Main Flow**
1. A member opens the **Activities** view and selects a day of the trip.
2. The member enters an activity title (and optionally an estimated cost) and clicks **Propose**.
3. The system saves the idea, and it appears in that day's list.
4. Members click **Vote** on ideas they like.
5. The system records each vote and re-sorts the day's list by number of votes (ties: earliest proposed first).

**Alternative Flows**
- The title is empty: the system shows an error.
- An idea with the same title already exists for that day: the system shows "Already proposed."
- A member clicks **Vote** on an idea they already voted for: the system removes their vote.

## MVP

Every use case above is part of the MVP. Each team member takes the lead on one.

| Use Case | Lead |
| --- | --- |
| 1. Create a trip and invite collaborators | |
| 2. Set a savings goal and log contributions | |
| 3. Log an expense | |
| 4. View daily budget summary | |
| 5. Convert currency | |
| 6. Propose and vote on activities | |

Login and signup come from the course's ca-login starter code and are shared by the team.

## Entities

**User**
- Instance variables: `username: String`, `password: String`
- Methods: getters

**Trip**
- Instance variables: `id: String`, `name: String`, `destination: String`, `startDate: LocalDate`, `endDate: LocalDate`, `homeCurrency: String`, `dailyBudget: double`, `memberUsernames: List<String>`, `savingsGoals: Map<String, Double>`
- Methods: `addMember(String username)`, `hasMember(String username): boolean`, `containsDate(LocalDate date): boolean`, `getNumberOfDays(): int`, `setSavingsGoal(String username, double amount)`, `getTotalSavingsGoal(): double`

**Contribution**
- Instance variables: `id: String`, `tripId: String`, `member: String`, `amount: double`, `date: LocalDate`
- Methods: getters

**Expense**
- Instance variables: `id: String`, `tripId: String`, `payer: String`, `description: String`, `amount: double`, `currency: String`, `amountInHomeCurrency: double`, `category: ExpenseCategory`, `date: LocalDate`
- Methods: getters
- `ExpenseCategory` is an enum: `FLIGHT, TRANSPORT, FOOD, LODGING, ACTIVITY, OTHER`

**ActivityIdea**
- Instance variables: `id: String`, `tripId: String`, `day: LocalDate`, `title: String`, `estimatedCost: double`, `proposedBy: String`, `voters: Set<String>`, `createdAt: LocalDateTime`
- Methods: `addVote(String username): boolean`, `removeVote(String username): boolean`, `hasVoted(String username): boolean`, `getVoteCount(): int`

## API Usage

**Frankfurter (exchange rates)**
- Documentation: https://frankfurter.dev
- Free, open source, no API key. Example: `GET https://api.frankfurter.dev/v2/rate/CAD/JPY` returns the current CAD to JPY rate.
- Used by Use Case 5 (Convert currency) and Use Case 3 (converting expenses into the trip's home currency), through a shared exchange rate data access interface.
- Status: not yet called from our Java program.

**Course user/data API (data persistence)**
- Reference: the `DBUserDataAccessObject` class in the course's ca-login repository (`db-user` branch).
- Stores a user and an associated JSON object. We will use it to save users, trips, contributions, expenses and activity ideas so that all members see the same data across devices. Plan to confirm with our TA: storing each trip as its own record keyed by trip id, so every member reads and writes the same record.
- Status: not yet called from our Java program.

## Future Extensions

- Search for activities, flights and lodging through an external API
- Charts beyond the daily summary (spending over the whole trip, by category)
- Splitting costs between members and showing who owes whom
