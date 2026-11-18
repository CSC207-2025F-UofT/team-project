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

    private final Set<BaseSpirit> baseSpirits;

    public Tags(Set<BaseSpirit> baseSpirits) {
        this.baseSpirits = Set.copyOf(baseSpirits);
    }

    public Set<BaseSpirit> getBaseSpirits() {
        return this.baseSpirits;
    }
}
