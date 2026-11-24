# Finance Tracker — Refactoring Notes

This branch is a working refactor of the application, not a parallel demo architecture. The launch class remains `app.FinanceTracker`; it is now the Main Component / composition root.

## Current package responsibilities

- `entity`: persistence-free business objects (`UserProfile`, `Account`, `Transaction`, `Category`, and enums).
- `use_case`: use cases, Input/Output Data, Input/Output Boundaries, and Data Access Interfaces.
- `interface_adapter`: Controllers, Presenters, and View Models.
- `view`: Swing presentation only.
- `frameworks_and_drivers.persistence`: concrete JSON data access implementations.
- `frameworks_and_drivers.currency`: Bank of Canada HTTP client.
- `app.FinanceTracker`: Main Component. Creates concrete gateways, interactors, presenters, controllers, and the View.
- `data/`: mutable JSON application data, kept outside `src/main/java`.

## Launch

Run `app.FinanceTracker`. It schedules application construction on the Swing event-dispatch thread and wires the transaction dashboard flow. It contains no Swing layout or event-handler implementation; those are in `view.FinanceTrackerView`.

## Current transaction dashboard flow

`FinanceTrackerView -> TransactionDashboardController -> TransactionDashboardInputBoundary -> TransactionDashboardInteractor -> TransactionDataAccessInterface -> JsonTransactionDataAccess -> data/UserTransactions.json`

The Interactor calculates totals and creates Transaction entities. The Presenter maps Output Data to the View Model. The Swing View renders state and forwards actions; it does not call HTTP or JSON directly.

The currently usable transaction data store is `data/UserProfiles.json`. The local HTTP server, HTTP client, API-specific transaction DTO, and username-based `TransactionControl_App` were removed because they duplicated the direct JSON transaction path without adding useful application behaviour.

## Domain model

- `UserProfile` represents a person/login profile.
- A profile owns zero or more financial `Account` objects.
- `Account` represents a Cash, Savings, Chequing, or other financial account. It has no password.
- `Transaction` is the single transaction entity. `TransactionType` distinguishes income and expense; optional account/category/currency fields support the richer account-owned transaction use case.
- Monetary values use `BigDecimal`.
- Entities do not read/write JSON, make HTTP requests, access Swing, or touch the filesystem.

## Team use cases

| Owner | Use case | Status |
|---|---|---|
| Binwen | Login | Input/Output Data, boundaries, Interactor, Controller, Presenter, and View Model added. A concrete credential gateway and login View remain. |
| Mashac | Upload Receipt | Input/Output Data, boundaries, Interactor, Controller, Presenter, and View Model added. Concrete receipt storage/scanning and View remain. |
| Will | Create Account | Interactor, Controller, Presenter, View Model, Data Access Interface, and JSON gateway added. It associates a new Account with an existing UserProfile. A dedicated View and authenticated-session integration remain. |
| Gary | Change Currency | Input/Output Data, boundaries, Interactor, Controller, Presenter, View Model, Bank of Canada client, and JSON currency gateway added. A dedicated View remains. |

Each use case owns its own vertical flow. Entities and appropriately focused Data Access Interfaces are shared; the team should not duplicate domain models or persistence code.

## Data storage

- `data/UserProfiles.json` is the current dashboard's preserved working JSON data.
- `data/Profiles.json` is the new canonical profile/account data schema used by `JsonAccountDataAccess`. It starts empty; profile registration/login must populate it.
- The previous parallel `AccountData.json`, `UserData.json`, and `Currencies.json` stores were removed from the refactor branch. They were not connected to the working dashboard flow; the old user file also contained plaintext credentials.
- The empty `Expenditures.json` and `Revenues.json` files and their transaction-control implementation were removed.

## Remaining work / verification

1. Implement Binwen's Login use case and the profile creation path that populates `Profiles.json`; use password hashes, never plaintext.
2. Add the Login and Create Account UI entry points after a profile is authenticated, and wire them in `app.FinanceTracker`.
3. Add a Change Currency View and verify exchange-rate direction with tests, including dates with no Bank of Canada observation.
4. Complete Mashac's receipt storage/scanning flow and make the user confirm extracted details before creating a transaction.
5. Migrate the dashboard's username-keyed transaction JSON to profile/account IDs once Login and Create Account are integrated.
6. Add unit tests for entities, interactors, and JSON gateways; run `mvn test` and checkstyle before merging.

The original `AccountDataBase` branch is unchanged. This branch is intentionally incremental so the team can review the migration without losing the starting point.
