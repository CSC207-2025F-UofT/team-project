# UC2 and shared question entities

**Responsible member for the following classes:** Xinjia Shen.

| Class | Used by | Responsibility |
|---|---|---|
| `Question` | UC2 and UC3 | Immutable source ID, question text, options, correct answer and optional explanation; supplies a wrong option for a simple hint |
| `QuestionSession` | UC2 and UC3 | Active answers, answer edits, completeness, correct count and one-time hint use |
| `ChallengeResult` | UC2; consumed by academic progress and saves | Completed correct count, question count and preparation points |

These entities have no Swing, HTTP or JSON dependencies. They do not deduct Energy or save files.
UC2 records preparation points through its state gateway; UC3 owns exam grading and battle rules.

This is a shared entity folder. `ExamBattle` belongs to UC3; player, day and weather entities belong to the
game-state/rest modules. Academic entities are being developed in UC1's branch.
