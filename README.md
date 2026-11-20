# CSC207 RPG Project

A turn-based RPG battle prototype built with Java Swing for CSC207. Five heroes face three randomly selected monsters from final fantasy, and turn order is determined by their speed stat.

Players can select enemies to attack, and defend from. 
The interface includes animated characters, HP bars, and a turn-order display. 
Items and Run are currently placeholders to be implemented, and character stats are temporary values.

## Running the Project

Requires JDK 11 or newer. Open the project in IntelliJ IDEA and run `Main.java`.

An internet connection is needed to download assets initially. Downloaded assets are cached locally in `ffbe-cache/` for reuse.

## Code Structure

- `Main.java`: User interface, input handling, rendering, and animation.
- `Battle.java`: Fighters, turn order, attacks, defence, and battle outcomes.
- `GameAssets.java`: Asset storage and animation helpers.
- `FFBE_API.java`: Asset downloading, caching, and animation data processing.

## Asset and API Credits

Character animations, portraits, monster sprites, animation data, and the battle background are downloaded from [DaddyRaegen/ffbe_asset_dump](https://github.com/DaddyRaegen/ffbe_asset_dump).

Despite its name, `FFBE_API.java` downloads publicly accessible files through GitHub’s raw file service. It does not require an API key. No other external APIs are used in this version.

Characters and artwork originate from *Final Fantasy Brave Exvius* and belong to their respective owners. This project does not claim ownership of these assets, and attribution does not grant redistribution rights.
