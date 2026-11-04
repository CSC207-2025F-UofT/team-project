# UofTears – Project Summary

*Written for: the four team members (and as a reference when talking to the TA). Companion to the README, which is the formal template-based spec.*

---

## 1. The pitch

**UofTears** is a Java Swing survival game. You are a University of Toronto student who has to survive one week (7 days). You track four stats: **Energy, Money, Sanity, Academic Survival**. Each day you move around a small campus map (Residence, Robarts, Bahen, Food Trucks, Exam Hall), pick up and use items, talk to characters, and play short challenges. Real Toronto weather changes how hard everything is. A local database remembers your game and a "transcript" of how you did. An LLM gives the NPCs their voices.

**Win:** finish Day 7 with Academic Survival ≥ 60. **Lose:** Energy, Sanity or Academic Survival hits 0.

### One day, in practice
1. *Start Day:* the app shows today's weather (e.g. "Snow, -8 °C: food trucks closed, challenges ×1.3 harder, walking costs ×1.5 energy").
2. *Navigate:* the player walks to a building. The system checks it's open and that you have the energy.
3. *Act (3 time blocks per day):* pick up an item (free or bought), use an item, talk to an NPC (LLM-voiced), fight the shawarma lady, race for a Robarts seat, or take the midterm.
4. *End of day:* sleep restores some energy, the day's stats are written to the transcript, and the game autosaves.
5. After Day 7 (or a stat hitting 0): report card, letter grade, and the run is archived in the database.

---

## 2. Requirements checklist

| Course requirement | How UofTears meets it |
|---|---|
| Pull Requests throughout | Feature branch + PR per use case; review rotation in §7 |
| Java + Swing GUI (no web app) | One `JFrame`, `CardLayout`, custom views per use case |
| One user story per member | Four user stories, one per member (§6) |
| Clean Architecture + SOLID | entity / use_case / interface_adapter / data_access / view layers; interfaces owned by the use case layer; use cases never call each other (§3) |
| External API | **Open-Meteo** (weather, core) and an **LLM API** (NPC dialogue, with fallback) |
| Data persistence | **Local SQLite database**: accounts, saves, event logs, day summaries, past runs (§5) |
| Everyone leads a use case | See §6 |
| Present and justify design | Design rationale notes in §3, §4, §5 |

---

## 3. Architecture in one page

```
view  →  interface_adapter  →  use_case  →  entity
              (Controller, Presenter,   (Interactor, boundaries,   (Player, GameState,
               ViewModel)                DAO *interfaces*)           Outcome, ...)
data_access implements the use_case DAO interfaces (SQLite, Open-Meteo, LLM, JSON content)
```

Key design decisions (be ready to justify them):

1. **Use cases never call each other.** They cooperate only through shared entities.
2. **Every action produces an `Outcome`** (stat effects, items gained/lost, unlocked locations, time blocks used, message). **`GameState.apply(outcome)`** applies it in one place: clamping, weather scaling of energy loss, event-log entry, end-of-day, win/lose. So Navigate, Fight, Midterm, etc. never duplicate game rules.
3. **Buffs connect members' work without coupling:** items add a `Buff`; challenges ask `GameState.effectiveDifficulty(kind)` (weather difficulty + buffs) and call `consumeBuffs(kind)`.
4. **Game content is data** (`locations.json`, `items.json`, `dialogues.json`, `opponents.json`): new content = no code change (Open/Closed).
5. **Everything external is behind an interface** owned by the use case layer: weather, LLM, database, randomness. Each has a fake for tests (Dependency Inversion).
6. **Timers belong to the view; decisions belong to the interactor.** The Robarts view measures the reaction time; the interactor decides whether it was "great", "ok" or a miss.
7. **Patterns to name in the presentation:** Observer (ViewModel property changes), Dependency Injection / Factory (`AppBuilder`), Strategy (weather → difficulty; challenge kinds), Decorator/Chain (LLM generator with scripted fallback).

---

## 4. External APIs

### 4.1 Weather: Open-Meteo (core API, keyless)

