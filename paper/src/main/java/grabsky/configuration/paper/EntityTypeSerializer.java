package grabsky.configuration.paper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import grabsky.configuration.paper.exception.JsonFormatException;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Type;

import static grabsky.configuration.paper.util.Conditions.requirePresent;

/** Converts {@link NamespacedKey} to {@link EntityType}. */
public final class EntityTypeSerializer implements JsonDeserializer<EntityType> {

    /** Default instance of {@link EntityTypeSerializer}. */
    public static final EntityTypeSerializer INSTANCE = new EntityTypeSerializer();

    private EntityTypeSerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public EntityType deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final NamespacedKey key = context.deserialize(element, NamespacedKey.class);
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        return requirePresent(Registry.ENTITY_TYPE.get(key), new JsonFormatException(type, element));
    }

}
