Architecture Overview

The codebase follows Clean Architecture's concentric layer pattern with dependencies pointing inward:

Layers (innermost to outermost):
1. Entity - Core business objects (Player, Scene, ClickableObject)
2. Use Case - Business logic (ClickButtonInteractor, SwitchToGameViewInteractor, etc.)
3. Interface Adapter - Controllers, Presenters, ViewModels, States
4. Frameworks & Drivers - Views (Swing UI), Data Access (File I/O)

Component Roles

Use Cases (/use_case/)

- Contains business logic in Interactor classes
- Defines InputBoundary (what the use case does) and OutputBoundary (how to present results) interfaces
- Uses InputData and OutputData DTOs to transfer data
- Depends only on abstractions, never on concrete implementations

Controllers (/interface_adapter/)

- Receive input from Views
- Convert view data → InputData DTOs
- Call Use Case Interactors

Presenters (/interface_adapter/)

- Implement OutputBoundary interfaces
- Receive OutputData from use cases
- Convert to ViewModel/State format
- Update ViewModels and trigger view changes
- Manage navigation via ViewManagerModel

ViewModels (/interface_adapter/)

- Extend ViewModel<StateType>
- Hold UI constants (labels, button text)
- Manage State objects
- Use PropertyChangeSupport for observer pattern

States (/interface_adapter/)

- Plain data objects holding view state (field values, error messages)

Views (/view/)

- Swing UI components
- Listen to user input and update State
- Call Controller methods on button clicks
- Observe ViewModels via PropertyChangeListener
- Update UI when ViewModel changes

Dependency Flow (Example: Click Button)

User clicks Button 1 → GameView captures event
User clicks → GameView → GameController.clickButton(1)
            ↓
        ClickButtonInputData(1) → ClickButtonInteractor (processes button click)
                                  ↓
                              ClickButtonOutputData("button 1 clicked") → GamePresenter
                                                                          ↓
                                                                      Updates GameState
                                                                      Fires property change
                                                                          ↓
                                                                      GameView displays message

Key Dependencies

- View → Controller (calls methods)
- Controller → Use Case InputBoundary (interface)
- Use Case → OutputBoundary (interface - implemented by Presenter)
- Use Case → DataAccessInterface (interface - implemented by DAO)
- Presenter → ViewModels (updates state)
- View → ViewModels (observes via PropertyChangeListener)

SOLID Principles Applied

- SRP: Each class has one responsibility (Interactor=logic, Controller=input conversion, Presenter=output formatting, View=UI)
- OCP: New views/use cases can be added without modifying existing code
- LSP: Any implementation of interfaces can be substituted
- ISP: Each use case defines only the data access methods it needs
- DIP: Use cases depend on interfaces, not concrete implementations (Interactor → OutputBoundary, not Presenter)

The architecture ensures testability, flexibility (can swap data storage), and maintainability through clear separation of concerns.