- **Docs:** https://open-meteo.com/en/docs
- **Request (once, at New Game):** 7-day forecast for UofT St. George: latitude 43.6629, longitude -79.3957, `daily=weather_code,temperature_2m_max,temperature_2m_min`, `timezone=America/Toronto`.
- **Mapping (WMO weather code → game condition):**
  - 0–1 → CLEAR; 2–3, 45, 48 → CLOUDY
  - 51–57 (drizzle), 61–67 (rain), 80–82 (showers), 95–99 (thunderstorm) → RAIN
  - 71–77, 85–86 → SNOW
  - Max temp > 30 °C or min temp < -15 °C → EXTREME_TEMP (overrides)
- **Game effects:** Difficulty multiplier (0.9 / 1.0 / 1.15 / 1.3 / 1.3), energy-loss multiplier (1.0 / 1.0 / 1.2 / 1.5 / 1.5), food trucks closed in SNOW / EXTREME_TEMP.
- **Design:** `WeatherDataAccessInterface.getForecast(days)` in the use case layer. `OpenMeteoWeatherDataAccessObject` (OkHttp + org.json) implements it. `FallbackWeatherDataAccessObject` produces seeded pseudo-weather if the call fails (the player sees a notice). The forecast is **stored in the save** so loading a game gives the same weather.
- **Owner:** Member 1. **Status:** not yet called from Java: first task.

### 4.2 LLM: NPC voices (second API, never required for the game to run)

**What the LLM does (and does not do)**
- *Does:* writes in-character replies for NPCs (shawarma lady, stressed TA, later Duo Owl) given the NPC's persona, today's weather, a short stats summary and the dialogue option the player chose. Example: the lady, on a snowy day with a broke, exhausted player, comments on it in her own voice.
- *Does not:* decide stats, items or outcomes. The authored dialogue tree and the interactor stay deterministic and testable. The LLM only produces **flavour text** (≤ 40 words).
- *Stretch:* "type your haggle pitch" free text. The LLM returns structured JSON, `{"persuasion": 0-10}`; the interactor clamps it and maps it to outcomes by threshold, so the model cannot hand out arbitrary rewards.
- *Stretch:* end-of-run "professor's comments" on the report card (uses the same generator interface).

**Design**
- `NpcDialogueGeneratorInterface.generate(persona, context, playerChoice) → NpcReply` in the use case layer.
- Implementations: `LlmNpcDialogueGenerator` (HTTP + JSON), `ScriptedNpcDialogueGenerator` (canned lines from `dialogues.json`), and `FallbackNpcDialogueGenerator` (try the LLM with a ~4 s timeout, then fall back to scripted). So the game works with no key and no internet, and grading/demo never depends on the network.
- The prompt asks for JSON (`{"reply": "..."}`); the parser validates length and shape and falls back on anything unexpected.
- **UI:** the call runs in a `SwingWorker` with a "…" typing indicator so the window never freezes.
- **Keys:** read from an environment variable or a git-ignored `.env`; never committed. Each developer uses their own key.
- **Testing:** unit tests use a fake generator and canned JSON for the parser. One manual test class (excluded from `mvn test`) calls the real API.
- **Provider:** to be decided as a team: choose one that all four can get a free or cheap key for (e.g. an OpenRouter-style endpoint like MapApp404 used, or the Anthropic API with a small model). Because of the interface, switching later is one class.
- **Owner:** Member 3.
- **Trade-off to be honest about:** two APIs means more moving parts. Weather is the guaranteed requirement-meeting API; the LLM is the "wow" feature. If time runs short, ship with the scripted generator only and the weather API still satisfies the requirement.

---

## 5. Persistence: local SQLite database

**Choice:** SQLite through JDBC (`org.xerial:sqlite-jdbc`), file `data/uofteart.db` (git-ignored), created automatically on first run by a `SchemaInitializer`. Plain `PreparedStatement`s, no ORM. Everything is local, so no network dependency. (The course user API from `DBUserDataAccessObject` is intentionally not used.)

**Tables (first draft)**

