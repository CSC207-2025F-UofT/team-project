package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Tags;

import java.util.Set;

public class SearchInputData {
    private final String name;
    private final Set<Tags.BaseSpirit> baseSpirits;
    private final Set<Tags.Flavor> flavors;
    private final Set<Tags.AlcoholRange> alcoholRanges;

    public SearchInputData(String name, Set<Tags.BaseSpirit> baseSpirits) {
        this(name, baseSpirits, Set.of(), Set.of());
    }

    public SearchInputData(String name, Set<Tags.BaseSpirit> baseSpirits,
                           Set<Tags.Flavor> flavors, Set<Tags.AlcoholRange> alcoholRanges) {
        this.flavors = flavors == null ? Set.of() : Set.copyOf(flavors);
        this.alcoholRanges = alcoholRanges == null ? Set.of() : Set.copyOf(alcoholRanges);
        this.name = name == null ? "" : name.strip();
        this.baseSpirits = baseSpirits == null ? Set.of() : Set.copyOf(baseSpirits);
    }

    String getName() {
        return this.name;
    }

    Set<Tags.BaseSpirit> getBaseSpirits() {
        return this.baseSpirits;
    }

    Set<Tags.Flavor> getFlavors() {
        return flavors;
    }

    Set<Tags.AlcoholRange> getAlcoholRanges() {
        return alcoholRanges;
    }

}
