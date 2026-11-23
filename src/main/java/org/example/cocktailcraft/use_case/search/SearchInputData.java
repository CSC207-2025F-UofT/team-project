package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Tags;

import java.util.Set;

public class SearchInputData {
    private final String name;
    private final Set<Tags.BaseSpirit> baseSpirits;

    public SearchInputData(String name, Set<Tags.BaseSpirit> baseSpirits) {
        this.name = name;
        this.baseSpirits = baseSpirits;
    }

    String getName() {
        return this.name;
    }

    Set<Tags.BaseSpirit> getBaseSpirits() {
        return this.baseSpirits;
    }
}
