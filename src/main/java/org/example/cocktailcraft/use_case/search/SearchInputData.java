package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Tags;

import java.util.Set;

public class SearchInputData {
    private final String name;
    private final Set<Tags.BaseSpirit> baseSpirits;

    public SearchInputData(String name, Set<Tags.BaseSpirit> baseSpirits) {
        this.name = name == null ? "" : name.strip();
        this.baseSpirits = baseSpirits == null ? Set.of() : Set.copyOf(baseSpirits);
    }

    String getName() {
        return this.name;
    }

    Set<Tags.BaseSpirit> getBaseSpirits() {
        return this.baseSpirits;
    }
}
