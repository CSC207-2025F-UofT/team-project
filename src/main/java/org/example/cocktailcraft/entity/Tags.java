package org.example.cocktailcraft.entity;

import java.util.Set;

public class Tags {
    public enum BaseSpirit {
        GIN,
        VODKA,
        RUM,
        TEQUILA,
        WHISKEY,
        BRANDY,
        OTHER
    }

    /** Broad flavor families derived only from explicit source flavor tags. */
    public enum Flavor {
        SWEET,
        SOUR_CITRUS,
        FRUITY_TROPICAL,
        BITTER,
        HERBAL_FLORAL,
        SMOKY_SPICY,
        COFFEE_NUTTY,
        SAVORY,
        DRY
    }

    /** Application-defined ranges for the source recipe ABV percentage. */
    public enum AlcoholRange {
        ZERO,
        LOW,
        MEDIUM,
        HIGH,
        UNKNOWN;

        public static AlcoholRange fromAbv(Double abv) {
            if (abv == null) {
                return UNKNOWN;
            }
            if (!Double.isFinite(abv) || abv < 0 || abv > 100) {
                throw new IllegalArgumentException("ABV must be between 0 and 100.");
            }
            if (abv == 0) {
                return ZERO;
            }
            if (abv < 10) {
                return LOW;
            }
            if (abv < 20) {
                return MEDIUM;
            }
            return HIGH;
        }
    }

    private final Set<BaseSpirit> baseSpirits;
    private final Set<Flavor> flavors;

    public Tags(Set<BaseSpirit> baseSpirits) {
        this(baseSpirits, Set.of());
    }

    public Tags(Set<BaseSpirit> baseSpirits, Set<Flavor> flavors) {
        this.baseSpirits = Set.copyOf(baseSpirits);
        this.flavors = Set.copyOf(flavors);
    }

    public Set<BaseSpirit> getBaseSpirits() {
        return this.baseSpirits;
    }

    public Set<Flavor> getFlavors() {
        return flavors;
    }

}
