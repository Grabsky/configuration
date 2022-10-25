package grabsky.configuration.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/** Converts {@link String} to {@link T} and vice-versa but using case-insensitive strategy. */
public class CaseInsensitiveEnumSerializer<T extends Enum<T>> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public JsonElement serialize(final T src, final Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override @SuppressWarnings("unchecked")
    public T deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonPrimitive() == false)
            throw new JsonParseException("Could not convert '" + element + "' to an enum.");
        // Unwrapping value as String
        final String cons = element.getAsString();
        // Iterating over enum constants
        for (final Enum<T> e : ((Class<Enum<T>>) type).getEnumConstants()) {
            // Comparing and returning if matches
            if (e.toString().equalsIgnoreCase(cons))
                return (T) e;
        }
        // No enum was found
        throw new JsonParseException("No enum matching '" + cons + "' was found for type '" + type.getTypeName() + "'.");
    }
}
