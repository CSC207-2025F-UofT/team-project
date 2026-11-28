package use_case.browse_pets;

import java.util.List;

/**
 * What the visitor asked for: the filters to apply (none means show everything).
 */
public class BrowsePetsInputData {
    private final List<PetFilter> filters;

    /**
     * Creates the request.
     *
     * @param filters the filters to apply together
     */
    public BrowsePetsInputData(List<PetFilter> filters) {
        this.filters = List.copyOf(filters);
    }

    /**
     * Returns the filters to apply.
     *
     * @return the filters
     */
    public List<PetFilter> getFilters() {
        return filters;
    }
}
