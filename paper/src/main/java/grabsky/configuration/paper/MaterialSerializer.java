package grabsky.configuration.paper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import grabsky.configuration.paper.exception.JsonSerializationException;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.lang.reflect.Type;

import static grabsky.configuration.paper.util.Conditions.requirePresent;

/** Converts {@link NamespacedKey} to {@link Material}. */
public final class MaterialSerializer implements JsonDeserializer<Material> {

    /** Default instance of {@link MaterialSerializer}. */
    public static final MaterialSerializer INSTANCE = new MaterialSerializer();

    private MaterialSerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public Material deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final NamespacedKey key = context.deserialize(element, NamespacedKey.class);
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        return requirePresent(Registry.MATERIAL.get(key), new JsonSerializationException(type, element));
    }

}
