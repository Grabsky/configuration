package grabsky.configuration.serializers;

import grabsky.configuration.exceptions.SerializationFailedException;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.function.Function;

import static grabsky.configuration.helpers.Preconditions.requirePresent;

public final class EnchantmentSerializer implements TypeSerializer<Enchantment> {
    /* Singleton. */ public static final EnchantmentSerializer INSTANCE = new EnchantmentSerializer();

    private final Function<NamespacedKey, @Nullable Enchantment> toEnchantment;

    // INTERNAL USE ONLY
    private EnchantmentSerializer() {
        this.toEnchantment = Registry.ENCHANTMENT::get;
    }

    public EnchantmentSerializer(final Function<NamespacedKey, @Nullable Enchantment> toEnchantment) {
        this.toEnchantment = toEnchantment;
    }

    @Override
    public Enchantment deserialize(final Type type, @NotNull final ConfigurationNode node) throws SerializationException {
        // Getting value as NamespacedKey if possible, throwing exception otherwise.
        final NamespacedKey key = requirePresent(node.get(NamespacedKey.class), new SerializationFailedException(NamespacedKey.class, node, null));
        // Returning Enchantment if present, throwing exception otherwise.
        return requirePresent(toEnchantment.apply(key), new SerializationFailedException(Enchantment.class, node, key.asString()));
    }

    @Override
    public void serialize(final Type type, @Nullable final Enchantment obj, final ConfigurationNode node) { /* NOT SUPPORTED */ }
}
