package grabsky.configuration.paper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import grabsky.configuration.paper.exception.JsonSerializationException;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Type;

import static grabsky.configuration.paper.util.Conditions.requirePresent;

/** Converts {@link NamespacedKey} to {@link Enchantment}. */
public final class EnchantmentSerializer implements JsonDeserializer<Enchantment> {

    /** Default instance of {@link EnchantmentSerializer}. */
    public static final EnchantmentSerializer INSTANCE = new EnchantmentSerializer();

    private EnchantmentSerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public Enchantment deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        final NamespacedKey key = context.deserialize(element, NamespacedKey.class);
        // Throwing an exception in case JsonElement is not a JsonPrimitive, therefore definitely not a valid String.
        return requirePresent(Registry.ENCHANTMENT.get(key), new JsonSerializationException(type, element));
    }

}