| Table | Key columns | Purpose |
|---|---|---|
| `users` | `username` (PK), `password_hash`, `salt` | Local accounts (separate saves per player on one machine) |
| `runs` | `run_id` (PK), `username`, `status` (IN_PROGRESS / WON / LOST), `days_survived`, final stats, `grade_letter`, `started_at`, `finished_at` | One row per game; history of past runs |
| `game_saves` | `run_id` (PK/FK), `day`, `time_blocks_left`, `energy`, `sanity`, `academic`, `money`, `current_location_id`, `forecast_json`, `buffs_json`, `unlocked_json`, `picked_up_today_json` | The one live save of a run |
| `inventory_items` | `run_id`, `slot`, `item_id` | Inventory contents |
| `event_log` | `id`, `run_id`, `day`, `source`, `message`, 4 stat deltas | What happened and why (challenge results, fights, items used) |
| `day_summaries` | `run_id`, `day`, `weather`, end-of-day stats | One transcript line per day |

**Behaviour**
- `GameState` keeps `eventLog` and `daySummaries` in memory (appended by `apply(outcome)` and `startNextDay`). The save use case writes the whole state in **one transaction** (replace the run's rows). That keeps use cases free of persistence logic.
- Saves happen on the Save button, automatically at the end of each day, and when the run ends (`runs` row finalised).
- **Transcript** = `day_summaries` + `event_log` for a run, plus a final report card (letter grade from Academic Survival). The title screen can list **past runs** from `runs`.
- **Interfaces (Interface Segregation):** `UserDataAccessInterface`, `SaveGameDataAccessInterface`, `TranscriptDataAccessInterface`. One `SqliteGameDataAccessObject` implements the save and transcript interfaces; `SqliteUserDataAccessObject` implements the account ones; both use a shared `SqliteConnectionProvider`.
- **Passwords:** salted hash (SHA-256 + salt is fine for a course project; note this in the README).
- **Testing:** DAO tests use the in-memory database `jdbc:sqlite::memory:`: save → load round trip, transaction rollback on failure, saving does not delete past runs, transcript queries. Interactor tests use in-memory fakes.
- **Owner:** Member 2 (schema, connection, game/transcript DAOs); Member 4 (user DAO, uses M2's connection). Agree the schema in Phase 0.

---

## 6. Per-person breakdown

Each lead writes, for their use cases: the interactor + boundaries + input/output data, the controller, the presenter, the state and view model, the Swing view, and the unit tests.

### Member 1: Environment & Navigation (User Story 1)

**Use cases:** UC1 Navigate to a Location; UC1b Start Day.

**What the player experiences:** a day banner with the real weather; a map of five buildings with closed ones greyed out and a reason ("Closed: snow"); travel that costs energy; a location screen listing what's available.

**Builds**
- Entities (merge first): `Player`, `StatEffects`, `Weather`, `WeatherCondition`, `Location`, `Outcome`, `GameState`, `GameStatus`.
- Interactors: `StartDayInteractor` (forecast → today's weather; reset time blocks; sleep; Day 7 win/lose), `NavigateInteractor` (open day? weather closure? unlocked? enough energy? deduct cost).
- Data access: `OpenMeteoWeatherDataAccessObject`, `FallbackWeatherDataAccessObject`, `JsonLocationDataAccessObject` + `locations.json`, `InMemoryGameStateDataAccessObject`.
- Views: `MapView`, `LocationView`, `ResultView` (result / win / game over), `StatsPanel`, `ViewManager`; `AppBuilder`, `Main`.
- Skeleton PR (first merge): entities, `GameState`, view manager, `AppBuilder`, empty `Main`.

**Tests:** every weather mapping; API failure → fallback; each closure rule; insufficient energy; energy hits 0 → game over; Day 7 WON/LOST.

**Depends on:** nobody. **Overlap:** pickups with M2; unlocks with M4.

### Member 2: Inventory, Persistence & Transcript (User Story 2)

**Use cases:** UC2 Collect/Buy Item; UC3 Use Item; UC8 Save & Load; UC10 View Transcript.

**What the player experiences:** items to pick up or buy; a side panel with Use buttons; Save/Load; at the end of the run a report card with a day-by-day table and a letter grade; a list of past runs on the title screen.

**Builds**
- Entities: `Item`, `Buff`, `Inventory`, `EventRecord`, `DaySummary`, `RunSummary` (agree with others in Phase 0).
- Interactors: `CollectItemInteractor` (available today? money? capacity ≤ 5?), `UseItemInteractor` (instant effect vs buff; reject duplicate active buff), `SaveGameInteractor`/`LoadGameInteractor`, `ViewTranscriptInteractor` (assembles the report card and archives the finished run).
- Data access: `SchemaInitializer`, `SqliteConnectionProvider`, `SqliteGameDataAccessObject` (save + transcript), `JsonItemDataAccessObject` + `items.json`.
- Views: `InventoryPanel` (on every in-game screen), item buttons in `LocationView` (with M1), `TranscriptView`, Save/Load buttons.
- Items: Coffee (Energy +20, Sanity -5), Protein Bar (Energy +10), Duo Plushie (Sanity +15), Earplugs (race difficulty -0.2), Cheat Sheet (memory difficulty -0.2), Shawarma Coupon (fight difficulty -0.25).

**Tests:** every item; full inventory; no money; already taken today; DAO round trip on in-memory SQLite; save never deletes past runs; transaction rollback; transcript contents; grade letter boundaries.

**Depends on:** M1 skeleton; M4 login for the username. **Overlap:** pickups with M1; NPC rewards/thefts with M3; user table/connection with M4.
**If this is too heavy:** make Use Item or the event-log detail a stretch.

### Member 3: NPC Interactions, Combat & LLM (User Story 3)

**Use cases:** UC4 Talk to an NPC; UC5 Fight.

**What the player experiences:** speaking to the shawarma lady and the stressed TA with branching options, in-character LLM replies, and a three-round haggling fight that uses buffs and the day's difficulty.

**Builds**
- Entities: `NPC`, `DialogueNode`, `DialogueOption`.
- Interactors: `TalkToNpcInteractor` (`start(npcId)`, `choose(optionId)`; next node, final `Outcome`, or "start fight"; uses a time block), `FightInteractor` (`playRound(move)`; 3 rounds; ties favour the opponent when effective difficulty > 1.1; consumes FIGHT buffs; builds the `Outcome` at the end).
- Data access / external: `JsonDialogueDataAccessObject` + `dialogues.json`, `JsonOpponentDataAccessObject` + `opponents.json`, `NpcDialogueGeneratorInterface` with `LlmNpcDialogueGenerator`, `ScriptedNpcDialogueGenerator`, `FallbackNpcDialogueGenerator`.
- Views: `DialogueView` (with typing indicator), `FightView`.
- Content minimum: Shawarma Lady and Stressed TA. Stretch: Duo Owl, free-text haggling.

**Tests:** every dialogue path; no time blocks left; fight win/lose/tie at each difficulty; buff reduces difficulty; weather-scaled energy loss on defeat; LLM parser with canned good and bad JSON; fallback triggers on timeout/error.

**Depends on:** M1 entities; M2 for item rewards; M4 for the shared difficulty rule. **Overlap:** rewards with M2; difficulty with M4.

### Member 4: Challenges & Accounts (User Story 4)

**Use cases:** UC6 Robarts Study Spot; UC7 Midterm from Hell; UC9 Sign Up / Log In.

**What the player experiences:** a reaction race ("Scanning for seats…" → green button → click; clicking early is a false start, "snooze you lose"), a memory test that shows a symbol sequence briefly and asks you to re-enter it, and a login/title screen.

**Builds**
- Interactors: `StudySpotInteractor` (`prepare()` returns random delay + difficulty-scaled thresholds; `submit(reactionMillis, falseStart)`), `MidtermInteractor` (`prepare()` generates the sequence; length 5, +1 if difficulty > 1.1, +2 if > 1.25; `submit(answer)` scores positions; ≥ 80% unlocks the Bahen lab), login/signup interactors (adapted from the course lab).
- Data access: `SqliteUserDataAccessObject` (salted password hash; uses M2's connection provider).
- Views: `StudySpotView` (random-delay `javax.swing.Timer`, green button, elapsed time), `MidtermView` (countdown, 1–9 buttons), `LoginView`, `SignupView`, `TitleView` (New Game / Load Game / Past Runs / Log out).

**Tests:** great/ok/miss thresholds at each difficulty and with buffs; false start; sequence length per difficulty; perfect/partial/fail/incomplete answer; unlock on pass; login/signup success and failure.

**Depends on:** M1 entities; M2 for Load Game / Past Runs on the title screen. **Overlap:** unlocks with M1; difficulty and buffs with M3; user table with M2.

---

## 7. How the work overlaps, and how we review

| Pair | Shared point | Agree in Phase 0 |
|---|---|---|
| M1 ↔ M2 | Items at locations | `Location.pickupItemIds`, `shopPrices` shape; where Collect buttons go |
| M2 ↔ M3 | NPC rewards / thefts | `Outcome.itemsGained/itemsLost`; inventory capacity rule |
| M3 ↔ M4 | Difficulty and buffs | `effectiveDifficulty(kind)`, `consumeBuffs(kind)` |
| M4 ↔ M1 | Unlocks | `Outcome.unlockedLocationIds`, `Location.openDays`, Exam Hall / Bahen lab rules |
| M2 ↔ M4 | User/save tables | One schema, one connection provider |

**Review rotation:** M1 reviews M2, M2 reviews M3, M3 reviews M4, M4 reviews M1, plus anyone whose overlap point the PR touches.

### Phase 0: one meeting before code
Write down in the README: entity APIs (`apply(Outcome)`, `effectiveDifficulty`, `consumeBuffs`, `GameStatus`), `GameStateDataAccessInterface`, `RandomProvider`, base ViewModel classes, JSON content shapes, the SQLite schema, branch naming and PR template. **Member 1 merges the skeleton first**; everyone else tests against fakes until views exist.

---

## 8. Order of work

1. Team contract; README review; TA approval of the idea (mention: LLM + weather + local SQLite).
2. Phase 0 meeting; skeleton PR.
3. Proofs of concept: M1 Open-Meteo call; M3 LLM call; M2 SQLite connection and schema.
4. Each lead: interactor + tests against fakes, then view.
5. First integration: login → new game → travel → pick up item → talk to NPC → play a challenge → end day.
6. Save/load, transcript, past runs.
7. Polish; tests to full coverage; README final; rehearse the presentation.
8. Stretch (after step 5 is merged): Duo Owl, free-text haggling, LLM professor comments, extra content, sound.

## 9. Risks and mitigations

| Risk | Mitigation |
|---|---|
| Weather API down at demo | Fallback weather + notice; forecast stored in the save |
| LLM down, slow, or no key | Fallback generator to scripted lines; 4 s timeout; never required |
| LLM output inconsistent | Flavour text only; strict parse; length cap; outcomes stay deterministic |
| Merge conflicts on `GameState` | Skeleton PR first; agree the contract in Phase 0; small PRs |
| Uneven workload | M2 has the most interactors but the smallest ones; Use Item / event detail are cut-lines; M3 Duo Owl is stretch |
| Content scope creep | Fixed list: 5 locations, 6 items, 2–3 NPCs, 3 challenge types |
| SQLite setup problems | Early proof of concept; in-memory DB for tests; file-based fallback is not needed because SQLite is already the fallback |

## 10. To confirm with the TA

- That a local SQLite database plus Open-Meteo and an LLM API is acceptable for the persistence and API requirements.
- Which Java version and build setup the course expects.
- Whether committing the game's content JSON files (not keys) is fine, and how graders should supply an LLM key (or whether the scripted fallback is enough for them).

## 11. Presentation split (draft)

| Part | Presenter |
|---|---|
| Idea, user stories, architecture overview | M1 |
| Items, persistence, transcript (live save/load demo) | M2 |
| NPCs, combat, LLM design and fallback demo | M3 |
| Challenges, login, testing and coverage | M4 |
| Demo order | Login → weather day → navigate → item → NPC (LLM) → challenge → end day → report card |
