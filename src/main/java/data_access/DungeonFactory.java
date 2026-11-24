package data_access;

import entity.Direction;
import entity.Dungeon;
import entity.Item;
import entity.ItemType;
import entity.Monster;
import entity.Room;
import entity.Stats;

import java.util.Arrays;

/**
 * Builds a small hard-coded first floor so the Explore screen can be run and
 * demoed on its own. Item names follow D&D 5e equipment / magic items, so the
 * team can later replace this with items fetched from https://www.dnd5eapi.co/.
 *
 * <p>This is an example of the Factory pattern: it hides the details of building
 * and connecting rooms behind one method call.</p>
 *
 * <p>Map of floor 1:</p>
 * <pre>
 *              [Armory]
 *                 |
 *  [Library] - [Great Hall] - [Shrine]
 *                 |              |
 *            [Entrance]      [Goblin Den]
 * </pre>
 */
public final class DungeonFactory {

    /** Only static methods, so this class is never instantiated. */
    private DungeonFactory() {
    }

    /**
     * Builds the first floor: six connected rooms, six items and one monster.
     *
     * @return the first floor, with the character at the Entrance
     */
    public static Dungeon createFloorOne() {
        // 1. Create the rooms.
        Room entrance = new Room("Entrance",
                "Cold air drifts up a crumbling stone stairway. Torches gutter in iron sconces, "
                        + "and the floor is scratched by the claws of something that passed recently.");
        Room hall = new Room("Great Hall",
                "A vaulted hall with a cracked marble floor. Faded banners of a forgotten "
                        + "kingdom hang from the rafters. Passages lead in every direction.");
        Room armory = new Room("Armory",
                "Racks of rusted weapons line the walls. Most are useless, but a few pieces "
                        + "still gleam with a faint magical light.");
        Room library = new Room("Library",
                "Toppled shelves and scattered scrolls fill this dusty room. A reading desk "
                        + "holds a single unbroken vial.");
        Room shrine = new Room("Shrine",
                "A small shrine to a long-forgotten god. Candles still burn on the altar, "
                        + "though no one has lit them in centuries.");
        Room goblinDen = new Room("Goblin Den",
                "The stench of smoke and rotten meat fills this cramped cave. Bones are piled in the corner.");

        // 2. Connect them, following the map above. connect() also adds the door back.
        entrance.connect(Direction.NORTH, hall);
        hall.connect(Direction.NORTH, armory);
        hall.connect(Direction.WEST, library);
        hall.connect(Direction.EAST, shrine);
        shrine.connect(Direction.SOUTH, goblinDen);

        // 3. Put items in the rooms: new Item(name, description, type, power).
        entrance.addItem(new Item("Potion of Healing",
                "A red liquid that glimmers when agitated. Drinking it restores 2d4 + 2 hit points.",
                ItemType.POTION, 7));
        armory.addItem(new Item("Longsword +1",
                "A finely balanced blade humming with enchantment. +1 to attack and damage rolls.",
                ItemType.WEAPON, 9));
        armory.addItem(new Item("Chain Mail",
                "Heavy interlocking metal rings. Sets your Armor Class to 16.",
                ItemType.ARMOR, 6));
        library.addItem(new Item("Potion of Giant Strength",
                "A cloudy potion with a sliver of fingernail floating in it. "
                        + "Your Strength becomes 21 for one hour.",
                ItemType.POTION, 21));
        shrine.addItem(new Item("Dragon Slayer Greatsword",
                "A massive blade etched with draconic runes. Deals an extra 3d6 damage to dragons.",
                ItemType.WEAPON, 12));
        shrine.addItem(new Item("Shield of Faith",
                "A blessed shield bearing a sun emblem. +2 to Armor Class.",
                ItemType.ARMOR, 2));

        // 4. Add a monster, so walking into the Goblin Den triggers combat (extension 4a).
        //    Uses the team's Monster entity: new Monster(name, API index, max health,
        //    armour class, stats). The numbers are the D&D 5e goblin's: 7 HP, AC 15,
        //    STR 8, DEX 14, CON 10, INT 10, WIS 8, CHA 8.
        goblinDen.setMonster(new Monster("Goblin", "goblin", 7, 15,
                new Stats(8, 14, 10, 10, 8, 8)));

        // 5. Create floor 1, with the character starting at the Entrance.
        return new Dungeon(1,
                Arrays.asList(entrance, hall, armory, library, shrine, goblinDen),
                entrance);
    }
}
