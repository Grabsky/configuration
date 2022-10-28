package grabsky.configuration.paper;

import com.google.gson.*;
import grabsky.configuration.paper.exception.JsonFormatException;
import grabsky.configuration.paper.object.EnchantmentEntry;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Type;

/** Converts {@link NamespacedKey} {@code (key)} and {@link Integer} {@code (level)} to {@link Enchantment}. */
public final class EnchantmentEntrySerializer implements JsonDeserializer<EnchantmentEntry> {

    /** Default instance of {@link EnchantmentSerializer}. */
    public static final EnchantmentEntrySerializer INSTANCE = new EnchantmentEntrySerializer();

    private EnchantmentEntrySerializer() { /* INSTANTIATING NOT ALLOWED */ }

    @Override
    public EnchantmentEntry deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        if (element instanceof JsonObject json) {
            final Enchantment enchantment = context.deserialize(json.get("key"), Enchantment.class);
            final int level = json.get("level").getAsInt();
            return new EnchantmentEntry(enchantment, level);
        }
        throw new JsonFormatException(type, element);
    }

}