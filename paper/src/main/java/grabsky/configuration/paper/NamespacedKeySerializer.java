package grabsky.configuration.paper;

import com.google.gson.*;
import grabsky.configuration.paper.exception.JsonFormatException;
import org.bukkit.NamespacedKey;

import java.lang.reflect.Type;

/** Converts {@link String} to {@link org.bukkit.NamespacedKey}. */
public final class NamespacedKeySerializer implements JsonDeserializer<NamespacedKey> {

    /** Default instance of {@link NamespacedKeySerializer}. */
    public static final NamespacedKeySerializer INSTANCE = new NamespacedKeySerializer();

    private NamespacedKeySerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public NamespacedKey deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonPrimitive json) {
            try {
                return NamespacedKey.fromString(json.getAsString().toLowerCase());
            } catch (final IllegalArgumentException e) {
                throw new JsonFormatException(type, json);
            }
        }
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        throw new JsonFormatException(type, element);
    }

}
