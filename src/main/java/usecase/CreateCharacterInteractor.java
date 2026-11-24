package usecase;

import entity.CharacterClass;
import entity.PlayerCharacter;
import entity.Race;
import java.util.List;

/** Coordinates character creation between the catalog and presentation boundaries. */
public class CreateCharacterInteractor {
  private final CatalogGateway catalog;
  private final CreateCharacterOutput output;

  /**
   * Creates the character creation use case.
   *
   * @param catalog the races and character classes as retrieved from the API
   * @param output the presenter that updates the view
   */
  public CreateCharacterInteractor(CatalogGateway catalog, CreateCharacterOutput output) {
    this.catalog = catalog;
    this.output = output;
  }

  /**
   * Validates the requested race and class, calculates stats, and reports the result in a {@code
   * CreateCharacterRequest} object.
   */
  public void execute(CreateCharacterRequest request) {
    Race race = findRace(catalog.getRaces(), request.getRaceName());
    CharacterClass cls = findClass(catalog.getClasses(), request.getClassName());

    if (race == null) {
      output.presentError("Unknown race: " + request.getRaceName());
      return;
    }
    if (cls == null) {
      output.presentError("Unknown class: " + request.getClassName());
      return;
    }

    PlayerCharacter character = new PlayerCharacter(race, cls);
    output.presentCreated(
        new CreateCharacterResponse(race.getName(), cls.getName(), character.getStats()));
  }

  /**
   * Finds the race whose name exactly matches the requested name.
   *
   * @param races the list of races, as called from the API
   * @param name the name of the desired race
   * @return the race with the same name as {@code name}, as a {@code Race} object
   */
  private Race findRace(List<Race> races, String name) {
    for (Race r : races) {
      if (r.getName().equals(name)) {
        return r;
      }
    }
    return null;
  }

  /**
   * Finds the character class whose name exactly matches the requested name.
   *
   * @param classes the list of character classes, as called from the API
   * @param name the name of the desired character class
   * @return the character class with the same name as {@code name}, as a {@code CharacterClass}
   *     object
   */
  private CharacterClass findClass(List<CharacterClass> classes, String name) {
    for (CharacterClass c : classes) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }
}
