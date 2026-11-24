# WorkoutX test fixtures

Saved responses from the WorkoutX API (v1), so tests can check parsing and error handling without network access and without spending the API key's request quota (the Free plan allows 500 requests in total).

All files except `search-no-results.json` are real responses, captured on 2026-11-23 with a Free-plan key. None of them contains an API key.

## Files

### `search-name-bench.json`
- **Request:** `GET /v1/exercises/name/bench`
- **Contents:** `{ "total": 73, "count": 10, "data": [ ...10 exercises... ] }`. The first page of a name search, capped at 10 results by the Free plan.
- **Use it to test:** parsing a normal page of results; that `total` (all matches) and `count` (this page) are read separately; mixed body parts (Upper Legs and Chest appear in the same results).
- **Known values:** `data[1]` is `0025`, "Barbell Bench Press", body part `Chest`, equipment `Barbell`, mechanic `compound`.

### `search-chest-barbell-press.json`
- **Request:** `GET /v1/exercises?name=press&bodyPart=chest&equipment=barbell&limit=10`
- **Contents:** `{ "total": 9, "count": 9, "data": [ ...9 exercises... ] }`. All nine are Chest / Barbell / compound.
- **Use it to test:** the main case of the Exercise Library screen (name, muscle and equipment filters combined); that every field the screen shows is parsed: `name`, `bodyPart`, `target`, `secondaryMuscles`, `equipment`, `mechanic`, `instructions`, `gifUrl`.
- **Known values:** `data[0]` is `0025`, "Barbell Bench Press", target `Pectorals`, secondary muscles `Triceps` and `Shoulders`, 7 instruction steps.

### `search-no-results.json`
- **Request:** none. Written by hand in the same shape as a real response.
- **Contents:** `{ "total": 0, "count": 0, "data": [] }`
- **Use it to test:** that an empty search gives an empty list (not an error), so the screen can show "No matches".

### `error-401-missing-key.json`
- **Request:** any endpoint without the `X-WorkoutX-Key` header (captured from `GET /v1/gifs/0025.gif`).
- **Contents:** `{ "error": "Unauthorized", "message": "API key required. ...", "docs": "..." }`. Status code 401.
- **Use it to test:** that an error response is turned into an "exercise library unavailable" failure instead of being parsed as exercises. Pair it with status code 401 in the test.

## Fields worth knowing
- `id` is a string (`"0025"`), not a number: keep the leading zeros.
- `apparatus` (extra gear such as a bench) only appears on body-weight exercises, so it's absent from every exercise in these files.
- `gifUrl` needs the API key in the `X-WorkoutX-Key` header and costs one request per GIF, so tests never load it. No GIF is stored here: the API's terms don't allow hosting its media.

## Not covered yet
- A `429` (rate limit or quota used up) response: capturing a real one means spending requests until the limit is hit.
