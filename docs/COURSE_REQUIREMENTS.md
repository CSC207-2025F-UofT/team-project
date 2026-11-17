# Course Requirements Checklist

Source: course requirements and rubric screenshot supplied by Qinli on 2026-11-16.
This is a working summary, not a replacement for the official full rubric.

## Required development practices

- Java program with Swing GUI; a web application is not approved.
- Approximately one significant user story per team member; each member leads and
  writes the majority of at least one use case. Preserve individual contribution.
- Use Pull Requests throughout development, with meaningful reviews and refactoring.
- Attempt Clean Architecture and SOLID; justify design decisions.
- Apply appropriate design patterns; do not add patterns only for their names.
- Write Unit Tests as user stories grow.
- Actually call an external API during application use. Bundled JSON is not enough.
- Demonstrate persistence, including saving and later loading user-created data.
- Participate in labs, meetings, communication, and peer review.

## Rubric reminders

- Clear MVP/User Stories/Use Cases describe user-visible behaviour rather than
  implementation details. Significant user stories should have little overlap.
- Prepare an end-to-end Demo showing each person's story.
- Explain API purpose and actual integration, and demonstrate Save and Load.
- Prepare architecture diagrams and evidence of the Dependency Rule.
- For the Excellent overall-design descriptor, discuss at least two SOLID
  principles, a suitable pattern beyond provided starter implementations, and
  extensibility. Confirm details against the full rubric before presentation.
- Code quality at Excellent or above requires Checkstyle or a similar tool,
  addressed style errors, and evidence of use (for example a representative PR).
- Presentation Delivery at Excellent or above requires all members to speak and
  completion within 15 minutes. Rehearse and prepare for design questions.

## Current gaps

- All individual use cases remain TODOs; only scaffold plumbing is implemented.
- Runtime external API integration is not implemented.
- User-data Save/Load is not implemented.
- Images/cards and shared Entity additions are not implemented.
- Individual use case tests, final diagrams, PR review evidence, and presentation
  materials still need to be produced by the team.

The course User API may satisfy API usage and persistence, as stated in the
course requirements, but its associated-JSON operations need documentation.
TheCocktailDB is a candidate for Details images; the API choice is not hard-coded.
