# Story 3 persistence

The app creates `data/uofteart.db` relative to its working directory at startup. For tests or an alternative location, construct `SqliteConnectionProvider(Path)`. Run `new SchemaInitializer(provider).initialize()` before using the DAOs. Initialization is safe to repeat. Each DAO calls `provider.openConnection()` in a try-with-resources block and closes the connection, statements, and result sets it opens. Each new connection enforces foreign keys.

## Account integration

| Column in `users` | SQLite definition | Purpose |
|---|---|---|
| `username` | `TEXT PRIMARY KEY NOT NULL` | Account key referenced by `runs.username` |
| `password_hash` | `TEXT NOT NULL` | Story 1's complete `iterations:base64Salt:base64Hash` string |
| `salt` | nullable `TEXT` | Optional separate salt under the README schema. The current account branch already embeds it in the hash string. |

Give the SQLite account DAO the same provider as the game DAO. It can insert and read the username and encoded hash using prepared statements. Once Story 1 is wired in, remove the temporary profile bootstrap in `Main`, pass `userSession::getCurrentUsername` to the persistence controller, and connect the account branch's existing Load button to `PersistenceController.load()`. The button is currently disabled and has no controller hook on that branch. Clear the active in-memory game when logging out to prevent a later session from inheriting it.

The current app uses the fixed `__story3_demo__` profile from startup to exit. Its empty hash is a placeholder for a profile without login credentials.

## Save and load

Each New Game gets a new run ID. Save writes that run and its snapshot in a single transaction. The snapshot stores the day, four stats, current location, name, and last-saved time. An omitted name defaults to `Game` followed by the eight-character run ID prefix. Saving the same run updates its snapshot and keeps its name. The picker sorts by last-saved time, with the existing save order breaking ties. Loading a selection restores its run ID, replaces the live in-memory game, and refreshes the HUD.

The additive migration gives older saves a default display name. Their last-saved time appears as **Unknown (older save)** until they are saved again. Older saves are ordered by their original save order below timestamped saves.

Deleting a save removes only the selected account's snapshot. The run record stays available for future transcript work. If the deleted run is still active, the next Save prompts for an optional name and recreates its snapshot with the same run ID.

## Demo in VS Code

1. Open the repository root using JDK 17 or newer. Run `mvn clean test` in the integrated terminal. Open `src/main/java/app/Main.java` and click **Run** above `main`.
2. Click **New Game**. Talk to the Shawarma Lady, choose a response, go back, and save as **First Run**. Once the app confirms the save, close it.
3. Run `Main.java` again from the same working directory. Click **New Game**, choose a different conversation response, and save as **Second Run**.
4. Close and restart the app. Click **Load Game** and check that both runs appear, with their day and last-saved times. Select **First Run**, compare its four-stat preview to the stats after loading, change a stat through another conversation, and save again. Check that **First Run** appears once at the top with its updated stats.
5. Select **Second Run** and click **Delete**. Choose **No** and check that it stays listed. Repeat and choose **Yes**. Confirm only **First Run** remains, then restart and check the list again.
