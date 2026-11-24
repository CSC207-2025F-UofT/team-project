# Story 3: SQLite Game Persistence and Save Management

## Summary

Implements Story 3 so players can save, restore, and manage multiple playthroughs between app launches.

## What this does

- Creates the local SQLite database and shared tables at startup.
- Stores one snapshot per run. The first save accepts an optional name. Saving again keeps the name and updates the timestamp and gameplay state.
- Opens a Saved Games dialog from the existing Load Game buttons. It lists saves newest first, shows the selected run's four stats, and loads that run into the existing game screen and HUD.
- Deletes an individual snapshot after confirmation. The dialog refreshes and shows success, error, and empty-state feedback.

## How it is built

The active game lives in the in-memory DAO. `SaveGameDataAccessInterface` gives the Save, Load, and Manage Saves use cases access to the SQLite DAO. Queries enforce username ownership, and the run ID identifies a snapshot across save and load. The migration adds `save_name` and `saved_at` to `game_saves` while preserving existing data. Deletion retains the run record.

The persistence controller runs SQLite work off the Swing event thread. The presenter publishes feedback and list changes through the persistence ViewModel and uses the existing HUD and navigation models on load.

## How to test

- `mvn clean test` passed all 71 tests with zero failures or errors. Coverage includes schema migration, multiple runs, ownership, selected load, update, and deletion.
- For the desktop check, run `app.Main` in VS Code. Save two named games on separate launches, restart, and confirm both appear under **Load Game**. Load one, update it, and confirm it appears once. Cancel deletion of the other, then confirm deletion and restart to verify it stays removed. The desktop walkthrough remains to be run.

## Team integration

This PR changes Story 3 persistence files and its AppBuilder wiring. Fion's account and authentication files and the other gameplay features are outside the diff for this enhancement. The shared connection provider, users schema, and encoded password representation retain Story 1 compatibility.

The app still uses the temporary Story 3 profile. Story 1 integration will supply the signed-in username and connect the existing title-screen Load button. Inventory (Story 6) and transcripts (Story 10) are outside this PR's scope.

## Review

Member 1 (Zixi).
