# Finance Tracker Clean Architecture Redesign

This branch is based on `AccountDataBase`. The existing implementation is retained while the new design is introduced and migrated incrementally.

## Layering (following the course Chapter 11 model)

- `entity`: business entities and enterprise rules only.
- `use_case`: Interactors, Input/Output Data, Input/Output Boundaries, and Data Access Interfaces.
- `interface_adapter`: Controllers, Presenters, and View Models.
- `frameworks_drivers`: Views, concrete JSON/database Data Access implementations, external API clients, and other details.
- `app`: Main component / composition root. It creates concrete objects and injects them through the use-case interfaces.

The dependency rule applies to source-code dependencies: entities know nothing about outer layers; use cases know entities and use-case-owned interfaces; adapters implement/translate those interfaces; frameworks and drivers are replaceable details.

## Shared domain model

- One `UserProfile` represents one person/login identity.
- One `UserProfile` can own zero or more financial `Account` objects.
- `Account` represents a Cash, Chequing, Savings, or other financial account. It is not a login identity and must not contain a username/password.
- Transactions belong to a financial account and use one canonical `Transaction` / `Income` / `Expense` model.
- Currency is a value/setting associated with an account or profile according to the chosen product rule; currency conversion is performed by a use case through a use-case-owned interface, not by an entity reading JSON or calling the network.

## Team use-case ownership

| Owner | Use case | Main responsibility |
|---|---|---|
| Binwen | Login | Authenticate a UserProfile and report success/failure. Password verification and credential lookup are behind the Login use case's Data Access Interface. |
| Mashac | Upload Receipt | Accept a receipt, store it, extract candidate transaction details, and return them for user confirmation. It should not silently create a financial transaction unless that behaviour is explicitly included in the use case. |
| Will | Create Account | Create a financial Account for the currently authenticated UserProfile and associate it with that profile. This does not create another login/profile. |
| Gary | Change Currency | Change the selected account/profile currency using a currency-rate Data Access Interface. Convert the balance consistently and persist the new currency and converted balance together. |

Each use case has its own vertical flow but shares entities and focused Data Access interfaces. No duplicated entity or persistence model per teammate.

## Standard flow for each use case

View -> Controller -> Input Boundary -> Interactor -> Data Access Interface / Entity -> Output Boundary -> Presenter -> View Model -> View.

The Interactor's input-boundary method returns void. It sends an Output Data object to the Output Boundary; the Presenter updates the View Model.

## Migration rules

1. Preserve useful behaviour and public domain concepts where possible, but remove persistence, HTTP, Swing, filesystem, and API calls from entities.
2. Replace the two transaction representations (account-ID based and username based) with one canonical transaction model and one repository/data-access path.
3. Treat the existing username/password fields in the old `Account` as legacy login data. Move identity/credentials to `UserProfile`; financial accounts should not contain passwords.
4. Keep mutable JSON files outside Java source/resources and give each concrete Data Access class one consistent path/configuration.
5. Keep the existing Bank of Canada currency client as an outer-layer implementation if its response parsing and rate direction are verified. The Change Currency Interactor must not depend on the HTTP client.
6. Keep existing receipt-related code if it exists and is useful; otherwise provide an interface boundary for the receipt scanner/storage dependency.
7. Do not delete legacy code until the replacement use case is wired, tested, and the old references have been removed.

## Existing implementation assessment

- The current `Account` and `UserProfile` constructors perform JSON persistence and call outer-layer controls. These need to become plain domain objects.
- The current `Transaction` constructor writes JSON and loads currency data. Transaction creation and persistence must move to the Record/Create Transaction use case and its Data Access Interface.
- The current `SignUpInteractor` imports its Data Access Interface from `interface_adapter`, has no injected Presenter, and reverses the password-match condition. It is not a reliable completed login implementation.
- `FinanceTracker` combines Swing presentation, API calls, server lifecycle, and analytics calculations.
- `ApiTransaction` / `TransactionControl_App` / `UserTransactions.json` are a second transaction schema keyed by username. They should be migrated into the canonical account-owned transaction model, not retained as a second source of truth.
- `CurrencyAPI` is a useful starting point for the Bank of Canada integration, but `CurrencyControl` currently combines rate lookup, JSON persistence, conversion, and account currency storage.
- The existing create/delete transaction flow in the local HTTP API is the closest end-to-end feature, but it is not a complete Clean Architecture use case and currently uses the separate username-based transaction schema. Reuse its user-facing behaviour, not its architecture.

## Safety of migration

This is a redesign branch. The original `AccountDataBase` branch remains unchanged. Migrate in small commits and run the Maven build/tests after each vertical slice.
