# Agent instructions

These rules apply to everyone committing to this repository, whether you write the commit yourself or an AI agent writes it for you.

## Commit messages

Use [Conventional Commits](https://www.conventionalcommits.org/):

```
type(scope): subject

Optional body explaining what changed and why.
```

### Type

| Type | Use it for |
|---|---|
| `feat` | a new feature or new behaviour |
| `fix` | a bug fix |
| `refactor` | a code change that doesn't change behaviour |
| `tune` | balance, feel or number tweaks (speed, damage, spawn rates) that aren't bug fixes |
| `test` | adding or changing tests |
| `docs` | README, design docs, comments-only changes, these instructions |
| `chore` | build files, `.gitignore`, dependencies, other housekeeping |
| `revert` | undoing an earlier commit |

### Scope (optional)

A short lowercase name for the area you changed, such as `player`, `enemies`, `spells`, `waves`, `game-loop`, `view` or `menu`. Leave it out when the change doesn't belong to one area.

### Subject

- Lowercase after the colon.
- Imperative mood: "add", "fix", "remove", not "added" or "adds".
- No trailing period.
- Describe the change in plain words, not the file names you touched.

### Body (optional)

Add a body for anything that isn't obvious from the subject:

- Leave a blank line after the subject.
- Wrap lines at about 72 characters.
- Explain what changed and why. When tuning, give the old and new numbers.
- Use `- ` bullets when the commit makes several separate changes.

### Examples

```
feat(player): add WASD and arrow key movement
fix(game-loop): stop the loop when the window closes
refactor: replace ternary operators with if statements in GameController
tune(enemies): lower slime speed from 3 to 2

Slimes reached the player before the first spell was off cooldown,
so wave 1 was almost impossible for new players.
```

### Other rules

- One logical change per commit. Don't mix a feature with an unrelated refactor.
- Put docs-only updates in their own `docs:` commit, separate from code changes.
- Don't add AI assistants as co-authors, and don't add AI attribution lines (such as `Co-Authored-By: Claude` or "Generated with ...") to commit messages or pull request descriptions.

## Branches

Name branches after the commit type: `feat/...`, `fix/...`, `refactor/...`, `tune/...`, `docs/...`, using short lowercase words separated by hyphens. Example: `feat/enemy-spawning`.
