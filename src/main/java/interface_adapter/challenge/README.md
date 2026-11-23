# UC2 interface adapters

**Responsible member:** Xinjia Shen.

- `ChallengeController`: converts view actions to UC2 input data and presents expected errors.
- `ChallengePresenter`: converts use-case output to display text and view state.
- `ChallengeViewModel`: holds plain immutable UI state and notifies listeners when it changes.

These adapters connect `view.ClassChallengeView` to `use_case.challenge`.
The view model contains no Swing controls. Energy spending, scoring and completion rules belong to the
interactor and shared-state gateway, rather than the controller or presenter.
