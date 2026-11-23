# UC2 application wiring

**Responsible member:** Xinjia Shen.

This folder is shared by the team. UC2 contributes:

- `ClassChallengeFactory`: connects the UC2 interactor, presenter, controller, view model and Swing page.
  The caller supplies the shared game-state adapter and the callback to return to Street.
- `QuestionApiCheck`: an optional command-line check of real Hugging Face requests for all six courses.

`Main` is the team's application entry point and composition root. Its `showGame` method creates the
shared state and navigation. It can supply the real UC2 page to the four-argument `GameView` constructor
once the shared Energy and academic-progress APIs are ready.

Application wiring constructs dependencies; class-challenge rules remain in `use_case.challenge`.
