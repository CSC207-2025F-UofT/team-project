# Hugging Face question adapter — UC2 and UC3

**Responsible member:** Xinjia Shen.

- `HuggingFaceQuestionGateway`: implements the shared question interface and validates remote MMLU rows.
- `JdkQuestionHttpClient`: performs HTTP GET requests with five-second timeouts and checks the response status.
- `QuestionHttpClient`: allows the transport to be replaced in deterministic tests.
- `MmluSelection`: maps six course topics to remote subjects and row references for each purpose.

Only row references are stored here. Question text and answers are loaded from Hugging Face on every call.
PRACTICE and EXAM question IDs are separate. Network, HTTP and invalid-data failures throw
`QuestionLoadException`; there is no local question bank or cache.

Provider JSON stays in this package and is converted to domain `Question` objects before reaching a use case.
MMLU has no difficulty parameter; the selected rows cover basic course concepts.
