## What
<!-- one or two sentences -->

## Why (user story / use case)
<!-- Story and UC number -->

## Architecture check
- [ ] No inward-dependency violations (entity and use_case import no Swing/IO/HTTP)
- [ ] New behavior added by a new class (Open/Closed), not by editing existing logic
- [ ] Interactor depends only on interfaces; wiring only in AppBuilder

## Tests and quality
- [ ] Unit tests added; `mvn test` passes locally and in CI
- [ ] `mvn checkstyle:check` passes
- [ ] Javadoc on all new public classes and methods
- [ ] No live API calls in tests

## Notes for reviewer
Closes #
