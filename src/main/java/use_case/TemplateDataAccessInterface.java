package use_case;

import entities.Template;

import java.util.List;

/**
 * Storage for workout templates.
 * A template's name is its unique key.
 */
public interface TemplateDataAccessInterface {

    /** Saves a template, overwriting any existing template with the same name. */
    void save(Template template);

    /** @return the template with the given name, or null if none exists. */
    Template get(String name);

    /** @return true if a template with the given name exists. */
    boolean existsByName(String name);

    /** @return all saved templates. */
    List<Template> getAll();

    /**
     * Deletes the template with the given name.
     * @return true if a template was deleted, false if none matched.
     */
    boolean delete(String name);
}
