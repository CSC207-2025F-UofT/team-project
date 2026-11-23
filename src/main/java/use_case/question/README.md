# Shared question contract — UC2 and UC3

**Responsible member:** Xinjia Shen. **Consumers:** UC2 class challenges and UC3 exams.

- `QuestionGateway`: returns exactly the requested number of distinct `Question` objects or throws an error.
- `QuestionTopic`: identifies the six supported course topics, independently of course IDs and display names.
- `QuestionPurpose`: selects PRACTICE or EXAM questions.
- `QuestionLoadException`: reports unavailable or invalid question data.

The current provider supports 1–3 PRACTICE questions and 1–5 EXAM questions per course.
UC2 always requests three. UC3 decides its own exam flow, battle ending, hint eligibility and grades.
There is no offline question fallback.

The HTTP implementation lives in `data_access.question`; this package has no HTTP or JSON dependencies.
