# Test doubles and fixtures

Tests never call the real WorkoutX API: they use the fakes and saved responses listed here.

## Fakes (Java classes)

### `use_case.search_exercises.FakeExerciseSearch`
- **Stands in for:** `ExerciseSearchDataAccessInterface` (the exercise library).
- **Behaviour:** `FakeExerciseSearch.returning(list)` answers every query with the same exercises. `FakeExerciseSearch.unavailable()` throws `ExerciseLibraryUnavailableException` on every query. Both record each query they receive (`getReceivedQueries()`).
- **Use it to test:** an interactor's success, no-match and unavailable paths; that a search was or wasn't sent (for example, that `CachingExerciseSearch` asks only once for a repeated search).
- **Used by:** `SearchExercisesInteractorTest`, `CachingExerciseSearchTest`.

### `RecordingPresenter` (nested in `SearchExercisesInteractorTest`)
- **Stands in for:** `SearchExercisesOutputBoundary` (the presenter).
- **Behaviour:** stores the last success output or error message, so the test can check what the interactor reported.
- **Use it to test:** what the Search Exercises interactor reports. It's private to that test; copy the idea for other use cases.

## Saved API responses

See [`resources/workoutx/README.md`](resources/workoutx/README.md): real WorkoutX responses for testing the JSON parsing and error handling.
