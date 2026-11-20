# CSC207 RPG Project

A turn-based RPG battle prototype built with Java Swing for CSC207. 
Five heroes face three monsters, and turn order is determined by their speed stat.

Players can select enemies to attack or defend to reduce incoming damage. 
The interface includes animated characters, HP bars, and a turn-order display. 
Items and Run are currently placeholders to be implemented, and character stats are temporary values.

## Running the Project

Requires JDK 11 or newer. Open the project in IntelliJ IDEA and run `Main.java`.

An internet connection is needed to download assets initially. Downloaded assets are cached locally in `superpowers-cache/` for reuse. Each launch selects a random battle background, so an uncached background requires an internet connection.

## Code Structure

- `Main.java`: User interface, input handling, rendering, and animation.
- `Battle.java`: Fighters, turn order, attacks, defence, and battle outcomes.
- `GameAssets.java`: Asset storage and animation helpers.
- `Game_API.java`: Asset downloading, caching, GIF processing, and battle setup.

## Asset and API Credits

Character and monster animations and battle backgrounds are downloaded from the RPG Battle System pack in [Sparklin Labs’ Superpowers Asset Packs](https://github.com/sparklinlabs/superpowers-asset-packs). Portraits are cropped from idle animation frames.

Despite its name, `Game_API` downloads publicly accessible files through GitHub’s raw file service. It does not require an API key. No other external APIs are used in this version.

Artwork was created by Pixel-boy at Sparklin Labs and released under the Creative Commons Zero (CC0) licence, which permits use, modification, and redistribution. This project does not claim authorship of the original artwork.
