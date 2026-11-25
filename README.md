# Finance Tracker

## Team Information


### Team Members
- Will Hui
- Gary Chang
- Meshac Sandrakanthan
- Binwen Zheng

---

## Project Description

### Domain

This is a program that tracks a user's spending patterns, which includes incomes, expenses and budgeting habits. It also shows various statistics in forms for graphs.

---

## User Stories

### User Story 1
As a user, I want to create an account so that I could track my finanical information and be able to pull it up anytime.

### User Story 2
As a user, I want to seperate my income and my expenses so that I could have a better understanding of my spending habits.

### User Story 3
As a user, I want to visualize the change in my budgeting(account balance) over time.

### User Story 4
As a user, I want be able to change my currency type so I could keep using this app even in different country.

(Add additional user stories as needed.)

---

## Use Cases

### Use Case 1: Register an Account

**Primary actor:** New user

**Preconditions:**
- The user does not already have an account associated with their email address.

**Trigger:** The user requests to create a new account.

**Success outcome:** A new user account is created, allowing the user to access their personal financial information.

**Main success scenario:**
1. The user requests to register an account.
2. The system asks the user to provide their registration information.
3. The user provides their email address, username, and password.
4. The user submits their registration information.
5. The system validates the provided information.
6. The system creates a new user account.
7. The system informs the user that registration was successful.

**Extensions:**
- **3a.** The user provides incomplete or invalid information.
    1. The system explains which information is invalid or missing.
    2. The user is given an opportunity to correct the information.
    3. The use case resumes at step 3.

- **5a.** The email address is already associated with an existing account.
    1. The system informs the user that the email address is already registered.
    2. The system does not create another account.
    3. The use case ends.

---

### Use Case 2: Record a Transaction

**Primary actor:** Registered user

**Preconditions:**
- The user is logged in.
- The user has at least one financial account.

**Trigger:** The user requests to record a new income or expense transaction.

**Success outcome:** The transaction is recorded and the associated account balance is updated accordingly.

**Main success scenario:**
1. The user requests to record a new transaction.
2. The system asks the user to provide the transaction information.
3. The user provides the transaction amount, type (income or expense), date, description, and category.
4. The user submits the transaction information.
5. The system validates the transaction information.
6. The system records the transaction.
7. The system updates the associated account balance based on the transaction type and amount.
8. The system confirms that the transaction was successfully recorded.

**Extensions:**
- **3a.** The user provides an invalid amount or incomplete transaction information.
    1. The system explains which information is invalid or missing.
    2. The user is given an opportunity to correct the information.
    3. The use case resumes at step 3.

- **5a.** The transaction information is invalid.
    1. The system explains why the transaction cannot be recorded.
    2. The system does not record the transaction or modify the account balance.
    3. The use case ends.

- **6a.** The system cannot record the transaction.
    1. The system informs the user that the transaction was unsuccessful.
    2. The account balance remains unchanged.
    3. The use case ends.

---

### Use Case 3: Upload a receipt

**Primary actor:** Registered user

**Preconditions:**
- The user is logged in.

**Trigger:**

**Success outcome:**

**Main success scenario:**
1.
**Extensions:**
- 

---

### Use Case 4: View Financial Statistics

**Primary actor:** Registered user

**Preconditions:**
- The user is logged in.

**Trigger:** The user requests to view their financial statistics.

**Success outcome:** The user is presented with a summary and graphical representation of their financial activity.

**Main success scenario:**
1. The user requests to view their financial statistics.
2. The system asks the user to select a time period.
3. The user selects a time period.
4. The system retrieves the user's financial transactions within the selected period.
5. The system calculates the user's income, expenses, and account balance changes.
6. The system generates graphical representations of the financial statistics.
7. The system displays the financial statistics to the user.

**Extensions:**
- **3a.** The user selects an invalid time period.
    1. The system explains that the selected time period is invalid.
    2. The user is given an opportunity to select another time period.
    3. The use case resumes at step 2.

- **4a.** The user has no transactions within the selected time period.
    1. The system informs the user that there is no financial activity during the selected period.
    2. The system displays an empty statistics view.
    3. The use case ends.

- **5a.** The system cannot retrieve the user's financial information.
    1. The system informs the user that the financial statistics cannot currently be generated.
    2. The use case ends.
       (Add additional use cases as needed.)

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Will Hui | View Financial Statistics |
| Gary Chang | Record a Transaction |
| Meshac Sandrakanthan | Upload a receipt |
| Binwen Zheng | Register an Account |

---

## Entities

### UserProfile
Represents a registered user and stores their personal information and associated financial accounts.

### Account
Represents a financial account belonging to a user. Stores account-related information, including its balance and account type.

### AccountType
Defines the different types of financial accounts supported by the application.

### Transaction
Represents a financial transaction and contains common information such as its amount, date, description, and associated category.

### Income
Represents money received by a user. Extends `Transaction` and represents incoming funds.

### Expense
Represents money spent by a user. Extends `Transaction` and represents outgoing funds.

### Category
Represents a classification used to organize financial transactions, such as Food, Transportation, or Entertainment.

### CategoryType
Defines the different types of transaction categories supported by the application.
(Add additional entities as needed.)

---

## API Usage

**API Name:**

**Documentation:** [Link]

**How it is used in the program:**

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
