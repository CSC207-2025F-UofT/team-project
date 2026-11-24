package usecase;

import entity.CharacterClass;
import entity.Race;
import java.util.List;

/**
 * The interface to be implemented by DnD5eCatalogGateway. Has methods to return the races and
 * character classes in the API.
 */
public interface CatalogGateway {
  /** Returns the available races and their ability bonuses. */
  List<Race> getRaces();

  /** Returns the available character classes and their base stats. */
  List<CharacterClass> getClasses();
}
