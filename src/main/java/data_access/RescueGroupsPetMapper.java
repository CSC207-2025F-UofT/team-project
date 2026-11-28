package data_access;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.ExternalPetListing;
import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;

/** Translates public JSON:API animal records into the gallery's pet model. */
final class RescueGroupsPetMapper {
    private static final String ATTRIBUTES = "attributes";
    private static final Pattern REPORTED_AGE =
            Pattern.compile("\\b([0-9]{1,3})\\s*(years?|yrs?|months?|mos?)\\b", Pattern.CASE_INSENSITIVE);

    private RescueGroupsPetMapper() {
    }

    static List<Pet> read(JSONObject response) {
        final Map<String, JSONObject> included = new HashMap<>();
        final JSONArray relatedRecords = response.optJSONArray("included", new JSONArray());
        for (int index = 0; index < relatedRecords.length(); index++) {
            final JSONObject related = relatedRecords.optJSONObject(index);
            if (related != null && related.optJSONObject(ATTRIBUTES) != null) {
                included.put(related.optString("type") + "/" + related.optString("id"),
                        related.getJSONObject(ATTRIBUTES));
            }
        }
        final Map<String, Pet> pets = new LinkedHashMap<>();
        final JSONArray animals = response.getJSONArray("data");
        for (int index = 0; index < animals.length(); index++) {
            final JSONObject animal = animals.optJSONObject(index);
            if (animal != null) {
                final Pet pet = toPet(animal, included);
                if (pet != null) {
                    pets.putIfAbsent(pet.getId(), pet);
                }
            }
        }
        return List.copyOf(pets.values());
    }

    private static Pet toPet(JSONObject animal, Map<String, JSONObject> included) {
        final JSONObject pet = animal.optJSONObject(ATTRIBUTES, new JSONObject());
        final JSONObject meta = animal.optJSONObject("meta", new JSONObject());
        final JSONObject relationships = animal.optJSONObject("relationships", new JSONObject());
        final JSONObject org = firstRelated(relationships, "orgs", included);
        final String organization = org.optString("name").strip();
        final String id = animal.optString("id").strip();
        final String name = pet.optString("name").strip();
        if (id.isEmpty() || name.isEmpty() || organization.isEmpty() || pet.optBoolean("isAdoptionPending")) {
            return null;
        }
        final String site = webUrl(org.optString("url"));
        final String adoptionUrl = firstText(webUrl(pet.optString("url")), webUrl(meta.optString("url")),
                webUrl(org.optString("adoptionUrl")), site);
        final String reportedAge = firstText(pet.optString("ageString"), meta.optString("ageString"));
        final int age = ageInYears(pet.optString("birthDate"), reportedAge);
        final String ageLabel = firstText(reportedAge,
                age >= 0 ? age + (age == 1 ? " year old" : " years old") : pet.optString("ageGroup"),
                "Age not reported");
        final String breed = firstText(pet.optString("breedString"), meta.optString("breedString"),
                pet.optString("breedPrimary"), firstRelated(relationships, "breeds", included).optString("name"),
                "Breed not reported");
        final JSONObject picture = firstRelated(relationships, "pictures", included);
        final String photoUrl = firstText(webUrl(picture.optString("small")),
                webUrl(pet.optString("pictureThumbnailUrl")), webUrl(picture.optString("large")));
        final ExternalPetListing listing = new ExternalPetListing(organization, site, adoptionUrl, ageLabel, age >= 0);
        return new Pet("rescuegroups:" + id, name, Species.CAT, breed, Math.max(0, age), sex(pet.optString("sex")),
                temperament(pet), healthNotes(pet), false, pet.optBoolean("isCurrentVaccinations"),
                pet.optBoolean("isAltered"), BigDecimal.ZERO, photoUrl, PetStatus.AVAILABLE, listing);
    }

    private static JSONObject firstRelated(JSONObject relationships, String type, Map<String, JSONObject> included) {
        final Object data = relationships.optJSONObject(type, new JSONObject()).opt("data");
        final JSONObject link;
        if (data instanceof JSONArray links) {
            link = links.optJSONObject(0);
        } else if (data instanceof JSONObject singleLink) {
            link = singleLink;
        } else {
            link = null;
        }
        return link == null ? new JSONObject()
                : included.getOrDefault(link.optString("type", type) + "/" + link.optString("id"), new JSONObject());
    }

    private static int ageInYears(String birthDate, String reportedAge) {
        try {
            final LocalDate birthday = LocalDate.parse(birthDate.split("T", 2)[0]);
            final LocalDate today = LocalDate.now();
            if (!birthday.isAfter(today)) {
                return Period.between(birthday, today).getYears();
            }
        } catch (DateTimeParseException exception) {
            // A listing can report an age instead of a birth date.
        }
        final Matcher matcher = REPORTED_AGE.matcher(reportedAge);
        if (matcher.find()) {
            final int amount = Integer.parseInt(matcher.group(1));
            return matcher.group(2).toLowerCase(Locale.ROOT).startsWith("m") ? amount / 12 : amount;
        }
        return -1;
    }

    private static PetSex sex(String sex) {
        return switch (sex.toLowerCase(Locale.ROOT)) {
            case "female" -> PetSex.FEMALE;
            case "male" -> PetSex.MALE;
            default -> PetSex.UNKNOWN;
        };
    }

    private static List<Temperament> temperament(JSONObject pet) {
        final EnumSet<Temperament> reported = EnumSet.noneOf(Temperament.class);
        for (String quality : pet.optString("qualities").split("[,;|]")) {
            final String token = quality.strip().toUpperCase(Locale.ROOT).replace(' ', '_');
            for (Temperament tag : Temperament.values()) {
                if (tag.name().equals(token)) {
                    reported.add(tag);
                }
            }
        }
        if (pet.optBoolean("isKidsOk")) {
            reported.add(Temperament.GOOD_WITH_KIDS);
        }
        if (pet.optBoolean("isCatsOk") && pet.optBoolean("isDogsOk")) {
            reported.add(Temperament.GOOD_WITH_OTHER_PETS);
        }
        return List.copyOf(reported);
    }

    private static String healthNotes(JSONObject pet) {
        final List<String> notes = new ArrayList<>();
        final String specialNeeds = pet.optString("specialNeedsDetails");
        if (!specialNeeds.isBlank()) {
            notes.add(specialNeeds);
        } else if (pet.optBoolean("isSpecialNeeds")) {
            notes.add("Special needs reported");
        }
        if (pet.opt("isCurrentVaccinations") instanceof Boolean vaccinated) {
            notes.add(vaccinated ? "Vaccinations current" : "Vaccinations not current");
        }
        if (pet.opt("isAltered") instanceof Boolean altered) {
            notes.add(altered ? "Spayed or neutered" : "Not spayed or neutered");
        }
        return String.join(" · ", notes);
    }

    private static String webUrl(String value) {
        try {
            final URI uri = URI.create(value.strip());
            if (uri.getHost() != null && uri.getUserInfo() == null && ("https".equalsIgnoreCase(uri.getScheme())
                    || "http".equalsIgnoreCase(uri.getScheme()))) {
                return uri.toASCIIString();
            }
        } catch (IllegalArgumentException exception) {
            // A missing or invalid link does not prevent browsing the listing.
        }
        return "";
    }

    private static String firstText(String... choices) {
        for (String choice : choices) {
            if (!choice.isBlank()) {
                return choice.strip();
            }
        }
        return "";
    }
}
