# Bar Assistant dataset snapshot

- Repository: https://github.com/bar-assistant/data
- Source branch: `v5`
- Pinned commit: `5a504d474614494119882eb91a8ffdc5491a483f`
- Snapshot: https://github.com/bar-assistant/data/tree/5a504d474614494119882eb91a8ffdc5491a483f
- Imported on: 2026-11-15
- License: see the unmodified upstream `LICENSE` in this directory.

## Contents

All 967 JSON files under the upstream `data/` directory are included:
663 cocktail recipes, 296 ingredient records, and 8 auxiliary JSON files.
The upstream directory structure and file contents are unchanged.
The upstream `README.md` and `schema.json` are also included unchanged.
Images and other non-JSON assets are not bundled; image references in the
JSON are preserved but may not resolve locally.

Every imported upstream file was verified against its Git blob SHA from the
pinned repository tree. All JSON files parse successfully. The 663 cocktail
recipes have distinct `_id` values and contain the name, ingredient name,
amount, and units fields used by the current sample reader. This is not a
full JSON Schema or runtime validation of every recipe.

## Application status

This is an offline dataset snapshot, not an external API connection.
The existing `/cocktails/daiquiri.json` remains a separate smoke-check sample.
`cocktail-index.txt` is a project-generated manifest containing the absolute
classpath paths of all 663 `data/cocktails/*/data.json` files, sorted by path.
It is not an upstream file. Regenerate it when recipes are added or removed.
`CocktailJsonReader.readAll()` reads that index and loads the listed recipes;
`DataReadCheck` verifies the full dataset, without counting the separate sample
a second time. Ingredient records and auxiliary JSON are not separately loaded.
The reader maps only recipe name and ingredient name, amount, and units.
The Swing entry point has not yet been wired to use the resulting list.
