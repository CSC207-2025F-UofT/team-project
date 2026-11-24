package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.Strictness;

import java.util.UUID;

/** Lenient readers for Supabase responses: anything missing or of the wrong type reads as null. */
final class SupabaseJson {
    private static final Gson JSON = new GsonBuilder().setStrictness(Strictness.STRICT).create();

    private SupabaseJson() { }

    static JsonElement parse(String body) {
        try {
            return JSON.fromJson(body, JsonElement.class);
        } catch (JsonParseException ignored) {
            return null;
        }
    }

    static JsonObject object(JsonElement value) {
        return value != null && value.isJsonObject() ? value.getAsJsonObject() : null;
    }

    static String string(JsonObject object, String property) {
        final JsonElement value = object == null ? null : object.get(property);
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()
                ? value.getAsString() : null;
    }

    static UUID id(JsonObject object) {
        final String value = string(object, "id");
        try {
            return value == null ? null : UUID.fromString(value);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
